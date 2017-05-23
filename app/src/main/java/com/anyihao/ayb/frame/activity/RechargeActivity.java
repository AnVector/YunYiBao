package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UTabAdapter;
import com.anyihao.ayb.frame.fragment.DayFragment;
import com.anyihao.ayb.frame.fragment.DaysFragment;
import com.anyihao.ayb.frame.fragment.MeMessageFragment;
import com.anyihao.ayb.frame.fragment.MonthFragment;
import com.anyihao.ayb.frame.fragment.PackageFragment;
import com.anyihao.ayb.frame.fragment.SeasonFragment;
import com.anyihao.ayb.frame.fragment.SysMessageFragment;
import com.anyihao.ayb.frame.fragment.YearFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.btn_confirm_to_recharge)
    AppCompatButton btnConfirmToRecharge;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"超值包", "月包", "3天包","日包","季包"};
    private List<String> mTitles = Arrays.asList(mTitleArray);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.data_flow_charge));
        tabLayout.setupWithViewPager(viewpager);
    }

    private void initViewPager() {
        PackageFragment fragment1 = new PackageFragment();
        MonthFragment fragment2 = new MonthFragment();
//        YearFragment fragment3 = new YearFragment();
        DaysFragment fragment3 = new DaysFragment();
        DayFragment fragment4 = new DayFragment();
        SeasonFragment fragment5 = new SeasonFragment();
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(fragment4);
        mFragments.add(fragment5);
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

        btnConfirmToRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeActivity.this, PayActivity.class);
                startActivity(intent);
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
