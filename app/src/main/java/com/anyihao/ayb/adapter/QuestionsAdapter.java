package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class QuestionsAdapter extends RecyclerViewAdapter<String> {


    public QuestionsAdapter(Context context, int layoutId){
        super(context,layoutId);
    }
    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.title,s);
        if(holder.getPosition() == getItemCount()-1){
            holder.setVisible(R.id.line,false);
        }
    }
}
