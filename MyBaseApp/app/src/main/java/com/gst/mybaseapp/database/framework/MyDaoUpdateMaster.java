package com.gst.mybaseapp.database.framework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gst.mybaseapp.database.been.CityDao;
import com.gst.mybaseapp.database.been.CreditCardDao;
import com.gst.mybaseapp.database.been.DaoMaster;
import com.gst.mybaseapp.database.been.LoginBuserNameDao;
import com.gst.mybaseapp.database.been.NoteTestDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这里重写了 onCreate 和 onUpgrade
 * author: GuoSongtao on 2019/4/9 17:20
 * email: 157010607@qq.com
 */
public class MyDaoUpdateMaster extends DaoMaster.OpenHelper {

    private static final String TAG = "MyDaoMaster";

    public MyDaoUpdateMaster(Context context, String name) {
        super(context, name);
    }

    public MyDaoUpdateMaster(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 其中城市数据库是写死了的不需要变动  所以不调用CityDao的createTable和dropTable
     * 注意 createAllTables和 dropAllTables 中的数据库都是可以删除的数据哦
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //方案一
        UpdateMigrationHelper.migrate(db, new UpdateMigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                createAllTables(db, false, NoteTestDao.class, LoginBuserNameDao.class, CreditCardDao.class);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                dropAllTables(db, false, NoteTestDao.class, LoginBuserNameDao.class, CreditCardDao.class);
            }
        }, NoteTestDao.class, LoginBuserNameDao.class, CreditCardDao.class, CityDao.class);

        //方案二
        //UpdateMigrationHelper.migrate(db, NoteTestDao.class, LoginBuserNameDao.class, CreditCardDao.class);
        Log.e(TAG, "onUpgrade: " + oldVersion + " newVersion = " + newVersion);
    }

    /**
     * 其中城市数据库是写死了的不需要变动  所以不调用CityDao的createTable和dropTable
     */
    @Override
    public void onCreate(Database db) {
//        DaoMaster. createAllTables(db, true);

        createAllTables(db, false, NoteTestDao.class, LoginBuserNameDao.class, CreditCardDao.class);
    }


    private static void createAllTables(Database db, boolean ifNotExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
        Log.e(TAG, "【Create all table by reflect】");
    }

    private static void dropAllTables(Database db, boolean ifExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "dropTable", ifExists, daoClasses);
        Log.e(TAG, "【Drop all table by reflect】");
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses.length < 1) {
            return;
        }
        try {
            for (Class cls : daoClasses) {
                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, isExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
