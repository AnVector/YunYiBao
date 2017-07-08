package com.anyihao.ayb.adapter;

import android.content.Context;
import android.graphics.Color;

import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.ayb.R;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class SignAdapter extends RecyclerViewAdapter<String> {

    public SignAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }


    @Override
    public void convert(ViewHolder holder, String s) {
        if (StringUtils.isEmpty(s))
            return;
        if (s.contains("7")) {
            holder.setInVisible(R.id.line);
        }
        String tag = s.substring(1);
        String weekday = s.substring(0, 1);
        switch (tag) {
            case "0":
                holder.setText(R.id.tv_title, weekday);//文字
                holder.setBackgroundRes(R.id.tv_title, R.drawable.unsign_bg);//文字背景
                holder.setBackgroundColor(R.id.line, Color.parseColor("#78acf0"));//连接线背景
                break;
            case "1":
                holder.setText(R.id.tv_title, weekday);
                holder.setBackgroundRes(R.id.tv_title, R.drawable.signed_bg);
                holder.setBackgroundColor(R.id.line, Color.parseColor("#feee35"));
                break;
            case "2":
                holder.setText(R.id.tv_title, "");
                holder.setBackgroundRes(R.id.tv_title, R.drawable.ic_signed);
                holder.setBackgroundColor(R.id.line, Color.parseColor("#78acf0"));
                break;
            default:
                break;
        }
    }

}
