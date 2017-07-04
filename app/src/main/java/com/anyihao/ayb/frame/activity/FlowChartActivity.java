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
import com.bigkoo.pickerview.lib.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class FlowChartActivity extends ABaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private TimePickerView pvDay;
    private TimePickerView pvHour;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"日报表", "月报表"};
    private String[] cmdArray = new String[]{"DAYFLOW", "MONTHFLOW"};
    private String[] dateArray = new String[]{"day", "month"};
    private List<String> mTitles = Arrays.asList(mTitleArray);

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
        tabLayout.setupWithViewPager(viewpager);
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
            bundle = new Bundle();
            bundle.putString("cmd", cmdArray[i]);
            bundle.putString("date", dateArray[i]);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        mTabAdapter = new UTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewpager.setAdapter(mTabAdapter);
        viewpager.setCurrentItem(0, true);
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
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(intent);
                pvDay.show();
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
        pvDay = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.parseColor("#C8C8C8"))
                .setDividerType(WheelView.DividerType.WRAP)
                .setTextColorCenter(Color.parseColor("#333333"))
                .setLineSpacingMultiplier(1.2f)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentSize(16)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();
        pvHour = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.parseColor("#C8C8C8"))
                .setDividerType(WheelView.DividerType.WRAP)
                .setTextColorCenter(Color.parseColor("#333333"))
                .setLineSpacingMultiplier(1.2f)
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
