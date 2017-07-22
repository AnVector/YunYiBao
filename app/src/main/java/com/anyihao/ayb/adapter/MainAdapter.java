package com.anyihao.ayb.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class MainAdapter extends UAdapter<ScanResult> {

    public MainAdapter(List<ScanResult> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

//        ((InformationViewHolder)holder).tvId.setText(position);
        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof MainViewHolder) {
            ScanResult content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((MainViewHolder) holder).tvTitle.setText(content.SSID);
            changeLeftIcon(((MainViewHolder) holder).tvTitle, ((MainViewHolder) holder)
                    .getContext(), R.drawable.ic_wifi_left);
        }
    }

    private void changeLeftIcon(TextView tv, Context context, int drawableId) {
        Drawable lefeIcon;
        Resources res = context.getResources();
        lefeIcon = res.getDrawable(drawableId);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        lefeIcon.setBounds(0, 0, lefeIcon.getMinimumWidth(), lefeIcon.getMinimumHeight());
        tv.setCompoundDrawables(lefeIcon, null, null, null); //设置左图标
    }

    private class MainViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public View line;
        public MainViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            line = itemView.findViewById(R.id.line);
        }
    }
}