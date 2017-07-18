package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.KeyValueBean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class RechargeRecordDetailsAdapter extends RecyclerViewAdapter<KeyValueBean> {


    public RechargeRecordDetailsAdapter(Context context, int layoutId, List<KeyValueBean> mData) {
        super(context, layoutId, mData);
    }

    @Override
    public void convert(ViewHolder holder, KeyValueBean keyValueBean) {
        if (keyValueBean == null)
            return;
        holder.setText(R.id.property, keyValueBean.getTitle());
        holder.setText(R.id.value, keyValueBean.getValue() + "");
        if (holder.getPosition() == getItemCount() - 1) {
            holder.setVisible(R.id.line, false);
        }
    }
}
