package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001812:09.
 * email:looper@126.com
 */

public class ShareTypeBean implements Parcelable {

    private int code;
    private String msg;
    private String imgUrl;
    private String content;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.imgUrl);
        dest.writeString(this.content);
    }

    public ShareTypeBean() {
    }

    protected ShareTypeBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.imgUrl = in.readString();
        this.content = in.readString();
    }

    public static final Creator<ShareTypeBean> CREATOR = new Creator<ShareTypeBean>() {
        @Override
        public ShareTypeBean createFromParcel(Parcel source) {
            return new ShareTypeBean(source);
        }

        @Override
        public ShareTypeBean[] newArray(int size) {
            return new ShareTypeBean[size];
        }
    };
}
