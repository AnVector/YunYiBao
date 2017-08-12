package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/7/27.
 */

public class WifiInfoBean implements Serializable {

    private String ssid;
    private boolean connected;
    private int level;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
