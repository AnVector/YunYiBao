package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.ayb.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class MeAdapter extends RecyclerViewAdapter<String> {

    private String[] mItems1 = new String[]{"我的流量", "流量商城", "流量报表", "充值记录", "邀请好友", "输入邀请码",
            "系统赠送记录",
            "我的积分", "商家特权", "授权设备管理"};
    private String[] mItems2 = new String[]{"我的流量", "流量商城", "流量报表", "充值记录", "邀请好友", "输入邀请码",
            "系统赠送记录",
            "我的积分", "授权设备管理"};

    public MeAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        String title;
        if (getItemCount() == 10) {
            title = mItems1[holder.getLayoutPosition() % mItems1.length];
        } else {
            title = mItems2[holder.getLayoutPosition() % mItems2.length];
        }
        holder.setText(R.id.title, title);
        holder.getConvertView().setTag(title);
        switch (title) {
            case "我的流量":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_balance, 15);
                if (!StringUtils.isEmpty(s)) {
                    float amount = Float.parseFloat(s);
                    if (amount > 1024f) {
                        float f = amount / 1024;
                        float ft = new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP)
                                .floatValue();
                        holder.setText(R.id.value, ft + "G");
                    } else {
                        holder.setText(R.id.value, s + "M");
                    }
                    holder.setVisible(R.id.value, true);
                } else {
                    holder.setVisible(R.id.value, false);
                }
                holder.setVisible(R.id.line, false);
                holder.setVisible(R.id.space, true);
                break;
            case "流量商城":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_shop, 15);
                holder.setVisible(R.id.value, false);
                break;
            case "流量报表":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_chart, 15);
                holder.setVisible(R.id.space, true);
                holder.setVisible(R.id.line, false);
                holder.setVisible(R.id.value, false);
                break;
            case "充值记录":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_history, 15);
                holder.setVisible(R.id.value, false);
                break;
            case "邀请好友":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_invite, 15);
                holder.setVisible(R.id.line, false);
                holder.setVisible(R.id.space, true);
                holder.setVisible(R.id.value, false);
                break;
            case "输入邀请码":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_code, 15);
                holder.setVisible(R.id.value, false);
                break;
            case "系统赠送记录":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_gift_his, 15);
                holder.setVisible(R.id.value, false);
                break;
            case "我的积分":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_credit, 15);
                holder.setText(R.id.value, s);
                holder.setVisible(R.id.value, true);
                holder.setVisible(R.id.line, false);
                holder.setVisible(R.id.space, true);
                break;
            case "商家特权":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_privilege, 15);
                holder.setVisible(R.id.value, false);
                break;
            case "授权设备管理":
                holder.setCompoundDrawables(R.id.title, R.drawable.ic_manage, 15);
                holder.setVisible(R.id.line, false);
                holder.setVisible(R.id.value, false);
                holder.setVisible(R.id.value, false);
                break;
            default:
                break;
        }
    }
}
