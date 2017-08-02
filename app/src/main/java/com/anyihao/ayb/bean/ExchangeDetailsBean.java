package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/8/1.
 */

public class ExchangeDetailsBean implements Serializable {

    private int code;
    private String msg;
    private String upImage;
    private String activeTm;
    private String introduction;
    private String method;

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

    public String getUpImage() {
        return upImage;
    }

    public void setUpImage(String upImage) {
        this.upImage = upImage;
    }

    public String getActiveTm() {
        return activeTm;
    }

    public void setActiveTm(String activeTm) {
        this.activeTm = activeTm;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
