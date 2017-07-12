package com.anyihao.ayb.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:38.
 * email:looper@126.com
 */

public class WxOrderInfoBean implements Serializable {


    /**
     * code : 200
     * msg : 订单生成成功
     * appid : wxc6a6d430c1f2c793
     * partnerid : 1400678202
     * prepayid : wx2017071219285926eea41f5c0120228043
     * package : Sign=WXPay
     * noncestr : J1DOHN06P3MCHC1DAP2QNYLWKQJCN9O4
     * timestamp : 1499859134
     * sign : C92885A62AFD1B34B039916D8FC05A52
     */

    private int code;
    private String msg;
    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;

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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
