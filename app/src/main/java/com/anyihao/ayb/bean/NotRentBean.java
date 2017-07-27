package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/7/24.
 */

public class NotRentBean implements Serializable {

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

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getPrintId() {
            return printId;
        }

        public void setPrintId(String printId) {
            this.printId = printId;
        }
    }

}
