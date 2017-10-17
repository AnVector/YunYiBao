package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.KeyValueBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class RentedDeviceAdapter extends UAdapter<KeyValueBean> {

    public RentedDeviceAdapter(List<KeyValueBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RentedDeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof RentedDeviceViewHolder) {
            KeyValueBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) {
                return;
            }
            ((RentedDeviceViewHolder) holder).tvTitle.setText(content.getTitle());
            ((RentedDeviceViewHolder) holder).tvValue.setText(content.getValue());
        }
    }


    private class RentedDeviceViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public TextView tvValue;

        public RentedDeviceViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
