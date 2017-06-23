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
import com.anyihao.ayb.frame.fragment.PackageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

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
    ViewPager mViewpager;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"超值包", "月包", "3天包", "日包", "季包"};
    private String[] mFlowTypes = new String[]{"VIP", "MONTH", "THREE", "DAY", "SEASON"};
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
        tabLayout.setupWithViewPager(mViewpager);
    }

    private void initViewPager() {
        PackageFragment fragment;
        for (int i = 0; i < 5; ++i) {
            fragment = new PackageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("flowType", mFlowTypes[i]);
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

        btnConfirmToRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageFragment fragment = (PackageFragment) mFragments.get(mViewpager
                        .getCurrentItem());
                if (fragment.isVisible() && !fragment.isDetached()) {
                    Intent intent = new Intent(RechargeActivity.this, PayActivity.class);
                    intent.putExtra("money", fragment.getMoney());
                    intent.putExtra("amount", fragment.getAmount());
                    intent.putExtra("expires", fragment.getExpires());
                    startActivity(intent);
                }
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
