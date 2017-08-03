package com.anyihao.ayb.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.KeyValueBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class MeAdapter extends UAdapter<KeyValueBean> {

    public MeAdapter(List<KeyValueBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new MeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof MeViewHolder) {
            KeyValueBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            MeViewHolder viewHolder = (MeViewHolder) holder;
            viewHolder.tvTitle.setText(content.getTitle());
            switch (content.getTitle()) {
                case "我的流量":
                    setCompoundDrawables(viewHolder, R.drawable.ic_balance);
                    if (!TextUtils.isEmpty(content.getValue())) {
                        float amount = Float.parseFloat(content.getValue());
                        if (amount >= 1024f) {
                            float f = amount / 1024;
                            float ft = new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .floatValue();
                            viewHolder.tvValue.setText(ft + "G");
                        } else {
                            viewHolder.tvValue.setText(content.getValue() + "M");
                        }
                        viewHolder.tvValue.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvValue.setVisibility(View.GONE);
                    }
                    viewHolder.line.setVisibility(View.GONE);
                    viewHolder.space.setVisibility(View.VISIBLE);
                    break;
                case "流量商城":
                    setCompoundDrawables(viewHolder, R.drawable.ic_shop);
                    setVisibility(viewHolder, true);
                    break;
                case "流量报表":
                    setCompoundDrawables(viewHolder, R.drawable.ic_chart);
                    setVisibility(viewHolder, false);
                    break;
                case "充值记录":
                    setCompoundDrawables(viewHolder, R.drawable.ic_history);
                    setVisibility(viewHolder, true);
                    break;
                case "邀请好友":
                    setCompoundDrawables(viewHolder, R.drawable.ic_invite);
                    setVisibility(viewHolder, false);
                    break;
                case "输入邀请码":
                    setCompoundDrawables(viewHolder, R.drawable.ic_code);
                    setVisibility(viewHolder, true);
                    break;
                case "系统赠送记录":
                    setCompoundDrawables(viewHolder, R.drawable.ic_gift_his);
                    viewHolder.tvValue.setVisibility(View.GONE);
                    break;
                case "我的积分":
                    setCompoundDrawables(viewHolder, R.drawable.ic_credit);
                    viewHolder.tvValue.setText(content.getValue());
                    viewHolder.tvValue.setVisibility(View.VISIBLE);
                    viewHolder.line.setVisibility(View.GONE);
                    viewHolder.space.setVisibility(View.VISIBLE);
                    break;
                case "商家特权":
                    setCompoundDrawables(viewHolder, R.drawable.ic_privilege);
                    setVisibility(viewHolder, true);
                    break;
                case "授权设备管理":
                    setCompoundDrawables(viewHolder, R.drawable.ic_manage);
                    setVisibility(viewHolder, false);
                    viewHolder.space.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    private void setVisibility(MeViewHolder holder, boolean visibility) {
        if (!visibility) {
            holder.line.setVisibility(View.GONE);
            holder.space.setVisibility(View.VISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
            holder.space.setVisibility(View.GONE);
        }
        holder.tvValue.setVisibility(View.GONE);
    }

    private void setCompoundDrawables(MeViewHolder holder, int drawableId) {
        Resources resources = holder.getContext().getResources();
        Drawable mDrawable = resources.getDrawable(drawableId);
        if (mDrawable != null) {
            mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                    mDrawable.getMinimumHeight());
            holder.tvTitle.setCompoundDrawablePadding(15);
            holder.tvTitle.setCompoundDrawables(mDrawable, null, null, null);
        }
    }


    private class MeViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvTitle;
        public TextView tvValue;
        public View line;
        public View space;

        public MeViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
            line = itemView.findViewById(R.id.line);
            space = itemView.findViewById(R.id.space);
        }
    }
}
