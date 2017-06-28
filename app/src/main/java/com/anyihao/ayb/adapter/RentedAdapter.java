package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class RentedAdapter extends UAdapter<String> {

    public RentedAdapter(List<String> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RentedViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        ((InformationViewHolder)holder).tvId.setText(position);
        super.onBindViewHolder(holder, position);
        if (holder instanceof RentedViewHolder) {
            String content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            ((RentedViewHolder) holder).tvDevice.setText(content);
            ((RentedViewHolder) holder).tvUser.setText("用户：打快板的妹妹");
            ((RentedViewHolder) holder).tvDate.setText("2017-05-13");
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
