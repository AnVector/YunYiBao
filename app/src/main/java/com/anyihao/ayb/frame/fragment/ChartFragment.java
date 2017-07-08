package com.anyihao.ayb.frame.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.DailyUsageBean;
import com.anyihao.ayb.bean.DailyUsageBean.DataBean;
import com.anyihao.ayb.bean.MonthlyUsageBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by Admin on 2017/4/17.
 */

public class ChartFragment extends ABaseFragment {

    @BindView(R.id.tv_data_amount)
    TextView tvDataAmount;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_unit_hint)
    TextView tvUnitHint;
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.progressbar_circular)
    CircularProgressBar progressbarCircular;
    private Typeface mTf;
    private String cmd;
    private String time;
    private LineDataSet mLineDataSet;
    private List<DailyUsageBean.DataBean> mDayBeans = new ArrayList<>();
    private List<MonthlyUsageBean.DataBean> mMonthBeans = new ArrayList<>();

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            cmd = bundle.getString("cmd");
            time = bundle.getString("time");
        }
        if ("DAYFLOW".equals(cmd)) {
            tvDate.setText(time.substring(5));
            tvUnitHint.setText("以小时为统计单位");
        } else {
            tvDate.setText(time);
            tvUnitHint.setText("以天为统计单位");
        }

        showProgressBar();
        getFlowChart();
        initChart();
    }

    private void initChart() {
        mTf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setNoDataText("您还没有开始使用哦");
        chart.setNoDataTextColor(Color.parseColor("#b5b5b5"));
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#b5b5b5"));
        xAxis.setTextSize(10f);
//        if ("DAYFLOW".equals(cmd)) {
//            xAxis.setLabelCount(12, false);
//        } else {
//            xAxis.setLabelCount(15, false);
//        }
//        xAxis.setAxisLineColor(Color.parseColor("#F5F5F5"));

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(3, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setTextColor(Color.parseColor("#b5b5b5"));
//        leftAxis.setAxisLineColor(Color.parseColor("#F5F5F5"));
        leftAxis.setTextSize(10f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(3, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        chart.animateX(750);
    }

    @Override
    protected void initEvent() {

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float value = e.getY();
                DecimalFormat df = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                if (value > 1024) {
                    value = value / 1024;
                    tvDataAmount.setText(String.valueOf(df.format(value) + "G"));
                } else {
                    tvDataAmount.setText(String.valueOf(e.getY() + "M"));
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public void queryByTime(String time) {

        this.time = time;
        getFlowChart();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_chart;
    }

    private void generateDayLine() {
        ArrayList<Entry> e = new ArrayList<>();
        float value;
        float hour;
        Double traffic;
        for (int i = 0; i < mDayBeans.size(); i++) {
            traffic = mDayBeans.get(i).getDataTraffic();
            value = new BigDecimal(traffic).setScale(1, RoundingMode.HALF_UP).floatValue();
            hour = Float.parseFloat(mDayBeans.get(i).getTime());
            e.add(new Entry(hour, value));
        }

        mLineDataSet = new LineDataSet(e, "M/小时");
        mLineDataSet.setLineWidth(1.0f);
        mLineDataSet.setDrawCircles(true);
        mLineDataSet.setCircleRadius(2.5f);
        mLineDataSet.setDrawValues(false);
        mLineDataSet.setValueTextColor(Color.parseColor("#000000"));
        mLineDataSet.setColor(Color.parseColor("#A8DEFF"));
        mLineDataSet.setCircleColor(Color.parseColor("#A8DEFF"));
        mLineDataSet.setHighlightEnabled(true);
        mLineDataSet.setHighLightColor(Color.parseColor("#F5F5F5"));
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(mLineDataSet);
        chart.setData(new LineData(sets));
        chart.invalidate();
    }

    private void generateMonthLine() {

        ArrayList<Entry> e = new ArrayList<>();
        float value;
        Double traffic;
        float day;
        for (int i = 0; i < mMonthBeans.size(); i++) {
            traffic = mMonthBeans.get(i).getDataTraffic();
            value = new BigDecimal(traffic).setScale(1, RoundingMode.HALF_UP).floatValue();
            day = Float.parseFloat(mMonthBeans.get(i).getDay());
            e.add(new Entry(day, value));
        }

        mLineDataSet = new LineDataSet(e, "M/日");
        mLineDataSet.setLineWidth(1.0f);
        mLineDataSet.setDrawCircles(true);
        mLineDataSet.setCircleRadius(2.5f);
        mLineDataSet.setDrawValues(false);
        mLineDataSet.setColor(Color.parseColor("#94F1C4"));
        mLineDataSet.setCircleColor(Color.parseColor("#94F1C4"));
        mLineDataSet.setHighlightEnabled(true);
        mLineDataSet.setHighLightColor(Color.parseColor("#F5F5F5"));
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(mLineDataSet);
        chart.setData(new LineData(sets));
        chart.invalidate();
    }

    private void getFlowChart() {
        if (StringUtils.isEmpty(time) || StringUtils.isEmpty(cmd))
            return;
        int actionType = 0;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", cmd);
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        if ("DAYFLOW".equals(cmd)) {
            params.put("day", time);
        } else {
            params.put("month", time);
            actionType = 1;
        }

        postForm(params, 1, actionType);
    }

    private void postForm(Map<String, String> params, int page, int actionType) {

        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(page)
                        .setActionType(actionType)
                        .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        hideProgressBar();
        if (actionType == 0) {
            DailyUsageBean bean = GsonUtils.getInstance().transitionToBean(result, DailyUsageBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans == null)
                    return;
                if (beans.size() > 0) {
                    mDayBeans.clear();
                    mDayBeans.addAll(beans);
                    generateDayLine();
                } else {
//                    ToastUtils.showToast(mContext.getApplicationContext(), "");
                    chart.clear();
                }
            }
        }

        if (actionType == 1) {
            MonthlyUsageBean bean = GsonUtils.getInstance().transitionToBean(result,
                    MonthlyUsageBean
                            .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<MonthlyUsageBean.DataBean> beans = bean.getData();
                if (beans == null)
                    return;
                if (beans.size() > 0) {
                    mMonthBeans.clear();
                    mMonthBeans.addAll(beans);
                    generateMonthLine();
                } else {
//                    ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
                    chart.clear();
                }
            }
        }
    }

    private void showProgressBar() {
        progressbarCircular.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).start();
    }

    private void hideProgressBar() {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        hideProgressBar();
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(mContext.getApplicationContext(), error);
        }
    }
}
