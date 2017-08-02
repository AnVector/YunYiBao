package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class SysRecordDetailsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_data_amount)
    TextView tvDataAmount;
    @BindView(R.id.tv_expires)
    TextView tvExpires;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private String mAmount;
    private String mExpires;
    private String mDate;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sys_record_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mAmount = intent.getStringExtra("amount");
        mExpires = intent.getStringExtra("expires");
        mDate = intent.getStringExtra("date");
    }

    @Override
    protected void initData() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.record_details));
        tvDataAmount.setText(mAmount);
        tvExpires.setText(mExpires);
        tvDate.setText(mDate);
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
