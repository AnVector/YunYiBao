package com.anyihao.ayb.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: Administrator
 * date: 2017/2/24 002409:58.
 * email:looper@126.com
 */
@Entity
public class Server {
    /**
     * code : 0
     * ids : 1
     * serverid : 38
     * taskLength : 1000
     * crtTm : 2016-10-10 19:00:01
     */

    @Transient//不存入数据库
    private int code;//返回码
    @Id(autoincrement = true)//自增
    private  Long id;//主键ID
    @Index(unique = true)
    private String ids;//序号
    @Generated //该属性不能被修改
    private String serverid;
    @Generated //该属性不能被修改
    private String taskLength;
    @Generated //该属性不能被修改
    private String crtTm;

    @Generated(hash = 1151933134)
    public Server() {
    }

    @Generated(hash = 1740609307)
    public Server(Long id, String ids, String serverid, String taskLength,
            String crtTm) {
        this.id = id;
        this.ids = ids;
        this.serverid = serverid;
        this.taskLength = taskLength;
        this.crtTm = crtTm;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getTaskLength() {
        return taskLength;
    }

    public void setTaskLength(String taskLength) {
        this.taskLength = taskLength;
    }

    public String getCrtTm() {
        return crtTm;
    }

    public void setCrtTm(String crtTm) {
        this.crtTm = crtTm;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
