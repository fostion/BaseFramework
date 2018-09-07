package cm.base.framework.base;

import android.app.Application;

import cm.base.framework.service.DBHelper;
import cm.base.framework.service.Local;

/**
 * Created by fostion on 2016/12/30.
 */

public class BaseApplication extends Application {

    public static BaseApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        initTools();
    }

    //初始化工具类
    private void initTools() {
        //缓存
        Local.init(getApplicationContext());
        DBHelper.getInstance().init(this);
    }

}
