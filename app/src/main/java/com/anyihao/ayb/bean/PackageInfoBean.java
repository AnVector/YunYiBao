package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:27.
 * email:looper@126.com
 */

public class PackageInfoBean implements Parcelable {
    private int code;
    private String msg;
    private int totalUseFlow;
    private String nickname;
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

    public int getTotalUseFlow() {
        return totalUseFlow;
    }

    public void setTotalUseFlow(int totalUseFlow) {
        this.totalUseFlow = totalUseFlow;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        private String keyPackageID;
        private String pkgType;
        private String pkgName;
        private String pkgDesc;
        private int price;

        public String getKeyPackageID() {
            return keyPackageID;
        }

        public void setKeyPackageID(String keyPackageID) {
            this.keyPackageID = keyPackageID;
        }

        public String getPkgType() {
            return pkgType;
        }

        public void setPkgType(String pkgType) {
            this.pkgType = pkgType;
        }

        public String getPkgName() {
            return pkgName;
        }

        public void setPkgName(String pkgName) {
            this.pkgName = pkgName;
        }

        public String getPkgDesc() {
            return pkgDesc;
        }

        public void setPkgDesc(String pkgDesc) {
            this.pkgDesc = pkgDesc;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
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
        dest.writeInt(this.totalUseFlow);
        dest.writeList(this.data);
    }

    public PackageInfoBean() {
    }

    protected PackageInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.totalUseFlow = in.readInt();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<PackageInfoBean> CREATOR = new Creator<PackageInfoBean>() {
        @Override
        public PackageInfoBean createFromParcel(Parcel source) {
            return new PackageInfoBean(source);
        }

        @Override
        public PackageInfoBean[] newArray(int size) {
            return new PackageInfoBean[size];
        }
    };
}
