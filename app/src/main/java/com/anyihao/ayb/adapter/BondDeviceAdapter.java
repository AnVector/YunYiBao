package com.anyihao.ayb.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

public class BondDeviceAdapter extends UAdapter<String> {

    public BondDeviceAdapter(List<String> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new BondDeviceListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

//        ((InformationViewHolder)holder).tvId.setText(position);
        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof BondDeviceListViewHolder) {
            String content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((BondDeviceListViewHolder) holder).tvDevice.setText(content);
            if (position % 2 == 0) {
                ((BondDeviceListViewHolder) holder).tvDeviceState.setText("已开启");
                ((BondDeviceListViewHolder) holder).tvDeviceState.setTextColor((
                        (BondDeviceListViewHolder) holder).getResources().getColor(R.color
                        .user_account_color));
            } else {
                changeLeftIcon(((BondDeviceListViewHolder) holder).tvDevice, (
                        (BondDeviceListViewHolder) holder).tvDevice.getContext(), R.drawable
                        .ic_device_disconnect);
                ((BondDeviceListViewHolder) holder).tvDeviceState.setText("未开启");
                ((BondDeviceListViewHolder) holder).tvDeviceState.setTextColor((
                        (BondDeviceListViewHolder) holder).getResources().getColor(R.color
                        .light_gray));
            }

            if (position == getAdapterItemCount() - 1) {
                ((BondDeviceListViewHolder) holder).line.setVisibility(View.GONE);
            }
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

    private class BondDeviceListViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvDevice;
        public TextView tvDeviceState;
        public View line;

        public BondDeviceListViewHolder(View itemView) {
            super(itemView);
            tvDevice = (TextView) itemView.findViewById(R.id.tv_device);
            tvDeviceState = (TextView) itemView.findViewById(R.id.tv_device_state);
            line = itemView.findViewById(R.id.line);
        }
    }
}
