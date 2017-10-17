package com.anyihao.ayb.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.ayb.bean.JPushBean;
import com.anyihao.ayb.frame.activity.MessageActivity;
import com.anyihao.ayb.frame.activity.PayActivity;
import com.anyihao.ayb.frame.activity.RechargeRecordActivity;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * Created by Admin on 2017/7/25.
 */

public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle bundle = intent.getExtras();
            Logger.d("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " +
                    printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d("[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface
                        .EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d("[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d("[MyReceiver] 用户点击打开了通知");
                //打开自定义的Activity
                launchActivity(context, bundle);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString
                        (JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface
                        .EXTRA_CONNECTION_CHANGE, false);
                Logger.w("[MyReceiver]" + intent.getAction() + " connected state change to "
                        + connected);
            } else {
                Logger.d("[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i("This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e("Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d(message);
        Logger.d(extras);

        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        ln.setTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        ln.setNotificationId(bundle.getInt("cn.jpush.android.MSG_ID"));
        ln.setBroadcastTime(System.currentTimeMillis());
        ln.setExtras(bundle.getString(JPushInterface.EXTRA_EXTRA));
        JPushInterface.addLocalNotification(context, ln);
    }

    private void launchActivity(Context context, Bundle bundle) {
        if (context == null || bundle == null) {
            return;
        }
        String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }
        JPushBean bean = GsonUtils.getInstance().transitionToBean(jsonStr, JPushBean.class);
        if (bean == null) {
            return;
        }
        Intent intent = new Intent();
        String type = bean.getType();
        String content = bean.getContent();
        switch (type) {
            case "message":
                handleMessageByContent(context, content);
                break;
            case "recharge":
                intent.setClass(context, RechargeRecordActivity.class);
                break;
            case "url":
                break;
            case "pay":
                intent.setClass(context, PayActivity.class);
                intent.putExtra("action", "notification");
                intent.putExtra("index", Integer.valueOf(content).intValue());
                break;
            default:
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void handleMessageByContent(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MessageActivity.class);
        intent.putExtra("action", "notification");
        switch (content) {
            case "0":
            case "1":
            case "2":
                intent.putExtra("index", Integer.valueOf(content));
            default:
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
