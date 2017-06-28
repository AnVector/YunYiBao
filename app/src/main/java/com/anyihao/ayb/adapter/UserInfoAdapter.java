package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class UserInfoAdapter extends RecyclerViewAdapter<String> {

    private String[] array = new String[]{"头像", "昵称", "我的二维码", "性别", "生日", "手机号码", "邮箱", "地区",
            "押金退款"};

    public UserInfoAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        String key = array[holder.getLayoutPosition()];
        holder.getConvertView().setTag(key);
        holder.setText(R.id.title, key);
        if ("头像".equals(key)) {
            holder.setVisible(R.id.value, false);
            holder.setImageResource(R.id.img_profile, R.drawable.user_profile);
            holder.setVisible(R.id.img_profile, true);
        } else if ("我的二维码".equals(key)) {
            holder.setImageResource(R.id.img_profile, R.drawable.ic_qr_code);
            holder.setVisible(R.id.img_profile, true);
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
