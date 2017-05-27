package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class SignInAdapter extends RecyclerViewAdapter<String> {


    public SignInAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.tv_title, s);
        if(s.equals("7")){
            holder.setVisible(R.id.line,false);
        }
    }
}
