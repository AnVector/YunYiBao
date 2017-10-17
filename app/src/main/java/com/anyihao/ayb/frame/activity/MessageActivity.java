package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UTabAdapter;
import com.anyihao.ayb.frame.fragment.EnvelopeFragment;
import com.anyihao.ayb.frame.fragment.MessageFragment;

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
    private String[] mMessageArray = new String[]{"PERSON", "SYSTEM"};
    private List<String> mTitles = Arrays.asList(mTitleArray);
    private int index = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String action = intent.getStringExtra("action");
        if (!TextUtils.isEmpty(action)) {
            index = intent.getIntExtra("index", 0);
        }
    }

    @Override
    protected void initData() {
        initViewPager();
        tabLayout.setupWithViewPager(viewpager);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.message_of_mine));
    }

    private void initViewPager() {
        Bundle bundle;
        EnvelopeFragment envelopeFragment = new EnvelopeFragment();
        bundle = new Bundle();
        bundle.putString("type", "REDPACKAGE");
        envelopeFragment.setArguments(bundle);
        mFragments.add(envelopeFragment);

        MessageFragment fragment;
        for (int i = 0; i < 2; ++i) {
            fragment = new MessageFragment();
            bundle = new Bundle();
            bundle.putString("type", mMessageArray[i]);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }

        mTabAdapter = new UTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewpager.setAdapter(mTabAdapter);
        viewpager.setCurrentItem(index, true);
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
}
