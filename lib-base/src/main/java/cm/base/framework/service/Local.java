package cm.base.framework.service;

import android.content.Context;
import android.support.annotation.Nullable;

import io.paperdb.Paper;

/**
 * 本地缓存
 */
public class Local {

    public static void init(Context context) {
        Paper.init(context);
    }

    public static void save(String key, Object value) {
        Paper.book().write(key,value);
    }

    @Nullable
    public static <T> T get(String key) {
        return Paper.book().read(key);
    }

    public static void clearCache(String key){
        Paper.book().delete(key);
    }

    public static void clearAllCache(){
        Paper.book().destroy();
    }


}
