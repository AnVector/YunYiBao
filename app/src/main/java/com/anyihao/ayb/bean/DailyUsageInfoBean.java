package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:56.
 * email:looper@126.com
 */

public class DailyUsageInfoBean implements Parcelable {

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
        private int dataTraffic;
        private String time;

        public int getDataTraffic() {
            return dataTraffic;
        }

        public void setDataTraffic(int dataTraffic) {
            this.dataTraffic = dataTraffic;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

    public DailyUsageInfoBean() {
    }

    protected DailyUsageInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<DailyUsageInfoBean> CREATOR = new Creator<DailyUsageInfoBean>() {
        @Override
        public DailyUsageInfoBean createFromParcel(Parcel source) {
            return new DailyUsageInfoBean(source);
        }

        @Override
        public DailyUsageInfoBean[] newArray(int size) {
            return new DailyUsageInfoBean[size];
        }
    };
}
