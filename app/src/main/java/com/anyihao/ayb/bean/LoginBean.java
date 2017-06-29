package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * author: Administrator
 * date: 2017/3/9 000915:56.
 * email:looper@126.com
 */

public class LoginBean implements Serializable {
    /**
     * code : 425
     * msg : 账号和密码不匹配，请重新输入
     * uid :
     * bindStatus :
     * userType :
     * avatar :
     * reqCode :
     */

    private int code;
    private String msg;
    private String uid;
    private int bindStatus;
    private String userType;
    private String avatar;
    private String reqCode;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

}
