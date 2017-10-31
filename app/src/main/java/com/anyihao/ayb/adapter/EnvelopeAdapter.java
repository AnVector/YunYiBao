package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RedPackageBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class EnvelopeAdapter extends UAdapter<DataBean> {


    public EnvelopeAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new EnvelopeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof EnvelopeViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) {
                return;
            }
            ((EnvelopeViewHolder) holder).tvStatus.setText(content.getSendName());
            ((EnvelopeViewHolder) holder).tvDate.setText(content.getCrtTm());
            ((EnvelopeViewHolder) holder).tvContent.setText(content.getMessage());
            if (content.getStatus() == 1) {
                ((EnvelopeViewHolder) holder).dot.setVisibility(View.GONE);
            } else {
                ((EnvelopeViewHolder) holder).dot.setVisibility(View.VISIBLE);
            }
            if (position == mData.size() - 1) {
                ((EnvelopeViewHolder) holder).dividerLine.setVisibility(View.GONE);
            }
        }
    }

    private class EnvelopeViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvStatus;
        public TextView tvDate;
        public TextView tvContent;
        public View dividerLine;
        public View dot;

        public EnvelopeViewHolder(View itemView) {
            super(itemView);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            dividerLine = itemView.findViewById(R.id.divider_line);
            dot = itemView.findViewById(R.id.red_dot);
        }
    }
}
