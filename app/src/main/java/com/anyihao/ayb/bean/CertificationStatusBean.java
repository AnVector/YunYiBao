package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/26.
 */

public class CertificationStatusBean implements Serializable {

    private int code;
    private String msg;
    private int infoStatus;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(int infoStatus) {
        this.infoStatus = infoStatus;
    }
}
