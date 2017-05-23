package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:41.
 * email:looper@126.com
 */

public class RechargeRecordBean implements Parcelable {

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        private String idxOrderID;
        private int amount;
        private String topupType;
        private String pkgType;
        private int flow;
        private String crtTm;
        private String status;

        public String getIdxOrderID() {
            return idxOrderID;
        }

        public void setIdxOrderID(String idxOrderID) {
            this.idxOrderID = idxOrderID;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getTopupType() {
            return topupType;
        }

        public void setTopupType(String topupType) {
            this.topupType = topupType;
        }

        public String getPkgType() {
            return pkgType;
        }

        public void setPkgType(String pkgType) {
            this.pkgType = pkgType;
        }

        public int getFlow() {
            return flow;
        }

        public void setFlow(int flow) {
            this.flow = flow;
        }

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeList(this.data);
    }

    public RechargeRecordBean() {
    }

    protected RechargeRecordBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<RechargeRecordBean> CREATOR = new Creator<RechargeRecordBean>() {
        @Override
        public RechargeRecordBean createFromParcel(Parcel source) {
            return new RechargeRecordBean(source);
        }

        @Override
        public RechargeRecordBean[] newArray(int size) {
            return new RechargeRecordBean[size];
        }
    };
}
