package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001811:49.
 * email:looper@126.com
 */

public class TaskInfoListBean implements Serializable {


    /**
     * code : 200
     * msg : 查询成功
     * data : {"limited":[{"image":"SSS","exContent":"可兑换5M流量","exchangeId":1,
     * "integral":"兑换所需积分：5分"},{"image":"SSS","exContent":"可兑换5M流量","exchangeId":2,
     * "integral":"兑换所需积分：5分"},{"image":"SSS","exContent":"可兑换5M流量","exchangeId":5,
     * "integral":"兑换所需积分：5分"}],"point":0,"normal":[{"exchangeTimes":"剩余1000次可兑换","image":"SSS",
     * "exContent":"可兑换5M流量","exchangeId":4,"integral":"兑换所需积分：5分"},
     * {"exchangeTimes":"剩余1000次可兑换","image":"SSS","exContent":"可兑换5M流量","exchangeId":7,
     * "integral":"兑换所需积分：5分"}],"signStatus":0,"day":0,"avatar":"https://tva3.sinaimg.cn/crop.13
     * .0.723.723.180/ea59e25fjw8fblj0xdpfuj20ku0k3abn.jpg",
     * "lend":[{"exchangeTimes":"剩余1000次可兑换","image":"SSS","exContent":"可兑换5M流量","exchangeId":3,
     * "integral":"兑换所需积分：5分"},{"exchangeTimes":"剩余1000次可兑换","image":"SSS","exContent":"可兑换5M流量",
     * "exchangeId":6,"integral":"兑换所需积分：5分"}]}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * limited : [{"image":"SSS","exContent":"可兑换5M流量","exchangeId":1,
         * "integral":"兑换所需积分：5分"},{"image":"SSS","exContent":"可兑换5M流量","exchangeId":2,
         * "integral":"兑换所需积分：5分"},{"image":"SSS","exContent":"可兑换5M流量","exchangeId":5,
         * "integral":"兑换所需积分：5分"}]
         * point : 0
         * normal : [{"exchangeTimes":"剩余1000次可兑换","image":"SSS","exContent":"可兑换5M流量",
         * "exchangeId":4,"integral":"兑换所需积分：5分"},{"exchangeTimes":"剩余1000次可兑换","image":"SSS",
         * "exContent":"可兑换5M流量","exchangeId":7,"integral":"兑换所需积分：5分"}]
         * signStatus : 0
         * day : 0
         * avatar : https://tva3.sinaimg.cn/crop.13.0.723.723
         * .180/ea59e25fjw8fblj0xdpfuj20ku0k3abn.jpg
         * lend : [{"exchangeTimes":"剩余1000次可兑换","image":"SSS","exContent":"可兑换5M流量",
         * "exchangeId":3,"integral":"兑换所需积分：5分"},{"exchangeTimes":"剩余1000次可兑换","image":"SSS",
         * "exContent":"可兑换5M流量","exchangeId":6,"integral":"兑换所需积分：5分"}]
         */

        private int point;
        private int signStatus;
        private int day;
        private String avatar;
        private List<LimitedBean> limited;
        private List<NormalBean> normal;
        private List<LendBean> lend;

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public int getSignStatus() {
            return signStatus;
        }

        public void setSignStatus(int signStatus) {
            this.signStatus = signStatus;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public List<LimitedBean> getLimited() {
            return limited;
        }

        public void setLimited(List<LimitedBean> limited) {
            this.limited = limited;
        }

        public List<NormalBean> getNormal() {
            return normal;
        }

        public void setNormal(List<NormalBean> normal) {
            this.normal = normal;
        }

        public List<LendBean> getLend() {
            return lend;
        }

        public void setLend(List<LendBean> lend) {
            this.lend = lend;
        }

        public static class LimitedBean implements Serializable {
            /**
             * image : SSS
             * exContent : 可兑换5M流量
             * exchangeId : 1
             * integral : 兑换所需积分：5分
             */

            private String image;
            private String exContent;
            private int exchangeId;
            private String integral;
            private String position;


            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }


            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getExContent() {
                return exContent;
            }

            public void setExContent(String exContent) {
                this.exContent = exContent;
            }

            public int getExchangeId() {
                return exchangeId;
            }

            public void setExchangeId(int exchangeId) {
                this.exchangeId = exchangeId;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }
        }

        public static class NormalBean implements Serializable {
            /**
             * exchangeTimes : 剩余1000次可兑换
             * image : SSS
             * exContent : 可兑换5M流量
             * exchangeId : 4
             * integral : 兑换所需积分：5分
             */

            private String exchangeTimes;
            private String image;
            private String exContent;
            private int exchangeId;
            private String integral;

            public String getExchangeTimes() {
                return exchangeTimes;
            }

            public void setExchangeTimes(String exchangeTimes) {
                this.exchangeTimes = exchangeTimes;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getExContent() {
                return exContent;
            }

            public void setExContent(String exContent) {
                this.exContent = exContent;
            }

            public int getExchangeId() {
                return exchangeId;
            }

            public void setExchangeId(int exchangeId) {
                this.exchangeId = exchangeId;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }
        }

        public static class LendBean implements Serializable {
            /**
             * exchangeTimes : 剩余1000次可兑换
             * image : SSS
             * exContent : 可兑换5M流量
             * exchangeId : 3
             * integral : 兑换所需积分：5分
             */

            private String exchangeTimes;
            private String image;
            private String exContent;
            private int exchangeId;
            private String integral;

            public String getExchangeTimes() {
                return exchangeTimes;
            }

            public void setExchangeTimes(String exchangeTimes) {
                this.exchangeTimes = exchangeTimes;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getExContent() {
                return exContent;
            }

            public void setExContent(String exContent) {
                this.exContent = exContent;
            }

            public int getExchangeId() {
                return exchangeId;
            }

            public void setExchangeId(int exchangeId) {
                this.exchangeId = exchangeId;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }
        }
    }
}
