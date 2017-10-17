package com.anyihao.ayb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.PackageListBean;
import com.anyihao.ayb.bean.PackageListBean.DataBean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */

public class DataFlowAdapter extends RecyclerViewAdapter<DataBean> {

    private View mCurrent;

    public DataFlowAdapter(Context context, int layoutId, List<DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, DataBean dataBean) {
        if (dataBean == null) {
            return;
        }
        if (holder.getLayoutPosition() == 0) {
            mCurrent = holder.getConvertView();
            setFocusView(mCurrent);
        }
        holder.setText(R.id.tv_data_amount, dataBean.getFlow());
        holder.setText(R.id.tv_price, dataBean.getPrice());
        if(dataBean.getActivity() == 9){
            holder.setVisible(R.id.imv_promotion, true);
        }else {
            holder.setVisible(R.id.imv_promotion, false);
        }
    }

    private void setFocusView(View view) {
        if (view == null) {
            return;
        }
        view.setBackgroundResource(R.drawable.data_flow_item_focus_bg);
        ((TextView) view.findViewById(R.id.tv_price)).setTextColor(mCurrent
                .getResources()
                .getColor(R.color.white));
        ((TextView) view.findViewById(R.id.tv_data_amount)).setTextColor(mCurrent
                .getResources()
                .getColor(R.color.white));
    }

    private void resetView() {
        if (mCurrent == null) {
            return;
        }
        mCurrent.setBackgroundResource(R.drawable.data_flow_item_normal_bg);
        ((TextView) mCurrent.findViewById(R.id.tv_price)).setTextColor
                (mContext.getResources()
                        .getColor(R.color.data_flow_item_price_text_color));
        ((TextView) mCurrent.findViewById(R.id.tv_data_amount)).setTextColor
                (mCurrent.getResources().getColor(R.color.toolbar_title_color));
    }

    @Override
    protected void setOnListener(final ViewGroup parent, final ViewHolder viewHolder, int
            viewType) {

        if (isClickEnabled(viewType)) {
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getPosition(viewHolder);
                        resetView();
                        setFocusView(v);
                        mCurrent = v;
                        mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                    }
                }
            });

            viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getPosition(viewHolder);
                        return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get
                                (position), position);
                    }
                    return false;
                }
            });
        }

        if (isFoucusEnabled(viewType)) {
            viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnItemFocusChangeListener != null) {
                        int position = getPosition(viewHolder);
                        mOnItemFocusChangeListener.onItemFocusChanged(parent, v, mDatas.get
                                (position), position, hasFocus);
                    }
                }
            });
        }
    }
}
