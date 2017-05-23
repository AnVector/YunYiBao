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
        holder.setText(R.id.title, s);

        switch (s) {
            case "意见反馈":
            case "关于云逸宝":
                holder.setVisible(R.id.space, true);
                holder.setVisible(R.id.line, false);
                break;
            default:
                break;
        }

    }
}
