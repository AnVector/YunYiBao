package com.anyihao.ayb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RechargeRecordListBean.DataBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.orhanobut.logger.Logger;

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

public class RechargeRecordAdapter extends UAdapter<DataBean> {

    public RechargeRecordAdapter(List<DataBean> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RechargeRecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        if (bp && holder instanceof RechargeRecordViewHolder) {
            DataBean content = mData.get((hasHeaderView() ? position - 1 : position));
            if (content == null) {
                return;
            }
            String pkgType = content.getPkgType();
            if ("WXPAY".equals(content.getTopupType())) {
                ((RechargeRecordViewHolder) holder).imgPayIcon.setImageResource(R.drawable
                        .ic_recharge_record_wxpay);
                if ("DEPOSIT".equals(pkgType)) {
                    if ("7".equals(content.getStatus())) {
                        ((RechargeRecordViewHolder) holder).tvDescription.setText("退款成功-微信支付");
                    } else {
                        ((RechargeRecordViewHolder) holder).tvDescription.setText("缴纳押金-微信支付");
                    }
                } else {
                    ((RechargeRecordViewHolder) holder).tvDescription.setText("微信充值成功" + content
                            .getFlow() + "流量");
                }

            } else {
                ((RechargeRecordViewHolder) holder).imgPayIcon.setImageResource(R.drawable
                        .ic_recharge_record_alipay);
                if ("DEPOSIT".equals(pkgType)) {
                    if ("7".equals(content.getStatus())) {
                        ((RechargeRecordViewHolder) holder).tvDescription.setText("退款成功-支付宝支付");
                    } else {
                        ((RechargeRecordViewHolder) holder).tvDescription.setText("缴纳押金-支付宝支付");
                    }
                } else {
                    ((RechargeRecordViewHolder) holder).tvDescription.setText("支付宝充值成功" + content
                            .getFlow() + "流量");
                }
            }

            String[] date = content.getCrtTm().split(" ");
            String week = null;
            if (date[0].length() > 5) {
                week = getWeekOfDate(date[0]);
                ((RechargeRecordViewHolder) holder).tvDate.setText(date[0].substring(5));
            }
            if (week != null) {
                ((RechargeRecordViewHolder) holder).tvWeekday.setText(week);
            }
            if ("7".equals(content.getStatus())) {
                ((RechargeRecordViewHolder) holder).tvPrice.setText("+" + content.getAmount() +
                        "元");
            } else {
                ((RechargeRecordViewHolder) holder).tvPrice.setText("-" + content.getAmount() +
                        "元");
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stick_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
        textView.setText(generateHeaderContent(position));
    }

    @Override
    public long generateHeaderId(int position) {
//        Logger.d(position + " = " + getItemId(position));
        return getItemId(position);
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    private String getWeekOfDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d == null) {
            return null;
        }
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    @Override
    public long getItemId(int position) {
        if (customHeaderView != null) {
            position--;
        }
        String date = "0";
        if (position >= 0 && position < mData.size()) {
            String time = mData.get(position).getCrtTm();
            if (time != null && time.length() >= 8) {
                date = time.substring(0, 7).replace("-", "");
            }
        }
        return Long.valueOf(date);
    }

    private String getNowMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        return sdf.format(date);
    }

    private String getNowYear() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.CHINA);
        return sdf.format(date);
    }

    private String generateHeaderContent(int position) {
        if (customHeaderView != null) {
            position--;
        }
        String date = "";
        if (position < 0 || position >= mData.size()) {
            return date;
        }
        String time = mData.get(position).getCrtTm();
        if (time == null || time.length() < 10) {
            return date;
        }
        date = time.substring(0, 7);
        if (date.equals(getNowMonth())) {
            return "本月";
        }
        date = time.substring(0, 4);
        if (date.equals(getNowYear())) {
            return time.substring(5, 7) + "月";
        }
        return time.substring(0, 7);
    }

    private class RechargeRecordViewHolder extends UltimateRecyclerviewViewHolder {

        public TextView tvWeekday;
        public TextView tvDate;
        public TextView tvPrice;
        public TextView tvDescription;
        public ImageView imgPayIcon;

        public RechargeRecordViewHolder(View itemView) {
            super(itemView);
            tvWeekday = (TextView) itemView.findViewById(R.id.tv_weekday);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            imgPayIcon = (ImageView) itemView.findViewById(R.id.img_payment_mode);
        }
    }
}
