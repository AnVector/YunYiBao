package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/26.
 */

public class RentDeviceBean implements Serializable {


    /**
     * code : 459
     * msg : 您未租借设备
     * vid :
     * vidVer :
     * vidStatus :
     * leaseTime :
     * shopName :
     * shopAddr :
     * contact :
     */

    private int code;
    private String msg;
    private String vid;
    private String vidVer;
    private String vidStatus;
    private String leaseTime;
    private String shopName;
    private String shopAddr;
    private String contact;

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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVidVer() {
        return vidVer;
    }

    public void setVidVer(String vidVer) {
        this.vidVer = vidVer;
    }

    public String getVidStatus() {
        return vidStatus;
    }

    public void setVidStatus(String vidStatus) {
        this.vidStatus = vidStatus;
    }

    public String getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(String leaseTime) {
        this.leaseTime = leaseTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
