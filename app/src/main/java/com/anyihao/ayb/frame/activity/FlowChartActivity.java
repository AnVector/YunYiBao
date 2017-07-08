package com.anyihao.ayb.frame.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UTabAdapter;
import com.anyihao.ayb.frame.fragment.ChartFragment;
import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

public class FlowChartActivity extends ABaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private TimePickerView mPvMonth;
    private TimePickerView mPvDay;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"日报表", "月报表"};
    private String[] cmdArray = new String[]{"DAYFLOW", "MONTHFLOW"};
    private List<String> mTitles = Arrays.asList(mTitleArray);
    private Date mDate = new Date();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_flow_chart;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        initTimePicker();
        tabLayout.setupWithViewPager(mViewpager);
        toolbar.inflateMenu(R.menu.flow_chart_toolbar_menu);
        toolbar.setNavigationIcon(R.drawable.ic_back);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
        toolbarTitle.setText(getString(R.string.data_flow_chart));
    }

    private void initViewPager() {
        ChartFragment fragment;
        Bundle bundle;
        for (int i = 0; i < 2; i++) {
            fragment = new ChartFragment();
            SimpleDateFormat sdf;
            bundle = new Bundle();
            bundle.putString("cmd", cmdArray[i]);
            if (cmdArray[i].contains("DAY")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            } else {
                sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
            }
            bundle.putString("time", sdf.format(mDate));
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        mTabAdapter = new UTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewpager.setAdapter(mTabAdapter);
        mViewpager.setCurrentItem(0, true);
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ChartFragment fragment = (ChartFragment) mFragments.get(mViewpager
                        .getCurrentItem());
                if (fragment == null)
                    return true;
                Bundle bundle = fragment.getArguments();
                if (bundle == null)
                    return true;
                String cmd = bundle.getString("cmd");
                if ("DAYFLOW".equals(cmd)) {
                    mPvDay.show();
                } else {
                    mPvMonth.show();
                }
                return true;
            }
        });

    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 1);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 1, 1);
        //时间选择器
        mPvMonth = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                String time = sdf.format(date);
                ChartFragment fragment = (ChartFragment) mFragments.get(mViewpager
                        .getCurrentItem());
                if (fragment == null)
                    return;
                if (fragment.isVisible() && !fragment.isDetached()) {
                    fragment.queryByTime(time);
                }
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH)
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.parseColor("#C8C8C8"))
                .setTextColorCenter(Color.parseColor("#333333"))
                .setLineSpacingMultiplier(1.5f)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentSize(16)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();
        mPvDay = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                String time = sdf.format(date);
                ChartFragment fragment = (ChartFragment) mFragments.get(mViewpager
                        .getCurrentItem());
                if (fragment == null)
                    return;
                if (fragment.isVisible() && !fragment.isDetached()) {
                    fragment.queryByTime(time);
                }

            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setLabel("", "", "", "", "", "")
                .setDividerColor(Color.parseColor("#C8C8C8"))
                .setTextColorCenter(Color.parseColor("#333333"))
                .setLineSpacingMultiplier(1.5f)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentSize(16)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
