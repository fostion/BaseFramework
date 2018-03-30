package cn.base.demo.login;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.util.Log;

import com.ipmacro.ppcore.BaseDownload;
import com.ipmacro.ppcore.FlvApple;
import com.ipmacro.ppcore.FlvDownload;
import com.ipmacro.ppcore.M3U8Download;
import com.ipmacro.ppcore.PPCore;

import java.util.List;

import cm.base.framework.base.BaseActivity;
import cm.base.framework.base.BasePresenter;
import cn.base.demo.R;

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    private LoginPresenter loginPresenter;

    @Nullable
    @Override
    public BasePresenter bindPresenter() {
        loginPresenter = new LoginPresenter(this);
        return loginPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //调用p层逻辑
//        loginPresenter.login();


        String sign = "308202bf308201a7a00302010202045a58aa8d300d06092a864886f70d01010b0500300f310d300b06035504031304666972653020170d3132313130323034313630375a180f32313132313030393034313630375a300f310d300b060355040313046669726530820122300d06092a864886f70d01010105000382010f003082010a02820101008859744a6f1851c69589c85ff22a920435b3cbae30b26dbc8ced83a69ddd17019481a10393997829bd592761a5edd83ab8032d82891aa5d63b78be92c5a6d8fc9afe199f56bda79e6d2abcaade0760deb461aa102e01737a641922af7b62c008429af546c6b9e1010d672fb30b681a905f158a9a1543159d681c4ce7fb0b00c265fa2c48a105b998a89c3f4d7bead693ba2a46aec1429cf9d95b764619ebc88c1968939cd9cedc57c3b0d92e16be39bf30fadc01583b7effe7985e204f9f84770129f5f0a84ca3d13fe1bff889134e4c5e25aa414bd0224d5fcb96077e89fe177b3c7aa1463dfb5b286bfced0713abe12f64e671f632f328e6741c34456c1e090203010001a321301f301d0603551d0e0416041429f18adefd7e15c1e3f8fb66ec148eb7c4f120fe300d06092a864886f70d01010b050003820101001eb975218533eaceb221297883c82ab6e47570219e3da9479622d101bdcd16cdbe744fc199e184f1eb8aa8ed9515cbc4a8713cd010d4fb6a8a3c5a8be2a58c4e4bc77307932c24876c7784ce22c322075db9ca7886f5fbed3108a9b63b2730af5aa11327e5b1387427af4c853581442c8dabf25fd81864cfa43e8a667e777fd932ac9fa2800969077b5015fd04087cb069a14d9f993c44570d13d124135ab8b6eb0953f6592c46079933a7fae6f2e4f7d7598eddb2a1cd7239a1b973a896751dd93e05568af0147828dafec030addd2dba82db517b37c98ee14974fa3319d79dbc8ec79a392307b5e2512478e4958f9979e5eef91089e482e7b7db2ea013738c";

        Context context = new ContextWrapper(this);
//        context.getPa

        PPCore.init(this);
        PPCore.login4(getBaseContext(), 0);
        final BaseDownload download = new M3U8Download();

        String playUrl = "http://125.88.70.140:30001/PLTV/88888956/224/3221227692/2.m3u8";
//        String playUrl = "https://videopull.10jqka.com.cn:8188/diwukejibenmianxuangufangfa_1505989287.flv";
        download.setHeader("");
        download.setSrcUrl(playUrl);
        download.start(playUrl);
//        download.setSrcUrl("https://videopull.10jqka.com.cn:8188/diwukejibenmianxuangufangfa_1505989287.flv");


        new Thread(){
            @Override
            public void run() {
                boolean isPrepare = false;
                while (!isPrepare){
                    isPrepare = download.prepare2();
                }
                Log.i("test_log", "url:"+download.getPlayUrl());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean startRxBus() {
        return true;
    }

    @Override
    public void loginSucc() {

    }

    @Override
    public void loginFail() {
    }
}
