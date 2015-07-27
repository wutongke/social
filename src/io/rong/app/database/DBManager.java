package io.rong.app.database;

import android.content.Context;

public class DBManager {

    private static DBManager instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }
        }
        return instance;
    }

    private DBManager(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getPackageName(), null);
                daoMaster = new DaoMaster(helper.getWritableDatabase());
            }
            daoSession = daoMaster.newSession();
        }
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}
