package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.ayb.R;

import butterknife.BindView;

public class DeviceCodeActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_device_num)
    TextView tvDeviceNum;
    @BindView(R.id.tv_device_num_hint)
    TextView tvDeviceNumHint;
    private String vid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_code;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        vid = intent.getStringExtra("vid");
    }

    @Override
    protected void initData() {
        tvDeviceNum.setText(vid);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setBackground(null);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbarTitleMid.setText(getString(R.string.scan_result));
        toolbarTitleMid.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTransparent(this);
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
