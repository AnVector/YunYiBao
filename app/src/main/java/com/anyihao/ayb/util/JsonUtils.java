package com.anyihao.ayb.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/3/18 001813:46.
 * email:looper@126.com
 */

public class JsonUtils {

    //2.1注册接口
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","RGT");
            json.put("userName","");
            json.put("nickname","");
            json.put("ckpwd","");
            json.put("pwd","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //2.2登录接口
    //2.2.1 本地登录
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","IN");
            json.put("userName","");
            json.put("pwd","");
            json.put("appType","");
            json.put("devType","");
            json.put("ver","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //2.2.2 第三方登录
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","IN");
            json.put("appId","");
            json.put("appType","");
            json.put("nickname","");
            json.put("devType","");
            json.put("ver","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //2.3 轮播图
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","BANNER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //2.4 个人中心
    static{
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","PERSON");
            json.put("uid","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.5 修改个人信息
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","PERSONSAVE");
            json.put("uid","");
            json.put("avatar","");
            json.put("userName","");
            json.put("nickname","");
            json.put("sex","");
            json.put("birthday","");
            json.put("phoneNumber","");
            json.put("email","");
            json.put("area","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.6 修改密码
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","ALERTPWD");
            json.put("uid","");
            json.put("old","");
            json.put("pwd","");
            json.put("ckpwd","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.7 绑定设备
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","BIND");
            json.put("uid","");
            json.put("vid","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.8 获取绑定设备列表
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","BINDLIST");
            json.put("uid","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.9 解绑设备
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","DEBIND");
            json.put("uid","");
            json.put("vid","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.11 获取充值流量套餐信息
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","PAYINFO");
            json.put("uid","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //2.12 返回充值订单
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","PAY");
            json.put("uid","");
            json.put("amount","");
            json.put("topupType","");
            json.put("keyPackageID","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.13 获取充值订单
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","PAYLIST");
            json.put("uid","");
            json.put("page","");
            json.put("pagesize","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.14 充值记录详情
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","PAYDETAILS");
            json.put("uid","");
            json.put("idxOrderID","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.15 获取任务列表
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","TASK");
            json.put("taskType","");
            json.put("page","");
            json.put("pagesize","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.16 获取任务结果
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","TASKRESULT");
            json.put("uid","");
            json.put("taskId","");
            json.put("taskData","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.17 上传定位信息
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","LOCATION");
            json.put("uid","");
            json.put("latitude","");
            json.put("longtitude","");
            json.put("city","");
            json.put("citycode","");
            json.put("province","");
            json.put("pcode","");
            json.put("district","");
            json.put("adcode","");
            json.put("postcode","");
            json.put("street","");
            json.put("number","");
            json.put("township","");
            json.put("building","");
            json.put("roads","");
            json.put("address","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.18 我的流量
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","FLOW");
            json.put("uid","");
            json.put("sign","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.19 流量报表
    //2.19.1 日报表
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","DAYFLOW");
            json.put("uid","");
            json.put("day","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.19.2 月报表
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","MONTH");
            json.put("uid","");
            json.put("month","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.20 我的消息
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","MSG");
            json.put("uid","");
            json.put("page","");
            json.put("pagesize","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.21 反馈
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","FBK");
            json.put("title","");
            json.put("content","");
            json.put("contactNumber","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.22 帮助
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","HELP");
            json.put("page","");
            json.put("pagesize","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.23 版本控制
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","VERCTRL");
            json.put("ver","");
            json.put("appType","0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.24 流量兑换
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","REQCODE");
            json.put("uid","");
            json.put("reqCode","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.25 分享类型
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","SHARETYPE");
            json.put("shareType","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.26 获取分享信息
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","SHR");
            json.put("uid","");
            json.put("type","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.27 反馈分享结果
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","SHRRESULT");
            json.put("uid","");
            json.put("type","");
            json.put("shareId","");
            json.put("shareResult","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.31 获取最新消息
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","NEWMSG");
            json.put("uid","");
            json.put("time","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 2.32 退出登录
    static {
        JSONObject json = new JSONObject();
        try {
            json.put("cmd","OUT");
            json.put("uid","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String convertToJsonStr(List<String> params){
        JSONObject json = new JSONObject();
        return json.toString();
    }
}
