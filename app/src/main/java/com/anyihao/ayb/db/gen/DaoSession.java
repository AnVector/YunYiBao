package com.anyihao.ayb.db.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.anyihao.ayb.db.entity.Server;

import com.anyihao.ayb.db.gen.ServerDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig serverDaoConfig;

    private final ServerDao serverDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        serverDaoConfig = daoConfigMap.get(ServerDao.class).clone();
        serverDaoConfig.initIdentityScope(type);

        serverDao = new ServerDao(serverDaoConfig, this);

        registerDao(Server.class, serverDao);
    }
    
    public void clear() {
        serverDaoConfig.clearIdentityScope();
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

}
