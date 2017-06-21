package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/21.
 */

public class UserLevelBean implements Serializable {


    /**
     * code : 200
     * msg : 验证成功
     * nickname : 188****5619
     * avatar : http://180.153.57.53:8099/vrsws/img/1.png
     * level :
     * integral : 0 积分
     * flow : 0.0
     */

    private int code;
    private String msg;
    private String nickname;
    private String avatar;
    private String level;
    private String integral;
    private String flow;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
}
