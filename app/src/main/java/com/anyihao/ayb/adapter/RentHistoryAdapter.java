package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RentHistoryListBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class RentHistoryAdapter extends UAdapter<DataBean> {

    public RentHistoryAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RentHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof RentHistoryViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((RentHistoryViewHolder) holder).tvTitle.setText("设备编号：" + content.getVid());
            ((RentHistoryViewHolder) holder).tvValue.setText(content.getStartTm() + "至" + content
                    .getEndTm());
            if (position == getAdapterItemCount() - 1) {
                ((RentHistoryViewHolder) holder).line.setVisibility(View.GONE);
            } else {
                ((RentHistoryViewHolder) holder).line.setVisibility(View.VISIBLE);
            }
        }
    }

    private class RentHistoryViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public TextView tvValue;
        public View line;

        public RentHistoryViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_device_num);
            tvValue = (TextView) itemView.findViewById(R.id.tv_duration);
            line = itemView.findViewById(R.id.divider_line);
        }
    }

}
