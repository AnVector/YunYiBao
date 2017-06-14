package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class AuthenticationActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_step_one)
    TextView tvStepOne;
    @BindView(R.id.tv_mobile_cert)
    TextView tvMobileCert;
    @BindView(R.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R.id.tv_user_cert)
    TextView tvUserCert;
    @BindView(R.id.tv_step_three)
    TextView tvStepThree;
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
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

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}