package com.anyihao.ayb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anyihao.ayb.listener.OnItemClickListener;
import com.anyihao.ayb.listener.OnItemFocusChangeListener;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/22 002217:03.
 * email:looper@126.com
 */

public abstract class UltimateAdapter<T> extends UltimateViewAdapter<UltimateViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    //click listener
    protected OnItemClickListener mOnItemClickListener;
    //focus listener
    protected OnItemFocusChangeListener mOnItemFocusChangeListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemFocusChangeListener(OnItemFocusChangeListener onItemFocusChangeListener) {

        this.mOnItemFocusChangeListener = onItemFocusChangeListener;
    }

    //重载构造方法
    public UltimateAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    //重载构造方法
    public UltimateAdapter(Context context, int layoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
    }

    // 定义对外开放的添加数据的方法
    public void add(int positionStart, int itemCount,
                    List<T> datas) {
        if (datas == null)
            return;
        if (mDatas == null) {
            mDatas = datas;
        }
        notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public UltimateViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        UltimateViewHolder viewHolder = UltimateViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setOnListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    @Override
    public UltimateViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public UltimateViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public UltimateViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isClickEnabled(int viewType) {
        return true;
    }

    protected boolean isFoucusEnabled(int viewType) {
        return true;
    }


    protected void setOnListener(final ViewGroup parent, final UltimateViewHolder viewHolder, int
            viewType) {
        if (isClickEnabled(viewType)) {
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getPosition(viewHolder);
                        mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                    }
                }
            });

            viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getPosition(viewHolder);
                        return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get
                                (position), position);
                    }
                    return false;
                }
            });
        }

        if (isFoucusEnabled(viewType)) {
            viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnItemFocusChangeListener != null) {
                        int position = getPosition(viewHolder);
                        mOnItemFocusChangeListener.onItemFocusChanged(parent, v, mDatas.get
                                (position), position, hasFocus);
                    }
                }
            });
        }


    }

    @Override
    public void onBindViewHolder(UltimateViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(UltimateViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return (mDatas == null ? 0 : mDatas.size());
    }

    @Override
    public int getAdapterItemCount() {
        return (mDatas == null ? 0 : mDatas.size());
    }
}
