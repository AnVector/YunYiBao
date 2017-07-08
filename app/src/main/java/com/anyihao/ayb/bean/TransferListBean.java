package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/7/6.
 */

public class TransferListBean implements Serializable {

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
        private String transferId;
        private String avatar;
        private String nickname;
        private String flow;
        private String transferType;
        private String crtTm;

        public String getTransferId() {
            return transferId;
        }

        public void setTransferId(String transferId) {
            this.transferId = transferId;
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

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }


    }
}
