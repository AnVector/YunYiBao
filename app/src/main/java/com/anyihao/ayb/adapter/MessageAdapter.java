package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.MessageBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class MessageAdapter extends UAdapter<DataBean> {


    public MessageAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof MessageViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) {
                return;
            }
            ((MessageViewHolder) holder).tvStatus.setText(content.getSendName());
            ((MessageViewHolder) holder).tvDate.setText(content.getCrtTm());
            ((MessageViewHolder) holder).tvContent.setText(content.getMessage());
            if (content.getStatus() == 1) {
                ((MessageViewHolder) holder).dot.setVisibility(View.GONE);
            } else {
                ((MessageViewHolder) holder).dot.setVisibility(View.VISIBLE);
            }
            if (position == mData.size() - 1) {
                ((MessageViewHolder) holder).dividerLine.setVisibility(View.GONE);
            }
        }
    }

    private class MessageViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvStatus;
        public TextView tvDate;
        public TextView tvContent;
        public View dividerLine;
        public View dot;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            dividerLine = itemView.findViewById(R.id.divider_line);
            dot = itemView.findViewById(R.id.red_dot);
        }
    }
}
