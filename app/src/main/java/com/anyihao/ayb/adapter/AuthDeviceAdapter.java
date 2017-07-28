package com.anyihao.ayb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean.DataBean;

/**
 * Created by Admin on 2017/4/7.
 */

public class AuthDeviceAdapter extends RecyclerViewAdapter<DataBean> {


    public AuthDeviceAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DataBean dataBean) {
        if (dataBean != null) {
            holder.setText(R.id.tv_device, dataBean.getMac());
            if (holder.getLayoutPosition() == mDatas.size() - 1) {
                holder.setVisible(R.id.line, false);
            }
        }

    }

    protected void setOnListener(final ViewGroup parent, final ViewHolder viewHolder, int
            viewType) {

        if (isFoucusEnabled(viewType)) {
            viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnItemFocusChangeListener != null) {
                        int position = getPosition(viewHolder);
                        //noinspection unchecked
                        mOnItemFocusChangeListener.onItemFocusChanged(parent, v, mDatas.get
                                (position), position, hasFocus);
                    }
                }
            });
        }

        if (isClickEnabled(viewType)) {
            Button btn = (Button) viewHolder.getConvertView().findViewById(R.id.tv_release_auth);
            if (btn == null)
                return;
            btn.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getPosition(viewHolder);
                        //noinspection unchecked
                        mOnItemClickListener.onItemClick(parent, v, mDatas.get(position),
                                position);
                    }
                }
            });

            btn.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressWarnings("unchecked")
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getPosition(viewHolder);
                        //noinspection unchecked
                        return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get
                                (position), position);
                    }
                    return false;
                }
            });
        }
    }
}
