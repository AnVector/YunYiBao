package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class RentHisAdapter extends RecyclerViewAdapter<String> {


    public RentHisAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.tv_device_num, s);
        holder.setText(R.id.tv_duration, "2017-05-05至2017-06-05");
    }
}
