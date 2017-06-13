package com.anyihao.ayb.frame.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Admin on 2017/4/17.
 */

public class DayChartFragment extends ABaseFragment {

    @BindView(R.id.tv_data_amount)
    TextView tvDataAmount;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_unit_hint)
    TextView tvUnitHint;
    @BindView(R.id.chart)
    LineChart chart;
    private Typeface mTf;


    @Override
    protected void initData() {
        mTf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12, false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);

        // set data
        chart.setData(generateDataLine());
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(750);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_day_chart;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine() {

        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 24; i++) {
            e.add(new Entry(i, (int) (Math.random() * 40)));
        }

        LineDataSet d = new LineDataSet(e, null);
        d.setLineWidth(1.5f);
        d.setCircleRadius(3.5f);
        d.setDrawValues(true);
        d.setColor(Color.parseColor("#A8DEFF"));
        d.setCircleColor(Color.parseColor("#A8DEFF"));
        d.setHighlightEnabled(false);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d);
        return new LineData(sets);
    }


    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
