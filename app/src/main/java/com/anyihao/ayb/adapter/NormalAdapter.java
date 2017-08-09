package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.TaskListBean.DataBean.NormalBean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class NormalAdapter extends RecyclerViewAdapter<NormalBean> {


    public NormalAdapter(Context context, int layoutId, List<NormalBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, NormalBean normalBean) {
        if (normalBean == null)
            return;
        holder.setText(R.id.tv_title, "可兑换" + normalBean.getExContent() + "流量");
        holder.setText(R.id.tv_points_get, normalBean.getIntegral() + "积分");
        holder.setText(R.id.tv_surplus, "剩余" + normalBean.getExchangeTimes() + "次可兑换");
        holder.displayRoundCornerImage(R.id.iv_advertisement, normalBean.getImage(), 0, 0);
    }
}
