package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * author: Administrator
 * date: 2017/3/18 001812:06.
 * email:looper@126.com
 */

public class VersionInfoBean implements Serializable {

    private int code;
    private String msg;
    private String var;
    private String varContent;
    private String telephone;
    private String verLink;

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

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVarContent() {
        return varContent;
    }

    public void setVarContent(String varContent) {
        this.varContent = varContent;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVerLink() {
        return verLink;
    }

    public void setVerLink(String verLink) {
        this.verLink = verLink;
    }
}
