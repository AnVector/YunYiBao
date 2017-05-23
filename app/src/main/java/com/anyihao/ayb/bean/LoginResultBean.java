package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * author: Administrator
 * date: 2017/3/9 000915:56.
 * email:looper@126.com
 */

public class LoginResultBean implements Serializable {

    /**
     * code : 1
     * reason : 200
     */

    private String code;
    private String reason;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
