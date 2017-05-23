package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:45.
 * email:looper@126.com
 */

public class RechargeDetailsBean implements Parcelable {

    private int code;
    private String msg;
    private String idxOrderID;
    private int flow;
    private int pkgType;
    private String topupType;
    private String status;
    private int amount;
    private String crtTm;
    private String effectTm;

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

    public String getIdxOrderID() {
        return idxOrderID;
    }

    public void setIdxOrderID(String idxOrderID) {
        this.idxOrderID = idxOrderID;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getPkgType() {
        return pkgType;
    }

    public void setPkgType(int pkgType) {
        this.pkgType = pkgType;
    }

    public String getTopupType() {
        return topupType;
    }

    public void setTopupType(String topupType) {
        this.topupType = topupType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCrtTm() {
        return crtTm;
    }

    public void setCrtTm(String crtTm) {
        this.crtTm = crtTm;
    }

    public String getEffectTm() {
        return effectTm;
    }

    public void setEffectTm(String effectTm) {
        this.effectTm = effectTm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.idxOrderID);
        dest.writeInt(this.flow);
        dest.writeInt(this.pkgType);
        dest.writeString(this.topupType);
        dest.writeString(this.status);
        dest.writeInt(this.amount);
        dest.writeString(this.crtTm);
        dest.writeString(this.effectTm);
    }

    public RechargeDetailsBean() {
    }

    protected RechargeDetailsBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.idxOrderID = in.readString();
        this.flow = in.readInt();
        this.pkgType = in.readInt();
        this.topupType = in.readString();
        this.status = in.readString();
        this.amount = in.readInt();
        this.crtTm = in.readString();
        this.effectTm = in.readString();
    }

    public static final Creator<RechargeDetailsBean> CREATOR = new Creator<RechargeDetailsBean>() {
        @Override
        public RechargeDetailsBean createFromParcel(Parcel source) {
            return new RechargeDetailsBean(source);
        }

        @Override
        public RechargeDetailsBean[] newArray(int size) {
            return new RechargeDetailsBean[size];
        }
    };
}
