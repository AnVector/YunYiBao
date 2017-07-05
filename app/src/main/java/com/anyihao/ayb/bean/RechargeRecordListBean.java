package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/26.
 */

public class RechargeRecordListBean implements Serializable {
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

    public class DataBean implements Serializable {
        private String idxOrderID;
        private int amount;
        private String topupType;
        private String pkgType;
        private String pkgInfo;
        private String flow;
        private String crtTm;
        private String status;

        public String getIdxOrderID() {
            return idxOrderID;
        }

        public void setIdxOrderID(String idxOrderID) {
            this.idxOrderID = idxOrderID;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getTopupType() {
            return topupType;
        }

        public void setTopupType(String topupType) {
            this.topupType = topupType;
        }

        public String getPkgType() {
            return pkgType;
        }

        public void setPkgType(String pkgType) {
            this.pkgType = pkgType;
        }

        public String getPkgInfo() {
            return pkgInfo;
        }

        public void setPkgInfo(String pkgInfo) {
            this.pkgInfo = pkgInfo;
        }

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }
    }
}
