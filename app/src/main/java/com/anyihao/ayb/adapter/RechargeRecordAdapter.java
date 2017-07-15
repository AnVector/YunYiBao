package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RechargeRecordListBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class RechargeRecordAdapter extends UAdapter<DataBean> {

    public RechargeRecordAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RechargeRecordViewHolder(v);
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

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof RechargeRecordViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            if ("WXPAY".equals(content.getTopupType())) {
                ((RechargeRecordViewHolder) holder).tvDescription.setText("微信充值成功" + content
                        .getFlow() + "流量");
                ((RechargeRecordViewHolder) holder).imgPayIcon.setImageResource(R.drawable
                        .ic_recharge_record_wxpay);
            } else {
                ((RechargeRecordViewHolder) holder).tvDescription.setText("支付宝充值成功" + content
                        .getFlow() + "流量");
                ((RechargeRecordViewHolder) holder).imgPayIcon.setImageResource(R.drawable
                        .ic_recharge_record_alipay);
            }
            String[] date = content.getCrtTm().split(" ");
            if (date.length == 2) {
                ((RechargeRecordViewHolder) holder).tvWeekday.setText(date[0]);
                ((RechargeRecordViewHolder) holder).tvTime.setText(date[1]);
            }
            ((RechargeRecordViewHolder) holder).tvPrice.setText("-" + content.getAmount() + "元");

        }
    }

    private class RechargeRecordViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvMonth;
        public TextView tvWeekday;
        public TextView tvTime;
        public TextView tvPrice;
        public TextView tvDescription;
        public ImageView imgPayIcon;
        public View line;
        public View recordItem;

        public RechargeRecordViewHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
            tvWeekday = (TextView) itemView.findViewById(R.id.tv_weekday);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            imgPayIcon = (ImageView) itemView.findViewById(R.id.img_payment_mode);
            line = itemView.findViewById(R.id.line);
            recordItem = itemView.findViewById(R.id.record_item);
        }
    }
}
