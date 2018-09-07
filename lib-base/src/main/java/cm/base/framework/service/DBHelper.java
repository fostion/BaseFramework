package cm.base.framework.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cm.base.framework.greendao.DaoMaster;
import cm.base.framework.greendao.DaoSession;
import cm.base.framework.service.compont.MySQLiteOpenHelper;

/**
 * 数据库操作
 */

public class DBHelper {

    private static DBHelper mInstance;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private String dbName = "demo_db";

    private DBHelper() {

    }

    public static DBHelper getInstance() {
        if (mInstance == null) {
            synchronized (DBHelper.class) {
                if (mInstance == null) {
                    mInstance = new DBHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context, dbName, null);
        mSQLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(mSQLiteDatabase);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

}
