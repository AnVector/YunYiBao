package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class RentedDeviceAdapter extends RecyclerViewAdapter<String> {

    private String[] array = new String[]{"设备编号", "设备版本", "设备状态", "租赁时间", "租赁状态", "商家名称", "商家地址",
            "商家联系方式"};

    public RentedDeviceAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        String title = array[holder.getLayoutPosition() % array.length];
        holder.setText(R.id.tv_title, title);
        holder.setText(R.id.tv_value, s);
        if ("商家联系方式".equals(title)) {
            holder.setVisible(R.id.line, false);
        }
    }
}
