package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class AboutUsAdapter extends RecyclerViewAdapter<String> {


    public AboutUsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.title, s);
        switch (s) {
            case "当前版本":
                holder.setText(R.id.value, "V2.0.0");
                break;
            case "客服电话":
                holder.setText(R.id.value, "0571-7598279");
                break;
            case "微信公众号":
                holder.setText(R.id.value, "云逸宝WIFI");
                break;
            case "官方网站":
                holder.setText(R.id.value, "www.aybwifi.com");
                holder.setVisible(R.id.line, false);
                break;
            default:
                break;
        }
    }
}
