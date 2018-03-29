package cm.base.framework.utils;

import android.util.Log;

/**
 * log 打印控制
 * Created by fostion on 2018/3/29.
 */

public class L {

    private static boolean isDebug = true;

    public static void i(String tag, String msg){
        if (isDebug){
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (isDebug){
            Log.e(tag, msg);
        }
    }

}
