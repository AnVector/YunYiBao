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
    private double totalUseFlow;
    private int initFlow;
    private double initUseFlow;
    private int buyFlow;
    private double buyUseFlow;
    private int taskFlow;
    private double taskUseFlow;
    private int transferFlow;
    private double transferUseFlow;

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


    public void setTotalUseFlow(int totalUseFlow) {
        this.totalUseFlow = totalUseFlow;
    }

    public int getInitFlow() {
        return initFlow;
    }

    public void setInitFlow(int initFlow) {
        this.initFlow = initFlow;
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


    public void setBuyUseFlow(int buyUseFlow) {
        this.buyUseFlow = buyUseFlow;
    }

    public int getTaskFlow() {
        return taskFlow;
    }

    public void setTaskFlow(int taskFlow) {
        this.taskFlow = taskFlow;
    }


    public void setTaskUseFlow(int taskUseFlow) {
        this.taskUseFlow = taskUseFlow;
    }


    public void setTransferUseFlow(int transferUseFlow) {
        this.transferUseFlow = transferUseFlow;
    }

    public int getTransferFlow() {
        return transferFlow;
    }

    public void setTransferFlow(int transferFlow) {
        this.transferFlow = transferFlow;
    }

    public double getTotalUseFlow() {
        return totalUseFlow;
    }

    public void setTotalUseFlow(double totalUseFlow) {
        this.totalUseFlow = totalUseFlow;
    }

    public double getInitUseFlow() {
        return initUseFlow;
    }

    public void setInitUseFlow(double initUseFlow) {
        this.initUseFlow = initUseFlow;
    }

    public double getBuyUseFlow() {
        return buyUseFlow;
    }

    public void setBuyUseFlow(double buyUseFlow) {
        this.buyUseFlow = buyUseFlow;
    }

    public double getTaskUseFlow() {
        return taskUseFlow;
    }

    public void setTaskUseFlow(double taskUseFlow) {
        this.taskUseFlow = taskUseFlow;
    }

    public double getTransferUseFlow() {
        return transferUseFlow;
    }

    public void setTransferUseFlow(double transferUseFlow) {
        this.transferUseFlow = transferUseFlow;
    }
}
