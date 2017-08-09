package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/21.
 */

public class AccountListBean implements Serializable {


    /**
     * code : 200
     * msg : 获取成功
     * data : [{"status":0,"type":"QQ"},{"status":0,"type":"WX"},{"status":0,"type":"WB"}]
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
         * status : 0
         * type : QQ
         */

        private int status;
        private String type;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
