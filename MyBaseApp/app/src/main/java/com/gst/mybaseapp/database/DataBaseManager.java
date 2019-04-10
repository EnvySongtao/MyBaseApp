package com.gst.mybaseapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.base.MyApp;
import com.gst.mybaseapp.database.been.City;
import com.gst.mybaseapp.database.been.CityDao;
import com.gst.mybaseapp.database.been.DaoMaster;
import com.gst.mybaseapp.database.been.DaoSession;
import com.gst.mybaseapp.database.been.LoginBuserName;
import com.gst.mybaseapp.database.been.LoginBuserNameDao;
import com.gst.mybaseapp.database.cityPinyinSort.CityPinyinComparator;
import com.gst.mybaseapp.database.framework.MyDaoUpdateMaster;
import com.gst.mybaseapp.utils.FileUtil;
import com.gst.mybaseapp.utils.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author: GuoSongtao on 2019/4/9 15:39
 * email: 157010607@qq.com
 */
public class DataBaseManager implements DataBaseManagerImpl {
    private final static String databasepath = "/data/data/%s/database"; // %s is //
    private static final String TAG = "DataBaseManager";

    private CityDao mCityDao;
    private CityPinyinComparator cityPinyinComparator = CityPinyinComparator.getInstance();
    private DaoSession mDaoSession = null;

    //因为用到了 initCityDb 所以只能用饥饿单例模式
    private static DataBaseManager ourInstance = null;

