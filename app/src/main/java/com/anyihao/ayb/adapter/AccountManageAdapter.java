package com.anyihao.ayb.adapter;

import android.content.Context;
import android.graphics.Color;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.AccountListBean.DataBean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class AccountManageAdapter extends RecyclerViewAdapter<DataBean> {

    public AccountManageAdapter(Context context, int layoutId, List<DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, DataBean dataBean) {
        if (dataBean == null)
            return;
        switch (dataBean.getType()) {
            case "QQ":
                holder.setText(R.id.tv_title, "QQ");
                holder.setCompoundDrawables(R.id.tv_title, R.drawable.qq_account, 15);
                break;
            case "WX":
                holder.setText(R.id.tv_title, "微信");
                holder.setCompoundDrawables(R.id.tv_title, R.drawable.wx_account, 15);
                break;
            case "WB":
                holder.setText(R.id.tv_title, "微博");
                holder.setCompoundDrawables(R.id.tv_title, R.drawable.wb_account, 15);
                break;
            default:
                break;
        }

        if (dataBean.getStatus() == 0) {
            holder.setText(R.id.tv_value, "未绑定");
            holder.setTextColor(R.id.tv_value, Color.parseColor("#B5B5B5"));
        } else {
            holder.setText(R.id.tv_value, "已绑定");
            holder.setTextColor(R.id.tv_value, Color.parseColor("#2DA8F4"));
        }

        if (holder.getLayoutPosition() == mDatas.size() - 1) {
            holder.setVisible(R.id.divider_line, false);
        }
    }
}
