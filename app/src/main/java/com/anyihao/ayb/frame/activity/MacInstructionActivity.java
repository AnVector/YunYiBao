package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.ClipboardUtils;
import com.anyihao.androidbase.utils.DeviceUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;

import butterknife.BindView;

public class MacInstructionActivity extends ABaseActivity {

    @BindView(R.id.tv_question0)
    TextView tvQuestion0;
    @BindView(R.id.tv_answer0)
    TextView tvAnswer0;
    @BindView(R.id.tv_question1)
    TextView tvQuestion1;
    @BindView(R.id.tv_answer1)
    TextView tvAnswer1;
    @BindView(R.id.tv_question2)
    TextView tvQuestion2;
    @BindView(R.id.tv_answer2)
    TextView tvAnswer2;
    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mac_instruction;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.check_mac_address));
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvAnswer0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(MacInstructionActivity.this, DeviceUtils.getMacAddress(MacInstructionActivity.this));
                ToastUtils.showToast(MacInstructionActivity.this, "Mac地址复制成功");
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }
}
