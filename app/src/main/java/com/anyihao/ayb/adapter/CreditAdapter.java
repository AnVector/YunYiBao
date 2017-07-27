package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.CreditBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class CreditAdapter extends UAdapter<DataBean> {

    public CreditAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new CreditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof CreditViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((CreditViewHolder) holder).tvTitle.setText(content.getDescription());
            ((CreditViewHolder) holder).tvDate.setText(content.getCrtTm());
            ((CreditViewHolder) holder).tvPoints.setText(content.getPoints());
        }
    }


    private class CreditViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvPoints;

        public CreditViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvPoints = (TextView) itemView.findViewById(R.id.tv_points);
        }
    }
}
