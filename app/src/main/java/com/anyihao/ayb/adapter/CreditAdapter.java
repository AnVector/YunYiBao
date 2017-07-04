package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.CreditBean.DataBean;

/**
 * Created by Admin on 2017/4/7.
 */

public class CreditAdapter extends RecyclerViewAdapter<DataBean> {


    public CreditAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DataBean dataBean) {

        if (dataBean != null) {
            holder.setText(R.id.title, dataBean.getDescription());
            holder.setText(R.id.date, dataBean.getCrtTm());
            holder.setText(R.id.tv_points, "+" + dataBean.getPoints());
        }
        if (holder.getLayoutPosition() == mDatas.size() - 1) {
            holder.setVisible(R.id.line, false);
        }
    }
}
