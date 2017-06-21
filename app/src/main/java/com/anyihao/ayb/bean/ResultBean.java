package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/20.
 */

public class ResultBean implements Serializable {


    /**
     * code : 446
     * msg : 手机号未填写，请填写
     */
    private int code;
    private String msg;

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
