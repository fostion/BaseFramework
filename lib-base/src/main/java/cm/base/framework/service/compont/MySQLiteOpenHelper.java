package cm.base.framework.service.compont;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import cm.base.framework.greendao.DaoMaster;
import cm.base.framework.greendao.StudentDao;
import cm.base.framework.utils.MigrationHelper2;

/**
 * 自定义  MySQLiteOpenHelper集成  DaoMaster.OpenHelper 重写更新数据库的方法
 * 当app下的build.gradle  的schemaVersion数据库的版本号改变时，，创建数据库会调用onUpgrade更细数据库的方法
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    /**
     * @param context 上下文
     * @param name    原来定义的数据库的名字   新旧数据库一致
     * @param factory 可以null
     */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion 更新数据库的时候自己调用
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //具体的数据转移在MigrationHelper2类中,处理数据库升级，导致数据被删除问题
        MigrationHelper2.migrate(db, StudentDao.class);
    }
}

