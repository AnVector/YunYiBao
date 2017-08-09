package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:33.
 * email:looper@126.com
 */

public class AliOrderBean implements Serializable {

    private int code;
    private String msg;
    private String orderInfo;

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

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
