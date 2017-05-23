package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: Administrator
 * date: 2017/3/18 001811:12.
 * email:looper@126.com
 */

public class PersonalInfoBean implements Parcelable {

    private int code;
    private String msg;
    private String avatar;
    private String userName;
    private String nickname;
    private String sex;
    private String birthday;
    private String phoneNumber;
    private String email;
    private String area;
    private String userType;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.avatar);
        dest.writeString(this.userName);
        dest.writeString(this.nickname);
        dest.writeString(this.sex);
        dest.writeString(this.birthday);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.area);
        dest.writeString(this.userType);
    }

    public PersonalInfoBean() {
    }

    protected PersonalInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.avatar = in.readString();
        this.userName = in.readString();
        this.nickname = in.readString();
        this.sex = in.readString();
        this.birthday = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.area = in.readString();
        this.userType = in.readString();
    }

    public static final Creator<PersonalInfoBean> CREATOR = new Creator<PersonalInfoBean>() {
        @Override
        public PersonalInfoBean createFromParcel(Parcel source) {
            return new PersonalInfoBean(source);
        }

        @Override
        public PersonalInfoBean[] newArray(int size) {
            return new PersonalInfoBean[size];
        }
    };
}
