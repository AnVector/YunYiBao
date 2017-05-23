package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class SystemRecordAdapter extends RecyclerViewAdapter<String> {


    public SystemRecordAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.title, s);
        holder.setText(R.id.tv_date, "2017-05-10");
        holder.setText(R.id.tv_value, "+100M");
        if(holder.getLayoutPosition() == mDatas.size()-1){
            holder.setVisible(R.id.line,false);
        }
    }
}
