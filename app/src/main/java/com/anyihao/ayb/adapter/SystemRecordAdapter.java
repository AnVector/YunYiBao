package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RechargeRecordListBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class SystemRecordAdapter extends UAdapter<DataBean> {

    public SystemRecordAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new SystemRecordViewHolder(v);
    }

    @Override
    public int getAdapterItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof SystemRecordViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((SystemRecordViewHolder) holder).tvTitle.setText(content.getPkgInfo());
            ((SystemRecordViewHolder) holder).tvDate.setText(content.getCrtTm());
            ((SystemRecordViewHolder) holder).tvValue.setText("+" + content.getFlow());
        }
    }

    private class SystemRecordViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvValue;

        public SystemRecordViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }

}
