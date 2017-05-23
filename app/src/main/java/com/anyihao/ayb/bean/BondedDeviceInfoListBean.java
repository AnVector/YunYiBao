package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:19.
 * email:looper@126.com
 */

public class BondedDeviceInfoListBean implements Parcelable {

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
        private String vid;
        private String alaisName;
        private String devStatus;
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

    public BondedDeviceInfoListBean() {
    }

    protected BondedDeviceInfoListBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<BondedDeviceInfoListBean> CREATOR = new Creator<BondedDeviceInfoListBean>() {
        @Override
        public BondedDeviceInfoListBean createFromParcel(Parcel source) {
            return new BondedDeviceInfoListBean(source);
        }

        @Override
        public BondedDeviceInfoListBean[] newArray(int size) {
            return new BondedDeviceInfoListBean[size];
        }
    };
}
