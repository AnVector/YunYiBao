package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.google.zxing.integration.android.IntentIntegrator;

import butterknife.BindView;

public class AuthenticationActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_mobile_cert)
    TextView tvMobileCert;
    @BindView(R.id.tv_user_cert)
    TextView tvUserCert;
    @BindView(R.id.tv_deposite)
    TextView tvDeposite;
    @BindView(R.id.tv_step_four)
    TextView tvStepFour;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.imv_finished)
    ImageView imvFinished;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    public static final int RESULT_FINISH_CODE = 0X0004;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_authentication;
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
        toolbarTitleMid.setText(getString(R.string.finished));
        tvStepFour.setBackground(getResources().getDrawable(R.drawable.ic_step_yes));

    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(AuthenticationActivity.this);
                integrator
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setOrientationLocked(false)//扫描方向固定
                        .setCaptureActivity(ScanActivity.class) //
                        // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                Intent intent = new Intent();
                setResult(RESULT_FINISH_CODE, intent);
                finish();
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
