package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/23.
 */

public class FlowAccountBean implements Serializable {


    /**
     * code : 200
     * msg : 查询成功
     * totalFlow : 0
     * totalUseFlow : 0
     * initFlow : 0
     * initUseFlow : 0
     * buyFlow : 0
     * buyUseFlow : 0
     * taskFlow : 0
     * taskUseFlow : 0
     */

    private int code;
    private String msg;
    private int totalFlow;
    private int totalUseFlow;
    private int initFlow;
    private int initUseFlow;
    private int buyFlow;
    private int buyUseFlow;
    private int taskFlow;
    private int taskUseFlow;

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

    public int getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(int totalFlow) {
        this.totalFlow = totalFlow;
    }

    public int getTotalUseFlow() {
        return totalUseFlow;
    }

    public void setTotalUseFlow(int totalUseFlow) {
        this.totalUseFlow = totalUseFlow;
    }

    public int getInitFlow() {
        return initFlow;
    }

    public void setInitFlow(int initFlow) {
        this.initFlow = initFlow;
    }

    public int getInitUseFlow() {
        return initUseFlow;
    }

    public void setInitUseFlow(int initUseFlow) {
        this.initUseFlow = initUseFlow;
    }

    public int getBuyFlow() {
        return buyFlow;
    }

    public void setBuyFlow(int buyFlow) {
        this.buyFlow = buyFlow;
    }

    public int getBuyUseFlow() {
        return buyUseFlow;
    }

    public void setBuyUseFlow(int buyUseFlow) {
        this.buyUseFlow = buyUseFlow;
    }

    public int getTaskFlow() {
        return taskFlow;
    }

    public void setTaskFlow(int taskFlow) {
        this.taskFlow = taskFlow;
    }

    public int getTaskUseFlow() {
        return taskUseFlow;
    }

    public void setTaskUseFlow(int taskUseFlow) {
        this.taskUseFlow = taskUseFlow;
    }
}
