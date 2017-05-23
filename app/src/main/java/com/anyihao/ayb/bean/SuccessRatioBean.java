package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/4/22.
 */

public class SuccessRatioBean implements Serializable {

    /**
     * size : 12
     * data : {"totalCount":59904,"classification":[{"count":"83","remark":"支付失败"},
     * {"count":"1802","remark":"订单加载失败(alert)"},{"count":"1","remark":"订单已支付"},{"count":"1",
     * "remark":"正在支付中"},{"count":"7779","remark":"balance_not_pay_err"},{"count":"28188",
     * "remark":"验证码接收超时"},{"count":"171","remark":"运行失败"},{"count":"153",
     * "remark":"确认短信验证码结果超时"},{"count":"137","remark":"内部错误"},{"count":"19","remark":"计费失败"},
     * {"count":"20270","remark":"成功"},{"count":"1300","remark":"验证码错误"}]}
     * resultCode : 200
     * resultMessage : 成功
     */

    private int size;
    private DataBean data;
    private int resultCode;
    private String resultMessage;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public static class DataBean {
        /**
         * totalCount : 59904
         * classification : [{"count":"83","remark":"支付失败"},{"count":"1802","remark":"订单加载失败
         * (alert)"},{"count":"1","remark":"订单已支付"},{"count":"1","remark":"正在支付中"},
         * {"count":"7779","remark":"balance_not_pay_err"},{"count":"28188","remark":"验证码接收超时"},
         * {"count":"171","remark":"运行失败"},{"count":"153","remark":"确认短信验证码结果超时"},{"count":"137",
         * "remark":"内部错误"},{"count":"19","remark":"计费失败"},{"count":"20270","remark":"成功"},
         * {"count":"1300","remark":"验证码错误"}]
         */

        private int totalCount;
        private List<ClassificationBean> classification;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<ClassificationBean> getClassification() {
            return classification;
        }

        public void setClassification(List<ClassificationBean> classification) {
            this.classification = classification;
        }

        public static class ClassificationBean {
            /**
             * count : 83
             * remark : 支付失败
             */

            private String count;
            private String remark;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }
    }
}
