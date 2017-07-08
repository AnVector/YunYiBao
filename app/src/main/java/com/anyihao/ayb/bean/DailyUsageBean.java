package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:56.
 * email:looper@126.com
 */

public class DailyUsageBean implements Serializable {

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

        private Double dataTraffic;
        private String time;

        public Double getDataTraffic() {
            return dataTraffic;
        }

        public void setDataTraffic(Double dataTraffic) {
            this.dataTraffic = dataTraffic;
        }


        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
