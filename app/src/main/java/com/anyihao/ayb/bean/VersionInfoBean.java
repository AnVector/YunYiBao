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
    private String version;
    private String verUpCon;
    private String downloadLink;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVerUpCon() {
        return verUpCon;
    }

    public void setVerUpCon(String verUpCon) {
        this.verUpCon = verUpCon;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

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
}
