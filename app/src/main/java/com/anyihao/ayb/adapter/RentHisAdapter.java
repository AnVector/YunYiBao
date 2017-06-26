package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RentHistoryListBean.DataBean;

/**
 * Created by Admin on 2017/4/7.
 */

public class RentHisAdapter extends RecyclerViewAdapter<DataBean> {


    public RentHisAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DataBean dataBean) {
        if(dataBean == null)
            return;
        holder.setText(R.id.tv_device_num, dataBean.getVid());
        holder.setText(R.id.tv_duration, dataBean.getStartTm() + "至" + dataBean.getEndTm());
    }
}
