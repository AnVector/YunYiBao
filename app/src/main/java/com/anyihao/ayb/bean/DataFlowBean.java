package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:53.
 * email:looper@126.com
 */

public class DataFlowBean implements Parcelable {

    private int code;
    private String msg;
    private int totalFlow;
    private int totalUseFlow;
    private int initFlow;
    private int initUseFlow;
    private int buyFlow;
    private int buyUseFlow;
    private int taskFlow;
    private int taskUseFlow;

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

    public int getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(int totalFlow) {
        this.totalFlow = totalFlow;
    }

    public int getTotalUseFlow() {
        return totalUseFlow;
    }

    public void setTotalUseFlow(int totalUseFlow) {
        this.totalUseFlow = totalUseFlow;
    }

    public int getInitFlow() {
        return initFlow;
    }

    public void setInitFlow(int initFlow) {
        this.initFlow = initFlow;
    }

    public int getInitUseFlow() {
        return initUseFlow;
    }

    public void setInitUseFlow(int initUseFlow) {
        this.initUseFlow = initUseFlow;
    }

    public int getBuyFlow() {
        return buyFlow;
    }

    public void setBuyFlow(int buyFlow) {
        this.buyFlow = buyFlow;
    }

    public int getBuyUseFlow() {
        return buyUseFlow;
    }

    public void setBuyUseFlow(int buyUseFlow) {
        this.buyUseFlow = buyUseFlow;
    }

    public int getTaskFlow() {
        return taskFlow;
    }

    public void setTaskFlow(int taskFlow) {
        this.taskFlow = taskFlow;
    }

    public int getTaskUseFlow() {
        return taskUseFlow;
    }

    public void setTaskUseFlow(int taskUseFlow) {
        this.taskUseFlow = taskUseFlow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeInt(this.totalFlow);
        dest.writeInt(this.totalUseFlow);
        dest.writeInt(this.initFlow);
        dest.writeInt(this.initUseFlow);
        dest.writeInt(this.buyFlow);
        dest.writeInt(this.buyUseFlow);
        dest.writeInt(this.taskFlow);
        dest.writeInt(this.taskUseFlow);
    }

    public DataFlowBean() {
    }

    protected DataFlowBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.totalFlow = in.readInt();
        this.totalUseFlow = in.readInt();
        this.initFlow = in.readInt();
        this.initUseFlow = in.readInt();
        this.buyFlow = in.readInt();
        this.buyUseFlow = in.readInt();
        this.taskFlow = in.readInt();
        this.taskUseFlow = in.readInt();
    }

    public static final Creator<DataFlowBean> CREATOR = new Creator<DataFlowBean>() {
        @Override
        public DataFlowBean createFromParcel(Parcel source) {
            return new DataFlowBean(source);
        }

        @Override
        public DataFlowBean[] newArray(int size) {
            return new DataFlowBean[size];
        }
    };
}
