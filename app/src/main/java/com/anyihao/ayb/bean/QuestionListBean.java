package com.anyihao.ayb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001812:03.
 * email:looper@126.com
 */

public class QuestionListBean implements Serializable {


    /**
     * code : 200
     * msg : 查询成功
     * data : [{"answer":"可以的！","crtTm":"2016-10-18 09:51:03","question":"wifi可以多人使用吗？"},
     * {"answer":"充值方式有支付宝，微信，网银等。","crtTm":"2016-10-18 09:51:04","question":"充值支持哪几种方式？"},
     * {"answer":"在登录页面，通过登录方式可以注册！","crtTm":"2017-03-26 17:14:53","question":"怎么注册呢？？？？？"},
     * {"answer":"没解答","crtTm":"2017-03-28 10:52:14","question":"没问题"}]
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
         * answer : 可以的！
         * crtTm : 2016-10-18 09:51:03
         * question : wifi可以多人使用吗？
         */

        private String answer;
        private String crtTm;
        private String question;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getCrtTm() {
            return crtTm;
        }

        public void setCrtTm(String crtTm) {
            this.crtTm = crtTm;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }
}
