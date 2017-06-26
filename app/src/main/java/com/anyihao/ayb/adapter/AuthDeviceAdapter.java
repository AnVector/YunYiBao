package com.anyihao.ayb.adapter;

import android.content.Context;

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
        holder.setText(R.id.tv_device, dataBean.getMac());
        if (holder.getLayoutPosition() == mDatas.size() - 1) {
            holder.setVisible(R.id.line, false);
        }
    }
}
