package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001812:11.
 * email:looper@126.com
 */

public class ShareBean implements Parcelable {

    private int code;
    private String msg;
    private String shareTitle;
    private String shareUrl;
    private String shareImg;
    private String shareId;

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

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.shareTitle);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareImg);
        dest.writeString(this.shareId);
    }

    public ShareBean() {
    }

    protected ShareBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.shareTitle = in.readString();
        this.shareUrl = in.readString();
        this.shareImg = in.readString();
        this.shareId = in.readString();
    }

    public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
