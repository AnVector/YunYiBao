package com.anyihao.ayb.frame.activity;

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
import com.anyihao.ayb.frame.fragment.NotRentFragment;
import com.anyihao.ayb.frame.fragment.RentedFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class MerchantPrivilegeActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"待取设备", "已租设备", "未租设备"};
    private List<String> mTitles = Arrays.asList(mTitleArray);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_merchant_privilege;
    }

    @Override
    protected void initData() {
        initViewPager();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.merchat_privilege));
        tabLayout.setupWithViewPager(viewpager);
    }

    private void initViewPager() {
        Bundle bundle;
        RentedFragment fragment1 = new RentedFragment();
        bundle = new Bundle();
        bundle.putString("status", "2");
        fragment1.setArguments(bundle);
        RentedFragment fragment2 = new RentedFragment();
        bundle = new Bundle();
        bundle.putString("status", "1");
        fragment2.setArguments(bundle);
        NotRentFragment fragment3 = new NotRentFragment();
        bundle = new Bundle();
        bundle.putString("status", "0");
        fragment3.setArguments(bundle);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
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
                return true;
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }
}
