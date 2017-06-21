package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/21.
 */

public class UserInfoBean implements Serializable {

    /**
     * code : 200
     * msg : 获取用户信息成功
     * avatar : http://180.153.57.53:8099/vrsws/img/1.png
     * nickname : 188****5619
     * sex : 未知
     * birthday : 未设置
     * phoneNumber : 188****5619
     * email : 未设置
     * area : 未设置
     * userType : 0
     * deposit : 未缴纳
     */

    private int code;
    private String msg;
    private String avatar;
    private String nickname;
    private String sex;
    private String birthday;
    private String phoneNumber;
    private String email;
    private String area;
    private int userType;
    private String deposit;

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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
}
