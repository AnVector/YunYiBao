package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/7/24.
 */

public class RentDetailsBean implements Serializable {

    private int code;
    private String msg;
    private String vid;
    private String lendNickname;
    private String lendPhoneNumber;
    private String lendTm;
    private String status;

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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getLendNickname() {
        return lendNickname;
    }

    public void setLendNickname(String lendNickname) {
        this.lendNickname = lendNickname;
    }

    public String getLendPhoneNumber() {
        return lendPhoneNumber;
    }

    public void setLendPhoneNumber(String lendPhoneNumber) {
        this.lendPhoneNumber = lendPhoneNumber;
    }

    public String getLendTm() {
        return lendTm;
    }

    public void setLendTm(String lendTm) {
        this.lendTm = lendTm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
