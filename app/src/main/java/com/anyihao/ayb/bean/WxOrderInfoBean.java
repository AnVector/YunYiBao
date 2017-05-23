package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:38.
 * email:looper@126.com
 */

public class WxOrderInfoBean implements Parcelable {

    private int code;
    private String msg;
    private String appId;
    private String partnerId;
    private String prepayId;
    private String orderId;
    private String packege;
    private String nonceStr;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackege() {
        return packege;
    }

    public void setPackege(String packege) {
        this.packege = packege;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.appId);
        dest.writeString(this.partnerId);
        dest.writeString(this.prepayId);
        dest.writeString(this.orderId);
        dest.writeString(this.packege);
        dest.writeString(this.nonceStr);
        dest.writeString(this.timestamp);
        dest.writeString(this.sign);
    }

    public WxOrderInfoBean() {
    }

    protected WxOrderInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.appId = in.readString();
        this.partnerId = in.readString();
        this.prepayId = in.readString();
        this.orderId = in.readString();
        this.packege = in.readString();
        this.nonceStr = in.readString();
        this.timestamp = in.readString();
        this.sign = in.readString();
    }

    public static final Creator<WxOrderInfoBean> CREATOR = new Creator<WxOrderInfoBean>() {
        @Override
        public WxOrderInfoBean createFromParcel(Parcel source) {
            return new WxOrderInfoBean(source);
        }

        @Override
        public WxOrderInfoBean[] newArray(int size) {
            return new WxOrderInfoBean[size];
        }
    };
}
