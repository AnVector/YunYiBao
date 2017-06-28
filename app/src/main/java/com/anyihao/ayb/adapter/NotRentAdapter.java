package com.anyihao.ayb.adapter;

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

public class NotRentAdapter extends UAdapter<String> {

    public NotRentAdapter(List<String> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new NotRentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof NotRentViewHolder) {
            String content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((NotRentViewHolder) holder).tvDevice.setText(content);
        }
    }

    private class NotRentViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvDevice;

        public NotRentViewHolder(View itemView) {
            super(itemView);
            tvDevice = (TextView) itemView.findViewById(R.id.tv_device);
        }
    }
}
