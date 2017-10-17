package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.KeyValueBean;

/**
 * Created by Admin on 2017/4/7.
 */

public class AboutUsAdapter extends RecyclerViewAdapter<KeyValueBean> {


    public AboutUsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, KeyValueBean bean) {
        if(bean == null) {
            return;
        }
        holder.setText(R.id.tv_title, bean.getTitle());
        holder.setText(R.id.tv_value, bean.getValue());
        if(holder.getLayoutPosition() == mDatas.size() -1){
            holder.setVisible(R.id.divider_line, false);
        }
    }
}
