package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:49.
 * email:looper@126.com
 */

public class TaskInfoListBean implements Parcelable {

    private int code;
    private String reason;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        private String taskId;
        private String taskName;
        private String image;
        private String taskDesc;
        private String taskLink;
        private String identifier;
        private String crtTm;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTaskDesc() {
            return taskDesc;
        }

        public void setTaskDesc(String taskDesc) {
            this.taskDesc = taskDesc;
        }

        public String getTaskLink() {
            return taskLink;
        }

        public void setTaskLink(String taskLink) {
            this.taskLink = taskLink;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.reason);
        dest.writeString(this.msg);
        dest.writeList(this.data);
    }

    public TaskInfoListBean() {
    }

    protected TaskInfoListBean(Parcel in) {
        this.code = in.readInt();
        this.reason = in.readString();
        this.msg = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<TaskInfoListBean> CREATOR = new Creator<TaskInfoListBean>() {
        @Override
        public TaskInfoListBean createFromParcel(Parcel source) {
            return new TaskInfoListBean(source);
        }

        @Override
        public TaskInfoListBean[] newArray(int size) {
            return new TaskInfoListBean[size];
        }
    };
}
