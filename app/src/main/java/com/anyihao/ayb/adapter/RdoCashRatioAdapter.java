package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.SuccessRatioBean.DataBean.ClassificationBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class RdoCashRatioAdapter extends UltimateViewAdapter {

    private List<ClassificationBean> mData;
    private int totalCount = 1;
    private String startTime;
    private String endTime;

    public RdoCashRatioAdapter(List<ClassificationBean> mData) {
        this.mData = mData;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ratio, parent, false);
        return new RdoCashRatioViewHolder(v);
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

//        ((InformationViewHolder)holder).tvId.setText(position);
        if (holder instanceof RdoCashRatioViewHolder) {
            if (position < getItemCount() && (customHeaderView != null ? position <= mData.size() :
                    position < mData.size()) && (customHeaderView != null ? position > 0 : true)) {
                ClassificationBean content = mData.get(customHeaderView != null ?
                        position - 1 : position);
                if (content == null) return;
                String count = content.getCount();
                ((RdoCashRatioViewHolder) holder).tvDesc.setText(content.getRemark());
                ((RdoCashRatioViewHolder) holder).tvCount.setText(count);
                if (position == 1) {
                    ((RdoCashRatioViewHolder) holder).tvDesc.setTextSize(16);
                    ((RdoCashRatioViewHolder) holder).tvCount.setTextSize(16);
                    ((RdoCashRatioViewHolder) holder).tvPercent.setTextSize(16);
                    ((RdoCashRatioViewHolder) holder).tvDesc.getPaint().setFakeBoldText(true);
                    ((RdoCashRatioViewHolder) holder).tvCount.getPaint().setFakeBoldText(true);
                    ((RdoCashRatioViewHolder) holder).tvPercent.getPaint().setFakeBoldText(true);
                    ((RdoCashRatioViewHolder) holder).tvPercent.setText("占比");
                } else {
                    ((RdoCashRatioViewHolder) holder).tvDesc.setTextSize(14);
                    ((RdoCashRatioViewHolder) holder).tvCount.setTextSize(14);
                    ((RdoCashRatioViewHolder) holder).tvPercent.setTextSize(14);
                    ((RdoCashRatioViewHolder) holder).tvDesc.getPaint().setFakeBoldText(false);
                    ((RdoCashRatioViewHolder) holder).tvCount.getPaint().setFakeBoldText(false);
                    ((RdoCashRatioViewHolder) holder).tvPercent.getPaint().setFakeBoldText(false);
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher isNum = pattern.matcher(count);
                    if (isNum.matches()) {
                        double percent = (double) Integer.parseInt(count) * 100 / totalCount;
                        NumberFormat nbf = NumberFormat.getInstance();
                        nbf.setMinimumFractionDigits(2);
                        ((RdoCashRatioViewHolder) holder).tvPercent.setText(nbf.format(percent) +
                                "%");
                    }
                }

            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parallax_recyclerview_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView tvStart = (TextView) holder.itemView.findViewById(R.id.tv_start_time);
        TextView tvEnd = (TextView) holder.itemView.findViewById(R.id.tv_end_time);
        if (tvStart == null || tvEnd == null)
            return;
        tvStart.setText("起始时间："+startTime);
        tvEnd.setText("截止时间："+endTime);
    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    /**
     * @param info     要插入的数据
     * @param position 要插入的位置
     */
    public void insert(String info, int position) {
        insertInternal(mData, info, position);
    }

    /**
     * @param current 要插入的数据
     */
    public void insert(List<ClassificationBean> current) {
        insertInternal(current, mData);
    }

    public void remove(int position) {
        removeInternal(mData, position);
    }

    public void clear() {
        clearInternal(mData);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mData, from, to);
    }

    public String getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position >= 0 && position < mData.size()) {
            return mData.get(position).getRemark();
        }
        return null;
    }

    private static class RdoCashRatioViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvDesc;
        public TextView tvCount;
        public TextView tvPercent;

        public RdoCashRatioViewHolder(View itemView) {
            super(itemView);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_description);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            tvPercent = (TextView) itemView.findViewById(R.id.tv_percent);
        }
    }
}
