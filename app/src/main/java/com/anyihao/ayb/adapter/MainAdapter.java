package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.WifiInfoBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class MainAdapter extends UAdapter<WifiInfoBean> {

    public MainAdapter(List<WifiInfoBean> data, int layoutId) {
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

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof MainViewHolder) {
            WifiInfoBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((MainViewHolder) holder).tvTitle.setText(content.getSsid());
            if (content.isConnected()) {
                ((MainViewHolder) holder).mIcConnect.setVisibility(View.VISIBLE);
            } else {
                ((MainViewHolder) holder).mIcConnect.setVisibility(View.GONE);
            }
        }
    }

    private class MainViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public View line;
        public ImageView mIcConnect;

        public MainViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            line = itemView.findViewById(R.id.line);
            mIcConnect = (ImageView) itemView.findViewById(R.id.imv_connected);
        }
    }
}
