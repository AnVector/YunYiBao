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
import com.anyihao.ayb.frame.fragment.BriberyMoneyFragment;
import com.anyihao.ayb.frame.fragment.MeMessageFragment;
import com.anyihao.ayb.frame.fragment.SysMessageFragment;

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
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"未租设备", "已租设备"};
    private List<String> mTitles = Arrays.asList(mTitleArray);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_merchant_privilege;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        tabLayout.setupWithViewPager(viewpager);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitleMid.setText(getString(R.string.merchat_privilege));
        toolbarTitleRight.setText(getString(R.string.direction_for_use));
        toolbarTitleRight.setTextColor(getResources().getColor(R.color.light_gray));

    }

    private void initViewPager() {
        SysMessageFragment fragment1 = new SysMessageFragment();
        MeMessageFragment fragment2 = new MeMessageFragment();
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
