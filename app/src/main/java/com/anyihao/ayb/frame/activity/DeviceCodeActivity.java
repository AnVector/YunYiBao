package com.anyihao.ayb.frame.activity;

import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_code;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        setTextFont(tvDeviceNumHint);
        tvDeviceNum.setText("IEBox10000");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.scan_result));
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

    private void setTextFont(TextView tv) {
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/W12.ttc");
        tv.setTypeface(fontFace);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }
}
