package com.anyihao.ayb.frame.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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

public class MessageActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"红包消息", "个人消息", "系统消息"};
    private List<String> mTitles = Arrays.asList(mTitleArray);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        tabLayout.setupWithViewPager(viewpager);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.message));
    }

    private void initViewPager() {
        SysMessageFragment fragment1 = new SysMessageFragment();
        MeMessageFragment fragment2 = new MeMessageFragment();
        BriberyMoneyFragment fragment3 = new BriberyMoneyFragment();
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

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
