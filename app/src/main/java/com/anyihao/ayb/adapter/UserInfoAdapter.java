package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class UserInfoAdapter extends RecyclerViewAdapter<String> {

    private String[] array = new String[]{"头像", "昵称", "我的二维码", "性别", "生日", "手机号码", "邮箱", "地区",
            "押金退款"};

    public UserInfoAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        String key = array[holder.getLayoutPosition()];
        holder.getConvertView().setTag(key);
        holder.setText(R.id.title, key);
        if ("头像".equals(key)) {
            holder.setVisible(R.id.value, false);
            holder.setVisible(R.id.img_profile, true);
            holder.setVisible(R.id.img_code, false);
            holder.displayCircleImage(R.id.img_profile, s, R.drawable.user_profile);
        } else if ("我的二维码".equals(key)) {
            holder.setImageResource(R.id.img_code, R.drawable.ic_qr_code);
            holder.setVisible(R.id.img_code, true);
            holder.setVisible(R.id.img_profile, false);
        } else if ("押金退款".equals(key)) {
            holder.setVisible(R.id.line, false);
            holder.setText(R.id.value, s);
        } else {
            holder.setText(R.id.value, s);
        }
        if ("生日".equals(key)) {
            holder.setVisible(R.id.space, true);
            holder.setVisible(R.id.line, false);
        }
    }
}
