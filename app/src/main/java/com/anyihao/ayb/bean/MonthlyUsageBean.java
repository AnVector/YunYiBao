package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:58.
 * email:looper@126.com
 */

public class MonthlyUsageBean implements Serializable {

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
        private String day;

        public Double getDataTraffic() {
            return dataTraffic;
        }

        public void setDataTraffic(Double dataTraffic) {
            this.dataTraffic = dataTraffic;
        }


        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }
}
