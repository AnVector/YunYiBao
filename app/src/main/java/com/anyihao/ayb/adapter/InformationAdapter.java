package com.anyihao.ayb.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ServerInfoList.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class InformationAdapter extends UltimateViewAdapter {

    private List<DataBean> mDatas;

    public InformationAdapter(List<DataBean> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public long generateHeaderId(int position) {
        if (getItem(position) != null) {
            return getItem(position).getTaskLength();
        }
        return -1;
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
                .inflate(R.layout.message_item, parent, false);
        InformationViewHolder vh = new InformationViewHolder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if(mDatas == null){
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        ((InformationViewHolder)holder).tvId.setText(position);
        if (holder instanceof InformationViewHolder) {
            if (position < getItemCount() && (customHeaderView != null ? position <= mDatas.size() :
                    position < mDatas.size()) && (customHeaderView != null ? position > 0 : true)) {
                DataBean serverInfo = mDatas.get(customHeaderView != null ?
                        position - 1 : position);
                if (serverInfo == null) return;
                ((InformationViewHolder) holder).tvTaskLength.setText("任务池长度：" + serverInfo
                        .getTaskLength());
                ((InformationViewHolder) holder).tvDate.setText(serverInfo.getCrtTm());
                ((InformationViewHolder) holder).tvId.setText(String.valueOf(position));
                ((InformationViewHolder) holder).tvServerId.setText("服务器编号：" + serverInfo
                        .getServerId());
                ((InformationViewHolder) holder).cvItem.setTag(String.valueOf(position));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.stick_header_item, parent, false);
//        return new RecyclerView.ViewHolder(view) {
//        };
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//        TextView textView = (TextView) holder.itemView.findViewById(R.id.stick_text);
//        textView.setText(String.valueOf(getItem(position).getIds()));
    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    /**
     * @param info     要插入的数据
     * @param position 要插入的位置
     */
    public void insert(DataBean info, int position) {
        insertInternal(mDatas, info, position);
    }

    /**
     * @param current 要插入的数据
     */
    public void insert(List<DataBean> current) {
        insertInternal(current, mDatas);
    }

    public void remove(int position) {
        removeInternal(mDatas, position);
    }

    public void clear() {
        clearInternal(mDatas);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mDatas, from, to);
    }

    public DataBean getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }

    private class InformationViewHolder extends UltimateRecyclerviewViewHolder {

        public CardView cvItem;
        public TextView tvId;
        public TextView tvTaskLength;
        public TextView tvServerId;
        public TextView tvDate;

        public InformationViewHolder(View itemView) {
            super(itemView);
            cvItem = (CardView) itemView.findViewById(R.id.card_view);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvServerId = (TextView) itemView.findViewById(R.id.tv_server_id);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTaskLength = (TextView) itemView.findViewById(R.id.tv_task_length);
        }
    }
}
