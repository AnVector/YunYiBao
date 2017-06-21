package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/20.
 */

public class RegisterBean implements Serializable {

    /**
     * code : 500
     * msg : 注册失败
     * uid :
     * reqCode :
     */

    private int code;
    private String msg;
    private String uid;
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

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }
}
