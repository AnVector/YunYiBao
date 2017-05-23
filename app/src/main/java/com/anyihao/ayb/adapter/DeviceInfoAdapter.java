package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class DeviceInfoAdapter extends RecyclerViewAdapter<String> {


    public DeviceInfoAdapter(Context context, int layoutId){
        super(context,layoutId);
    }
    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.title,s);
        switch (s){
            case "修改WIFI名称":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_modify,15);
                break;
            case "修改WIFI密码":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_modify,15);
                holder.setVisible(R.id.space,true);
                holder.setVisible(R.id.line,false);
                break;
            case "设备重启":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_restart,15);
                break;
            case "设备重置":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_reset,15);
                holder.setVisible(R.id.line,false);
                break;
            default:
                break;


        }

    }
}
