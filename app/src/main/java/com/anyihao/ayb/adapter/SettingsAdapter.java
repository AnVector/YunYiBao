package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class SettingsAdapter extends RecyclerViewAdapter<String> {


    public SettingsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tv_title, s);

        if ("意见反馈".equals(s) || "关于云逸宝".equals(s)) {
            holder.setVisible(R.id.space, true);
            holder.setVisible(R.id.divider_line, false);
        }
    }
}
