package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class CreditAdapter extends RecyclerViewAdapter<String> {


    public CreditAdapter(Context context, int layoutId){
        super(context,layoutId);
    }
    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.title,s);
        holder.setText(R.id.date,"2017-05-17");
        holder.setText(R.id.tv_points,"+10");
    }
}
