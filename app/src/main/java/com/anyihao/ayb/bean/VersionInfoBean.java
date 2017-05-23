package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001812:06.
 * email:looper@126.com
 */

public class VersionInfoBean implements Parcelable {

    private int code;
    private String msg;
    private String var;
    private String varContent;
    private String telephone;
    private String verLink;

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

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVarContent() {
        return varContent;
    }

    public void setVarContent(String varContent) {
        this.varContent = varContent;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVerLink() {
        return verLink;
    }

    public void setVerLink(String verLink) {
        this.verLink = verLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.var);
        dest.writeString(this.varContent);
        dest.writeString(this.telephone);
        dest.writeString(this.verLink);
    }

    public VersionInfoBean() {
    }

    protected VersionInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.var = in.readString();
        this.varContent = in.readString();
        this.telephone = in.readString();
        this.verLink = in.readString();
    }

    public static final Creator<VersionInfoBean> CREATOR = new Creator<VersionInfoBean>() {
        @Override
        public VersionInfoBean createFromParcel(Parcel source) {
            return new VersionInfoBean(source);
        }

        @Override
        public VersionInfoBean[] newArray(int size) {
            return new VersionInfoBean[size];
        }
    };
}
