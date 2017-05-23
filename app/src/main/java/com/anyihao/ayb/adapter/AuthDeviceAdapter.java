package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class AuthDeviceAdapter extends RecyclerViewAdapter<String> {


    public AuthDeviceAdapter(Context context, int layoutId){
        super(context,layoutId);
    }
    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.tv_device,s);
    }
}
