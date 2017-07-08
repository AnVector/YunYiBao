package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/7/8.
 */

public class RedPackageBean implements Serializable {


    /**
     * code : 200
     * msg : 查询成功
     * data : [{"message":"","sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:54:25",
     * "flow":"100M","sendAvatar":"http://122.224.91
     * .148:8888/vrsws/upload/avatar/2017-07-06/1499342938494.png","keyId":25},{"message":"",
     * "sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:54:23","flow":"100M",
     * "sendAvatar":"http://122.224.91.148:8888/vrsws/upload/avatar/2017-07-06/1499342938494
     * .png","keyId":24},{"message":"","sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:54:21",
     * "flow":"100M","sendAvatar":"http://122.224.91
     * .148:8888/vrsws/upload/avatar/2017-07-06/1499342938494.png","keyId":23},{"message":"",
     * "sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:54:19","flow":"100M",
     * "sendAvatar":"http://122.224.91.148:8888/vrsws/upload/avatar/2017-07-06/1499342938494
     * .png","keyId":22},{"message":"","sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:54:15",
     * "flow":"100M","sendAvatar":"http://122.224.91
     * .148:8888/vrsws/upload/avatar/2017-07-06/1499342938494.png","keyId":21},{"message":"",
     * "sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:54:10","flow":"200M",
     * "sendAvatar":"http://122.224.91.148:8888/vrsws/upload/avatar/2017-07-06/1499342938494
     * .png","keyId":20},{"message":"","sendName":"疯人愿","status":0,"crtTm":"2017-07-08 17:53:39",
     * "flow":"100M","sendAvatar":"http://122.224.91
     * .148:8888/vrsws/upload/avatar/2017-07-06/1499342938494.png","keyId":19}]
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
         * message :
         * sendName : 疯人愿
         * status : 0
         * crtTm : 2017-07-08 17:54:25
         * flow : 100M
         * sendAvatar : http://122.224.91.148:8888/vrsws/upload/avatar/2017-07-06/1499342938494.png
         * keyId : 25
         */

        private String message;
        private String sendName;
        private int status;
        private String crtTm;
        private String flow;
        private String sendAvatar;
        private int keyId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSendName() {
            return sendName;
        }

        public void setSendName(String sendName) {
            this.sendName = sendName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }

        public String getSendAvatar() {
            return sendAvatar;
        }

        public void setSendAvatar(String sendAvatar) {
            this.sendAvatar = sendAvatar;
        }

        public int getKeyId() {
            return keyId;
        }

        public void setKeyId(int keyId) {
            this.keyId = keyId;
        }
    }
}
