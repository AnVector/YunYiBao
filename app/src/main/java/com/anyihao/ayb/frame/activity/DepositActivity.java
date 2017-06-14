package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class DepositActivity extends ABaseActivity {

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
    @BindView(R.id.tv_cash_pledge)
    TextView tvCashPledge;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_cash_pledge_hint)
    TextView tvCashPledgeHint;
    @BindView(R.id.tv_ali_pay)
    TextView tvAliPay;
    @BindView(R.id.rbt_alipay)
    RadioButton rbtAlipay;
    @BindView(R.id.tv_wx_pay)
    TextView tvWxPay;
    @BindView(R.id.rbt_wx)
    RadioButton rbtWx;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_deposit;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitleMid.setText(getString(R.string.deposit));
        tvStepThree.setBackground(getResources().getDrawable(R.drawable.ic_step_yes));

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
                Intent intent = new Intent(DepositActivity.this, AuthenticationActivity.class);
                startActivity(intent);
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
