package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/26.
 */

public class RentHistoryListBean implements Serializable {

    private int code;
    private String msg;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean implements Serializable {
        private int keyId;
        private String vid;
        private String startTm;
        private String endTm;

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public int getKeyId() {
            return keyId;
        }

        public void setKeyId(int keyId) {
            this.keyId = keyId;
        }

        public String getStartTm() {
            return startTm;
        }

        public void setStartTm(String startTm) {
            this.startTm = startTm;
        }

        public String getEndTm() {
            return endTm;
        }

        public void setEndTm(String endTm) {
            this.endTm = endTm;
        }
    }
}
