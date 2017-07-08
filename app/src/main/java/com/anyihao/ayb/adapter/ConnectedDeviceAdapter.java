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

public class ConnectedDeviceAdapter extends UAdapter<String> {

    public ConnectedDeviceAdapter(List<String> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ConnectedDeviceListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof ConnectedDeviceListViewHolder) {
            String content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            if (position == 0) {
                ((ConnectedDeviceListViewHolder) holder).space.setVisibility(View.VISIBLE);
                ((ConnectedDeviceListViewHolder) holder).line.setVisibility(View.GONE);
            }
            ((ConnectedDeviceListViewHolder) holder).tvUserName.setText(content);
            if (position % 2 == 0) {
                ((ConnectedDeviceListViewHolder) holder).tvOnlineDevice.setText("iPad在线");
            } else {
                ((ConnectedDeviceListViewHolder) holder).tvOnlineDevice.setText("手机在线");
            }
        }
    }

    private class ConnectedDeviceListViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvUserName;
        public TextView tvOnlineDevice;
        public View line;
        public View space;

        public ConnectedDeviceListViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvOnlineDevice = (TextView) itemView.findViewById(R.id.tv_online_device);
            line = itemView.findViewById(R.id.line);
            space = itemView.findViewById(R.id.space);
        }
    }
}
