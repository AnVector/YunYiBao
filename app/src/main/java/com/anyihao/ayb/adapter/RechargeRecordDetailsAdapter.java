package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class RechargeRecordDetailsAdapter extends RecyclerViewAdapter<String> {


    public RechargeRecordDetailsAdapter(Context context, int layoutId){
        super(context,layoutId);
    }
    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.property,s);
        switch (s){
            case "流量充值":
                holder.setText(R.id.value,"100M");
                break;
            case "有效期限":
                holder.setText(R.id.value,"一年");
                break;
            case "付款方式":
                holder.setText(R.id.value,"支付宝支付");
                break;
            case "流水号":
                holder.setText(R.id.value,"AYB172827328745151546720");
                break;
            case "支付时间":
                holder.setText(R.id.value,"2017-03-25 17:22:22");
                break;
            default:
                break;
        }
        if(holder.getPosition() == getItemCount()-1){
            holder.setVisible(R.id.line,false);
        }
    }
}
