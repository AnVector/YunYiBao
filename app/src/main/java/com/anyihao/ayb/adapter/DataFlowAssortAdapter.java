package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class DataFlowAssortAdapter extends RecyclerViewAdapter<Double> {

    private String[] array = new String[]{"初始赠送剩余流量", "购买会员剩余流量", "转赠剩余流量", "任务赠送剩余流量"};


    public DataFlowAssortAdapter(Context context, int layoutId, List<Double> data) {
        super(context, layoutId, data);
    }

    @Override
    public void convert(ViewHolder holder, Double aDouble) {
        holder.setText(R.id.tv_description, array[holder.getLayoutPosition()] + aDouble + "%");
        switch (holder.getLayoutPosition()) {
            case 0:
                holder.setProgressDrawable(R.id.seekBar, mContext.getResources().getDrawable(R
                        .drawable.seekbar_bg1));
                holder.setProgress(R.id.seekBar, aDouble.intValue());
                break;
            case 1:
                holder.setProgressDrawable(R.id.seekBar, mContext.getResources().getDrawable(R
                        .drawable.seekbar_bg2));
                holder.setProgress(R.id.seekBar, aDouble.intValue());
                break;
            case 2:
                holder.setProgressDrawable(R.id.seekBar, mContext.getResources().getDrawable(R
                        .drawable.seekbar_bg3));
                holder.setProgress(R.id.seekBar, aDouble.intValue());
                break;
            case 3:
                holder.setProgressDrawable(R.id.seekBar, mContext.getResources().getDrawable(R
                        .drawable.seekbar_bg4));
                holder.setProgress(R.id.seekBar, aDouble.intValue());
                break;
            default:
                break;
        }
    }
}