    public synchronized static DataBaseManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataBaseManager();
        }
        return ourInstance;
    }

    /**
     * city 用 CityDbName = "YunRichCity.db";// 城市编码数据库名
     * 平时的数据库用 AppConfig.AppDbName
     */
    private DataBaseManager() {

        File file = new File(getDatabaseFile(AppConfig.CityDbName));
        if (!file.exists()) {
            Log.e(TAG, "getCitySession: 数据库文件不存在");
        }

        //支持数据库更新 注意增加数据库dao 要更新等级  不然不认为是在更新
        MyDaoUpdateMaster helper = new MyDaoUpdateMaster(MyApp.getInstance(), getDatabaseFile(AppConfig.CityDbName));
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        mCityDao = mDaoSession.getCityDao();

    }

    @Override
    public String getDataBaseInfo() {
        File dbFile = new File(getDatabaseFile(AppConfig.CityDbName));
        return "dbFile.exists()  = " + (dbFile.exists()) + " ; dbFile.length() = " + (dbFile.length());
    }

    /**
     * 获取所有的省名称
     *
     * @return
     */
    @Override
    public List<String> getAllProvices() {
        List<String> allProvice = new ArrayList<>();
        Cursor pexecQuery = null;
        try {
            if (mCityDao == null) return allProvice;

            pexecQuery = mCityDao.getDatabase().rawQuery("SELECT distinct (PROVNAME) from YunRichCity2;", null);
            while (pexecQuery.moveToNext()) {
                String provice = pexecQuery.getString(0);
                allProvice.add(provice);
            }
            Collections.sort(allProvice, cityPinyinComparator);
            pexecQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pexecQuery.close();
            } catch (Exception e) {
            }
        }
        return allProvice;
    }

    /**
     * @param areaMap key为省份 值为对应的城市列表
     * @return
     */
    @Override
    public void updateProvicesAndAreas(Map<String, List<String>> areaMap, List<String> allProvices) {
        areaMap.clear();
        for (String proStr : allProvices) {
            areaMap.put(proStr, new ArrayList<>());
        }
        Cursor cexecQuery = null;
        try {
            if (mCityDao == null) return;

            cexecQuery = mCityDao.getDatabase().rawQuery("SELECT AREANAME,PROVNAME from YunRichCity2", null);
            while (cexecQuery.moveToNext()) {
                String area = cexecQuery.getString(0);
                String provice = cexecQuery.getString(1);
                List<String> allCites = areaMap.get(provice);
                allCites.add(area);
            }

            Iterator iter = areaMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                List<String> allCites = (List<String>) entry.getValue();
                Collections.sort(allCites, cityPinyinComparator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cexecQuery.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 根据省名和城市名获取城市信息
     * <p>
     * QueryBuilder的常见方法：
     * where(WhereCondition cond, WhereCondition... condMore): 查询条件，参数为查询的条件！
     * or(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore): 嵌套条件或者，用法同or。
     * and(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore): 嵌套条件且，用法同and。
     * join(Property sourceProperty, Class<J> destinationEntityClass):多表查询，后面会讲。
     * 输出结果有四种方式，选择其中一种最适合的即可，list()返回值是List,而其他三种返回值均实现Closeable,需要注意的不使用数据时游标的关闭操作：
     * list （）所有实体都加载到内存中。结果通常是一个没有魔法的 ArrayList。最容易使用。
     * listLazy （）实体按需加载到内存中。首次访问列表中的元素后，将加载并缓存该元素以供将来使用。必须关闭。
     * listLazyUncached （）实体的“虚拟”列表：对列表元素的任何访问都会导致从数据库加载其数据。必须关闭。
     * listIterator （）让我们通过按需加载数据（懒惰）来迭代结果。数据未缓存。必须关闭。
     * <p>
     * 查询即可：
     * <p>
     * eq()："equal ('=?')" 等于；
     * notEq() ："not equal ('<>?')" 不等于；
     * like()：" LIKE ?" 值等于；
     * between()：" BETWEEN ? AND ?" 取中间范围；
     * in()：" IN ("  in命令;
     * notIn()：" NOT IN (" not in 命令;
     * gt()：">?"  大于;
     * lt()："<?"  小于;
     * ge()：">=?"  大于等于;
     * le()："<=? "  小于等于;
     * isNull()：" IS NULL" 为空;
     * isNotNull()：" IS NOT NULL" 不为空;
     *
     * @param proviceName
     * @param areaName
     * @return
     */
    @Override
    public City getAreaCode(String proviceName, String areaName) {
        City city = null;
        try {
            if (mCityDao == null) return city;

            QueryBuilder<City> queryBuilder = mCityDao.queryBuilder();
            queryBuilder.where(CityDao.Properties.PROVNAME.eq(proviceName), CityDao.Properties.AREANAME.eq(areaName));
            List<City> list = queryBuilder.list();
            if (list != null && list.size() > 0)
                city = queryBuilder.list().get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return city;
    }

    /**
     * 获取所有登录账号名字
     *
     * @return
     */
    @Override
    public List<String> getAllLoginNames() {
        LoginBuserNameDao dao = mDaoSession.getLoginBuserNameDao();
        List<LoginBuserName> loginBuserNameList = dao.loadAll();
        List<String> names = new ArrayList<>();

        Log.i("DataBaseManager", "loginBuserNameList.size() = " + loginBuserNameList.size());
        if (!loginBuserNameList.isEmpty()) {
            for (int i = loginBuserNameList.size() - 1; i >= 0; i--) {
                names.add(loginBuserNameList.get(i).getBUsername());
            }
        }
        return names;
    }

    /**
     * 保存登录名和密码
     *
     * @return
     */
    @Override
    public void saveBusername(String bUsername, String password) {
        LoginBuserNameDao dao = mDaoSession.getLoginBuserNameDao();
        LoginBuserName loginBuserName = new LoginBuserName(2L, bUsername, password);
        loginBuserName.setBUsername(bUsername);
        loginBuserName.setPassword(password);
        //注意greenDao的save：有ID去update 没ID去insert    loginBuserName的id=2直接去update会失败
        if (dao.load(2L) == null) {
            dao.insert(loginBuserName);
        } else {
            dao.update(loginBuserName);
        }


        //删除CreditCard旧数据
//        CreditCardDao creditCardDao = mDaoSession.getCreditCardDao();
//        List<CreditCard> creditCards = creditCardDao._queryLoginBuserName_CreditCards(loginBuserName.getId());
//        creditCardDao.deleteInTx(creditCards);
//
//        //LoginBuserNameDao 有一对多（一个登录名多张信用卡）
//        //插入对应的CreditCard数据
//        Random random = new Random();
//        for (int j = 0; j < random.nextInt(5) + 1; j++) {
//            CreditCard creditCard = new CreditCard();
//            creditCard.setUserId(loginBuserName.getId());
//            creditCard.setUserName(loginBuserName.getBUsername());
//            creditCard.setCardNum(String.valueOf(random.nextInt(899999999) + 100000000) + String.valueOf(random.nextInt(899999999) + 100000000));
//            creditCard.setWhichBank("招商银行");
//            creditCard.setCardType(random.nextInt(10));
//            mDaoSession.insert(creditCard);
//        }
    }


    @Override
    public void clearAllLoginNames() {
        LoginBuserNameDao dao = mDaoSession.getLoginBuserNameDao();
        dao.deleteAll();
    }

    /**
     * 将asset下的城市数据库文件 移动到app的数据库文件目录下
     * static方法无法用接口实现
     *
     * @param ctx
     */
    public static boolean initCityDb(Context ctx) {
        // 初次启动，将assets 目录下的数据库复制到内存中。
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(AppConfig.CityDbName);

        File file = new File(sfile);
        if (file.exists()) {
            file.delete();
        }

        // copied and valid
        if (!file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                LogUtil.e("Create \"" + spath + "\" fail!");
                return false;
            }
            if (!FileUtil.copyAssetsToFilesystem(ctx, AppConfig.CityDbName, sfile)) {
                LogUtil.e(String.format("Copy %s to %s fail!", AppConfig.CityDbName, sfile));
                return false;
            }
        }
        LogUtil.e(String.format("Copy %s to %s SUCC!", AppConfig.CityDbName, sfile));
        return true;
    }


    private static String getDatabaseFilepath() {
        return String.format(databasepath, MyApp.getInstance().getApplicationInfo().packageName);
    }

    private static String getDatabaseFile(String dbfile) {
        return String.format(databasepath, MyApp.getInstance().getApplicationInfo().packageName) + "/" + dbfile;
    }

}
