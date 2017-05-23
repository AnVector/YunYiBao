package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001810:58.
 * email:looper@126.com
 */

public class RegisterResultBean implements Parcelable {


    private int code;
    private String msg;
    private String reqCode;
    private String uid;

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

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.reqCode);
        dest.writeString(this.uid);
    }

    public RegisterResultBean() {
    }

    protected RegisterResultBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.reqCode = in.readString();
        this.uid = in.readString();
    }

    public static final Creator<RegisterResultBean> CREATOR = new Creator<RegisterResultBean>() {
        @Override
        public RegisterResultBean createFromParcel(Parcel source) {
            return new RegisterResultBean(source);
        }

        @Override
        public RegisterResultBean[] newArray(int size) {
            return new RegisterResultBean[size];
        }
    };
}
