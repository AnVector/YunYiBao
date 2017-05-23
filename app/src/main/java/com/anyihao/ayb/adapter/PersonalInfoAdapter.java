package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class PersonalInfoAdapter extends RecyclerViewAdapter<String> {


    public PersonalInfoAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.title, s);
        switch (s) {
            case "头像":
                holder.setVisible(R.id.value, false);
                holder.setImageResource(R.id.img_profile, R.drawable.user_profile);
                holder.setVisible(R.id.img_profile, true);
                break;
            case "昵称":
                holder.setText(R.id.value, "开心就好");
                break;
            case "我的二维码":
                holder.setImageResource(R.id.img_profile, R.drawable.ic_qr_code);
                holder.setVisible(R.id.img_profile, true);
                break;
            case "性别":
                holder.setText(R.id.value, "男");
                break;
            case "生日":
                holder.setText(R.id.value, "2017-03-24");
                holder.setVisible(R.id.space, true);
                holder.setVisible(R.id.line, false);
                break;
            case "手机号码":
                holder.setText(R.id.value, "15098808078");
                break;
            case "邮箱":
                holder.setText(R.id.value, "15098808078@163.com");
                break;
            case "地区":
                holder.setText(R.id.value, "滨江区");
                break;
            case "押金退款":
                holder.setVisible(R.id.line,false);
                break;
            default:
                break;


        }
    }
}
