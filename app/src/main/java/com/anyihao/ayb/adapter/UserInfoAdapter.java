package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ProfileBean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class UserInfoAdapter extends RecyclerViewAdapter<ProfileBean> {

    public UserInfoAdapter(Context context, int layoutId, List<ProfileBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, ProfileBean profileBean) {
        if (profileBean == null)
            return;
        String title = profileBean.getTitle();
        holder.setText(R.id.title, title);
        switch (title) {
            case "头像":
                holder.setVisible(R.id.value, false);
                holder.setVisible(R.id.space, false);
                holder.setVisible(R.id.line, true);
                holder.setVisible(R.id.img_profile, true);
                holder.setVisible(R.id.img_code, false);
                holder.displayCircleImage(R.id.img_profile, profileBean.getValue(), R.drawable
                        .user_profile);
                break;
            case "我的二维码":
                holder.setImageResource(R.id.img_code, R.drawable.ic_qr_code);
                holder.setVisible(R.id.img_code, true);
                holder.setVisible(R.id.line, true);
                holder.setVisible(R.id.img_profile, false);
                holder.setVisible(R.id.value, false);
                break;
            case "押金退款":
                holder.setVisible(R.id.line, false);
                holder.setText(R.id.value, profileBean.getValue());
                break;
            default:
                holder.setText(R.id.value, profileBean.getValue());
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
