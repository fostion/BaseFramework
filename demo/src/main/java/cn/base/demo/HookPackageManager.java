package cn.base.demo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

import cm.base.framework.utils.ReflectUtil;

/**
 * Hook PackageManager处理so校验签名
 *
 */

public class HookPackageManager {

    private static final String TAG = "HookPackageManager";
    private static final String sign = "308202bf308201a7a00302010202045a58aa8d300d06092a864886f70d01010b0500300f310d300b06035504031304666972653020170d3132313130323034313630375a180f32313132313030393034313630375a300f310d300b060355040313046669726530820122300d06092a864886f70d01010105000382010f003082010a02820101008859744a6f1851c69589c85ff22a920435b3cbae30b26dbc8ced83a69ddd17019481a10393997829bd592761a5edd83ab8032d82891aa5d63b78be92c5a6d8fc9afe199f56bda79e6d2abcaade0760deb461aa102e01737a641922af7b62c008429af546c6b9e1010d672fb30b681a905f158a9a1543159d681c4ce7fb0b00c265fa2c48a105b998a89c3f4d7bead693ba2a46aec1429cf9d95b764619ebc88c1968939cd9cedc57c3b0d92e16be39bf30fadc01583b7effe7985e204f9f84770129f5f0a84ca3d13fe1bff889134e4c5e25aa414bd0224d5fcb96077e89fe177b3c7aa1463dfb5b286bfced0713abe12f64e671f632f328e6741c34456c1e090203010001a321301f301d0603551d0e0416041429f18adefd7e15c1e3f8fb66ec148eb7c4f120fe300d06092a864886f70d01010b050003820101001eb975218533eaceb221297883c82ab6e47570219e3da9479622d101bdcd16cdbe744fc199e184f1eb8aa8ed9515cbc4a8713cd010d4fb6a8a3c5a8be2a58c4e4bc77307932c24876c7784ce22c322075db9ca7886f5fbed3108a9b63b2730af5aa11327e5b1387427af4c853581442c8dabf25fd81864cfa43e8a667e777fd932ac9fa2800969077b5015fd04087cb069a14d9f993c44570d13d124135ab8b6eb0953f6592c46079933a7fae6f2e4f7d7598eddb2a1cd7239a1b973a896751dd93e05568af0147828dafec030addd2dba82db517b37c98ee14974fa3319d79dbc8ec79a392307b5e2512478e4958f9979e5eef91089e482e7b7db2ea013738c";
    private static boolean isHook = false;
    private static boolean isInit = false;

    public static void startHook(Activity context){
        isHook = true;
        if (!isInit){
            clear(context);
            hook(context);
            isInit = true;
        }
    }

    public static void stopHook(){
        isHook = false;
    }

    private static void clear(Activity context){
        try {
            Object object = ReflectUtil.getField("android.content.ContextWrapper", context, "mBase");
            ReflectUtil.setField("android.app.ContextImpl", object, "mPackageManager", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hook(final Context context) {
        try {
            //替换IPackageManager， 新的IPackageManager增加动态代理逻辑
            //反射获取pm
            final Object oldPM = ReflectUtil.getField("android.app.ActivityThread", null, "sPackageManager");
            Class iPM = Class.forName("android.content.pm.IPackageManager");
            //使用动态代理修改值
            Object newPM = Proxy.newProxyInstance(iPM.getClassLoader(), new Class[]{iPM}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    if (!isHook){
                        return method.invoke(oldPM, objects);
                    }

                    Log.d(TAG, "method: " + method.getName());

                    //检测方法
                    if ("getInstalledPackages".equals(method.getName())) {
                        List<PackageInfo> apps = (List<PackageInfo>) method.invoke(oldPM, objects);
                        Iterator<PackageInfo> iter = apps.iterator();
                        while (iter.hasNext()) {
                            PackageInfo packageinfo = iter.next();
                            String thisName = packageinfo.packageName;
                            if (thisName.equals(context.getPackageName())) {
                                packageinfo.signatures[0] =  new Signature(sign);
                            }
                        }
                        return apps;
                    } else if("getPackageInfo".equals(method.getName())){
                        PackageInfo pkgInfo = (PackageInfo)method.invoke(oldPM, objects);
                        String thisName = pkgInfo.packageName;
                        if (thisName.equals(context.getPackageName())) {
                            pkgInfo.signatures[0] =  new Signature(sign);
                        }

                        return pkgInfo;
                    } else {
                        return method.invoke(oldPM, objects);
                    }
                }
            });
            //设置反射后的对象
            ReflectUtil.setField("android.app.ActivityThread", null, "sPackageManager", newPM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
