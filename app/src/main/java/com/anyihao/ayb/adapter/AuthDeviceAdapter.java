package com.anyihao.ayb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean.DataBean;
import com.anyihao.ayb.bean.ConnectedUserBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class AuthDeviceAdapter extends UAdapter<DataBean> {

    public AuthDeviceAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new AuthDeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null)
            return;
        //变量index设置为final的原因，可将变量index设为全局变量作为对比
        final int index = (hasHeaderView() ? position - 1 : position);
        if (hasHeaderView()) {
            bp = position <= getAdapterItemCount() && position > 0 && position < getItemCount();
        } else {
            bp = position < getItemCount() && position < getAdapterItemCount();
        }
        if (bp && holder instanceof AuthDeviceViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((AuthDeviceViewHolder) holder).tvMacAddress.setText(content.getMac());

            ((AuthDeviceViewHolder) holder).btnRelease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        if (index < 0 || index >= mData.size())
                            return;
                        mOnItemClickListener.onItemClick((ViewGroup) v.getParent(), v, mData.get
                                (index), index);
                    }
                }
            });
        }

    }

    private class AuthDeviceViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvMacAddress;
        public Button btnRelease;
        public View line;

        public AuthDeviceViewHolder(View itemView) {
            super(itemView);
            tvMacAddress = (TextView) itemView.findViewById(R.id.tv_device);
            btnRelease = (Button) itemView.findViewById(R.id.btn_release_auth);
            line = itemView.findViewById(R.id.line);
        }
    }
}
