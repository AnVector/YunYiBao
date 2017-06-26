package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/26.
 */

public class MerchantListBean implements Serializable {


    /**
     * code : 200
     * msg : 列表获取成功
     * data : [{"shopAddr":"杭州","shopName":"寒寒","longitude":120.20953296,"merchantId":1,
     * "latitude":30.1880666,"contact":"15659105066"},{"shopAddr":"hang","shopName":"不是你的店",
     * "longitude":120.20673396,"merchantId":2,"latitude":30.1883666,"contact":"13509565656"},
     * {"shopAddr":"bei","shopName":"那是谁的店","longitude":120.20793396,"merchantId":3,"latitude":30
     * .1886666,"contact":"110"},{"shopAddr":"ge","shopName":"施航航的店","longitude":120.20683396,
     * "merchantId":4,"latitude":30.1889666,"contact":"119"},{"shopAddr":"便利店","shopName":"航~",
     * "longitude":120.20793396,"merchantId":5,"latitude":30.1890666,"contact":"1151541544"},
     * {"shopAddr":"门口","shopName":"思思","longitude":120.20983396,"merchantId":6,"latitude":30
     * .1894666,"contact":"8888888888"}]
     */

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

    public static class DataBean implements Serializable {
        /**
         * shopAddr : 杭州
         * shopName : 寒寒
         * longitude : 120.20953296
         * merchantId : 1
         * latitude : 30.1880666
         * contact : 15659105066
         */

        private String shopAddr;
        private String shopName;
        private double longitude;
        private int merchantId;
        private double latitude;
        private String contact;

        public String getShopAddr() {
            return shopAddr;
        }

        public void setShopAddr(String shopAddr) {
            this.shopAddr = shopAddr;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
    }
}
