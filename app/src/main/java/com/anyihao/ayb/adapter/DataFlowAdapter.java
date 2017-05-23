package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class DataFlowAdapter extends RecyclerViewAdapter<String> {

    private String[] price;


    public DataFlowAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setPrice(String[] price) {
        this.price = price;
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tv_data_amount, s);
        if(price!=null&&price.length>holder.getPosition()){
            holder.setText(R.id.tv_price, price[holder.getPosition()]);
        }
    }
}
