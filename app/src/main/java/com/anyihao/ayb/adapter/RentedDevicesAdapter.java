package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class RentedDevicesAdapter extends RecyclerViewAdapter<String> {


    public RentedDevicesAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tv_title, s);
        switch (s) {
            case "设备编号":
                holder.setText(R.id.tv_value, "Iebox-1000");
                break;
            case "设备版本":
                holder.setText(R.id.tv_value, "V1.0.0");
                break;
            case "设备状态":
                holder.setText(R.id.tv_value, "开启");
                break;
            case "租赁时间":
                holder.setText(R.id.tv_value, "2017-05-05");
                break;
            case "租赁状态":
                holder.setText(R.id.tv_value, "租用中");
                break;
            case "商家名称":
                holder.setText(R.id.tv_value, "JMart");
                break;
            case "商家地址":
                holder.setText(R.id.tv_value, "滨安路Ix-work B座603");
                break;
            case "商家联系方式":
                holder.setText(R.id.tv_value, "15098808078");
                holder.setVisible(R.id.line, false);
                break;
            default:
                break;
        }
    }
}
