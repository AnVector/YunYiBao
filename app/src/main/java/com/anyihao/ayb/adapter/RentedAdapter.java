package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class RentedAdapter extends UltimateViewAdapter {

    private List<String> mData;
    private OnItemClickListener mOnItemClickListener;

    public RentedAdapter(List<String> mData) {
        this.mData = mData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public long generateHeaderId(int position) {
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
                .inflate(R.layout.item_rented_device, parent, false);
        RentedViewHolder vh = new RentedViewHolder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        ((InformationViewHolder)holder).tvId.setText(position);
        if (holder instanceof RentedViewHolder) {
            if (position < getItemCount() && (customHeaderView != null ? position <= mData.size() :
                    position < mData.size()) && (customHeaderView != null ? position > 0 : true)) {
                String content = mData.get(customHeaderView != null ?
                        position - 1 : position);
                if (content == null) return;
                final int index = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(null, v, mData.get(index), index);
                        }
                    }
                });
                ((RentedViewHolder) holder).tvDevice.setText(content);
                ((RentedViewHolder) holder).tvUser.setText("用户：打快板的妹妹");
                ((RentedViewHolder) holder).tvDate.setText("2017-05-13");
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
    public void insert(String info, int position) {
        insertInternal(mData, info, position);
    }

    /**
     * @param current 要插入的数据
     */
    public void insert(List<String> current) {
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
            return mData.get(position);
        }
        return null;
    }

    private class RentedViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvUser;
        public TextView tvDevice;
        public TextView tvDate;
        public Button btnReturn;

        public RentedViewHolder(View itemView) {
            super(itemView);
            tvUser = (TextView) itemView.findViewById(R.id.tv_user);
            tvDevice = (TextView) itemView.findViewById(R.id.tv_device);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            btnReturn = (Button) itemView.findViewById(R.id.btn_return);
        }
    }
}
