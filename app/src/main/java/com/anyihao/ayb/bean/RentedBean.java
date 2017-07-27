package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/7/24.
 */

public class RentedBean implements Serializable {

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

    public class DataBean implements Serializable{
        private String printId;
        private String keyId;
        private String nickname;
        private String outTm;

        public String getPrintId() {
            return printId;
        }

        public void setPrintId(String printId) {
            this.printId = printId;
        }

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOutTm() {
            return outTm;
        }

        public void setOutTm(String outTm) {
            this.outTm = outTm;
        }
    }
}
