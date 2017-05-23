package com.anyihao.ayb.adapter;

import android.content.Context;
import android.graphics.Color;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class AccountManageAdapter extends RecyclerViewAdapter<String> {


    public AccountManageAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.title, s);
        switch (s) {
            case "QQ":
                holder.setCompoundDrawables(R.id.title, R.drawable.qq_account,15);
                holder.setText(R.id.value, "未绑定");
                break;
            case "微信":
                holder.setCompoundDrawables(R.id.title, R.drawable.wx_account,15);
                holder.setText(R.id.value, "未绑定");
                break;
            case "微博":
                holder.setCompoundDrawables(R.id.title, R.drawable.wb_account,15);
                holder.setText(R.id.value, "已绑定");
                holder.setTextColor(R.id.value, Color.parseColor("#2DA8F4"));
                holder.setVisible(R.id.line, false);
                break;
            default:
                break;
        }
    }
}
