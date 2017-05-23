package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/9 000915:53.
 * email:looper@126.com
 */

public class ServerInfoList implements Serializable {

    private String count;
    private String code;
    private String reason;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class DataBean {
        /**
         * crtTm : 2017/03/09 11:03:51
         * id : 5
         * serverId : 68
         * taskLength : 400
         */

        private String crtTm;
        private String id;
        private String serverId;
        private int taskLength;

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public int getTaskLength() {
            return taskLength;
        }

        public void setTaskLength(int taskLength) {
            this.taskLength = taskLength;
        }
    }
}
