package com.anyihao.ayb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.TransferListBean.DataBean;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * author: Administrator
 * date: 2017/2/23 002310:47.
 * email:looper@126.com
 */

public class TransferRecordAdapter extends UAdapter<DataBean> {

    private Date mDate = new Date();
    private String mCurrent;

    {
        mCurrent = getCurrentDate(mDate);
    }

    public TransferRecordAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new TransferRecordViewHolder(v);
    }

    @Override
    public int getAdapterItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof TransferRecordViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) return;
            Context context = ((TransferRecordViewHolder) holder).getContext();
            if (content.getCrtTm() != null) {
                String[] arr = content.getCrtTm().split(" ");
                if (arr.length > 0) {
                    ((TransferRecordViewHolder) holder).tvWeekday.setText(getWeekOfDate
                            (convertToDate(arr[0])));
                    ((TransferRecordViewHolder) holder).tvTime.setText(arr[1]);
                    String month = arr[0].substring(0, 7);
                    if (!month.equals(mCurrent)) {
                        ((TransferRecordViewHolder) holder).tvMonth.setText(month);
                        ((TransferRecordViewHolder) holder).tvMonth.setVisibility(View.VISIBLE);
                        ((TransferRecordViewHolder) holder).line.setVisibility(View.GONE);
                        mCurrent = month;
                    }
                }

            }
            ((TransferRecordViewHolder) holder).tvDescription.setText(content
                    .getNickname() + "-转流量");
            ((TransferRecordViewHolder) holder).tvPrice.setText("-" + content.getFlow());
            if (context != null) {
                Glide.with(context)
                        .load(content.getAvatar())
                        .bitmapTransform(new CropCircleTransformation(context))
                        .placeholder(R.drawable.user_profile)
                        .crossFade()
                        .into(((TransferRecordViewHolder) holder).imgPayIcon);
            }
        }
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    private String getWeekOfDate(Date date) {
        if (date == null)
            return null;
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    private Date convertToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    private String getCurrentDate(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        return dateFormat.format(date);
    }


    private class TransferRecordViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvMonth;
        public TextView tvWeekday;
        public TextView tvTime;
        public TextView tvPrice;
        public TextView tvDescription;
        public ImageView imgPayIcon;
        public View line;
        public View recordItem;

        public TransferRecordViewHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
            tvWeekday = (TextView) itemView.findViewById(R.id.tv_weekday);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            imgPayIcon = (ImageView) itemView.findViewById(R.id.img_payment_mode);
            line = itemView.findViewById(R.id.line);
            recordItem = itemView.findViewById(R.id.record_item);
        }
    }
}
