package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/22.
 */

public class PackageListBean implements Serializable {


    /**
     * code : 200
     * msg : 查询成功
     * totalUseFlow : 0M
     * nickname : 188****5619
     * data : [{"pkgDesc":"全国流量，即时生效，30天有效","price":"8元","packageID":"006","flow":"1G",
     * "pkgType":"MONTH","activity":0},{"pkgDesc":"全国流量，即时生效，30天有效","price":"12元",
     * "packageID":"007","flow":"2G","pkgType":"MONTH","activity":0},
     * {"pkgDesc":"全国流量，即时生效，30天有效","price":"16元","packageID":"008","flow":"3G",
     * "pkgType":"MONTH","activity":0},{"pkgDesc":"全国流量，即时生效，30天有效","price":"25元",
     * "packageID":"009","flow":"5G","pkgType":"MONTH","activity":0}]
     */

    private int code;
    private String msg;
    private String totalUseFlow;
    private String nickname;
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

    public String getTotalUseFlow() {
        return totalUseFlow;
    }

    public void setTotalUseFlow(String totalUseFlow) {
        this.totalUseFlow = totalUseFlow;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * pkgDesc : 全国流量，即时生效，30天有效
         * price : 8元
         * packageID : 006
         * flow : 1G
         * pkgType : MONTH
         * activity : 0
         */

        private String pkgDesc;
        private String price;
        private String packageID;
        private String flow;
        private String pkgType;
        private int activity;

        public String getPkgDesc() {
            return pkgDesc;
        }

        public void setPkgDesc(String pkgDesc) {
            this.pkgDesc = pkgDesc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPackageID() {
            return packageID;
        }

        public void setPackageID(String packageID) {
            this.packageID = packageID;
        }

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }

        public String getPkgType() {
            return pkgType;
        }

        public void setPkgType(String pkgType) {
            this.pkgType = pkgType;
        }

        public int getActivity() {
            return activity;
        }

        public void setActivity(int activity) {
            this.activity = activity;
        }
    }
}
