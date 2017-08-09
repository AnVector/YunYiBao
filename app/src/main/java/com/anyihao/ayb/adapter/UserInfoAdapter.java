package com.anyihao.ayb.adapter;

import android.content.Context;
import android.graphics.Color;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.KeyValueBean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class UserInfoAdapter extends RecyclerViewAdapter<KeyValueBean> {

    public UserInfoAdapter(Context context, int layoutId, List<KeyValueBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, KeyValueBean keyValueBean) {
        if (keyValueBean == null)
            return;
        String title = keyValueBean.getTitle();
        holder.setText(R.id.tv_title, title);
        switch (title) {
            case "头像":
                holder.setVisible(R.id.tv_value, false);
                holder.setVisible(R.id.space, false);
                holder.setVisible(R.id.line, true);
                holder.setVisible(R.id.img_profile, true);
                holder.setVisible(R.id.img_code, false);
                holder.displayCircleImage(R.id.img_profile, keyValueBean.getValue(), R.drawable
                        .user_profile);
                break;
            case "我的二维码":
                holder.setImageResource(R.id.img_code, R.drawable.ic_qr_code);
                holder.setVisible(R.id.img_code, true);
                holder.setVisible(R.id.line, true);
                holder.setVisible(R.id.img_profile, false);
                holder.setVisible(R.id.tv_value, false);
                break;
            case "押金退款":
                holder.setTextColor(R.id.tv_value, Color.parseColor("#ff1919"));
                holder.setVisible(R.id.line, false);
                holder.setText(R.id.tv_value, keyValueBean.getValue());
                break;
            default:
                holder.setTextColor(R.id.tv_value, Color.parseColor("#b5b5b5"));
                holder.setText(R.id.tv_value, keyValueBean.getValue());
                holder.setVisible(R.id.img_code, false);
                holder.setVisible(R.id.img_profile, false);
                break;
        }
        if ("生日".equals(title)) {
            holder.setVisible(R.id.space, true);
            holder.setVisible(R.id.line, false);
        }
    }
}
