package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class AdAdapter extends RecyclerViewAdapter<String> {


    public AdAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.tv_title, s);
    }
}
