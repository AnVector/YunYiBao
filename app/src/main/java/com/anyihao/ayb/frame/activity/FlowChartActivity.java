package com.anyihao.ayb.frame.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UTabAdapter;
import com.anyihao.ayb.frame.fragment.DayChartFragment;
import com.anyihao.ayb.frame.fragment.MonthChartFragment;

import java.util.ArrayList;
import java.util.Arrays;
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
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"日报表", "月报表"};
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
        tabLayout.setupWithViewPager(viewpager);
        toolbar.inflateMenu(R.menu.flow_chart_toolbar_menu);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitle.setText(getString(R.string.data_flow_chart));
    }

    private void initViewPager() {
        DayChartFragment fragment1 = new DayChartFragment();
        MonthChartFragment fragment2 = new MonthChartFragment();
        mFragments.add(fragment1);
        mFragments.add(fragment2);
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
                return true;
            }
        });

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
