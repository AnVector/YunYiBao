package com.anyihao.ayb.common;

import android.content.Context;

import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.db.gen.DaoMaster;
import com.anyihao.ayb.db.gen.DaoSession;


/**
 * author: Administrator
 * date: 2017/2/16 001610:39.
 * email:looper@126.com
 */

public class GreenDaoManager {

    private static DaoMaster mDaoMaster = null;
    private static DaoSession mDaoSession = null;

    private GreenDaoManager() {
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final GreenDaoManager INSTANCE = new GreenDaoManager();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static GreenDaoManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    /**
     * 初始化数据
     */
    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context
                .getApplicationContext(),
                GlobalConsts.DB_NAME);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getDaoMaster(Context context) {
        if (null == mDaoMaster) {
            synchronized (GreenDaoManager.class) {
                init(context.getApplicationContext());
            }
        }
        return mDaoMaster;
    }

    public DaoSession getDaoSession(Context context) {
        if (null == mDaoSession) {
            synchronized (GreenDaoManager.class) {
                init(context.getApplicationContext());
            }
        }
        return mDaoSession;
    }

    public DaoSession getNewSession(Context context) {
        if (null == mDaoMaster) {
            synchronized (GreenDaoManager.class) {
                init(context);
            }
        }
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
