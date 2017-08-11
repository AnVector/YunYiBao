package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RentedBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class RentedAdapter extends UAdapter<DataBean> {

    private OnItemButtonClickListener mOnItemButtonClickListener;

    public void setOnItemButtonClickListener(OnItemButtonClickListener
                                                     mOnItemButtonClickListener) {
        this.mOnItemButtonClickListener = mOnItemButtonClickListener;
    }

    public RentedAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RentedViewHolder(v);
    }

    public interface OnItemButtonClickListener {
        void onItemButtonClick(ViewGroup parent, View view, DataBean bean, int position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (holder instanceof RentedViewHolder) {
            final int index = (hasHeaderView() ? position - 1 : position);
            DataBean content = mData.get(index);
            if (content == null) return;
            ((RentedViewHolder) holder).tvDevice.setText("设备编号：" + content.getPrintId());
            ((RentedViewHolder) holder).tvUser.setText("用户：" + content.getNickname());
            ((RentedViewHolder) holder).tvDate.setText(content.getOutTm());
            ((RentedViewHolder) holder).btnReturn.setText("确认归还");
            ((RentedViewHolder) holder).btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemButtonClickListener != null) {
                        if (index < 0 || index >= mData.size())
                            return;
                        mOnItemButtonClickListener.onItemButtonClick((ViewGroup) v.getParent(),
                                v, mData.get
                                        (index), index);
                    }
                }
            });
        }
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
