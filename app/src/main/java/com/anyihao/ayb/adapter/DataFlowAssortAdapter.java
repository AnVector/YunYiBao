package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class DataFlowAssortAdapter extends RecyclerViewAdapter<String> {


    public DataFlowAssortAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tv_description, s);
        switch (s) {
            case "初始赠送剩余流量43.36%":
                holder.setBackgroundRes(R.id.flow_data_view, R.drawable.data_flow_bg1);
                break;
            case "购买会员剩余流量25.45%":
                holder.setBackgroundRes(R.id.flow_data_view, R.drawable.data_flow_bg2);
                break;
            case "任务赠送剩余流量43.60%":
                holder.setBackgroundRes(R.id.flow_data_view, R.drawable.data_flow_bg3);
                break;
            default:
                break;
        }
    }
}
