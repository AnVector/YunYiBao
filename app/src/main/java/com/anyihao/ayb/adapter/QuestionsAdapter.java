package com.anyihao.ayb.adapter;

import android.content.Context;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.QuestionListBean.DataBean;

/**
 * Created by Admin on 2017/4/7.
 */

public class QuestionsAdapter extends RecyclerViewAdapter<DataBean> {


    public QuestionsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DataBean dataBean) {
        holder.setText(R.id.tv_title, dataBean.getQuestion());
        if (holder.getPosition() == getItemCount() - 1) {
            holder.setVisible(R.id.divider_line, false);
        }
    }
}
