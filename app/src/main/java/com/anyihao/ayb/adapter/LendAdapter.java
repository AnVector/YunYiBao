package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.TaskInfoListBean.DataBean.LendBean;

/**
 * Created by Admin on 2017/4/7.
 */

public class LendAdapter extends RecyclerViewAdapter<LendBean> {


    public LendAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, LendBean lendBean) {
        holder.setText(R.id.tv_title, "可兑换" + lendBean.getExContent() + "流量");
        holder.setText(R.id.tv_points_get, lendBean.getIntegral() + "积分");
        holder.setText(R.id.tv_surplus, "剩余" + lendBean.getExchangeTimes() + "次可兑换");
        holder.displayRoundCornerImage(R.id.iv_advertisement, lendBean.getImage(), 0, 0);
    }
}
