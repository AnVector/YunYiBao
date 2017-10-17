package com.anyihao.ayb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ConnectedUserBean.DataBean;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class ConnectedDeviceAdapter extends UAdapter<DataBean> {

    public ConnectedDeviceAdapter(List<DataBean> data, int layoutId) {
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
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) {
                return;
            }
            if (position == 0) {
                ((ConnectedDeviceListViewHolder) holder).space.setVisibility(View.VISIBLE);
                ((ConnectedDeviceListViewHolder) holder).line.setVisibility(View.GONE);
            }
            Context context = ((ConnectedDeviceListViewHolder) holder).getContext();
            if (context != null) {
                Glide.with(context)
                        .load(content.getAvatar())
                        .crossFade()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .placeholder(R.drawable.user_profile)
                        .into(((ConnectedDeviceListViewHolder) holder).mImageView);
            }
            ((ConnectedDeviceListViewHolder) holder).tvUserName.setText(content.getNickname());
            ((ConnectedDeviceListViewHolder) holder).tvOnlineDevice.setText(content.getDevice());
            if (position == mData.size() - 1) {
                ((ConnectedDeviceListViewHolder) holder).line.setVisibility(View.GONE);
            }
        }
    }

    private class ConnectedDeviceListViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvUserName;
        public TextView tvOnlineDevice;
        public ImageView mImageView;
        public View line;
        public View space;

        public ConnectedDeviceListViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvOnlineDevice = (TextView) itemView.findViewById(R.id.tv_online_device);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_user_profile);
            line = itemView.findViewById(R.id.divider_line);
            space = itemView.findViewById(R.id.space);
        }
    }
}
