package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/7/7.
 */

public class SignBean implements Serializable {

    private int code;
    private String msg;
    private int day;
    private int point;

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
