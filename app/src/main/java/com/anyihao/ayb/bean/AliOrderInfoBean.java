package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:33.
 * email:looper@126.com
 */

public class AliOrderInfoBean implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.orderInfo);
    }

    public AliOrderInfoBean() {
    }

    protected AliOrderInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.orderInfo = in.readString();
    }

    public static final Creator<AliOrderInfoBean> CREATOR = new Creator<AliOrderInfoBean>() {
        @Override
        public AliOrderInfoBean createFromParcel(Parcel source) {
            return new AliOrderInfoBean(source);
        }

        @Override
        public AliOrderInfoBean[] newArray(int size) {
            return new AliOrderInfoBean[size];
        }
    };
}
