package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class DepositActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_mobile_cert)
    TextView tvMobileCert;
    @BindView(R.id.tv_user_cert)
    TextView tvUserCert;
    @BindView(R.id.tv_step_three)
    TextView tvStepThree;
    @BindView(R.id.tv_deposite)
    TextView tvDeposite;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_cash_pledge)
    TextView tvCashPledge;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_cash_pledge_hint)
    TextView tvCashPledgeHint;
    @BindView(R.id.rbt_alipay)
    RadioButton rbtAlipay;
    @BindView(R.id.rbt_wx)
    RadioButton rbtWx;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    private static final int REQUEST_DEPOSIT_CODE = 0X0002;
    public static final int RESULT_DEPOSIT_CODE = 0X0003;
    private String topupType = "ALIPAY";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_deposit;
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
        toolbarTitleMid.setText(getString(R.string.deposit));
        tvStepThree.setBackground(getResources().getDrawable(R.drawable.ic_step_yes));
        SpannableString spannableString = new SpannableString("押金（可退还）");
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#999999"));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#333333"));
        spannableString.setSpan(colorSpan1, 2, spannableString.length(), Spanned
                .SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvCashPledge.setText(spannableString);
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
                startActivityForResult(intent, REQUEST_DEPOSIT_CODE);
            }
        });

        rbtAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtAlipay.setChecked(true);
                rbtWx.setChecked(false);
                topupType = "ALIPAY";
            }
        });

        rbtWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtWx.setChecked(true);
                rbtAlipay.setChecked(false);
                topupType = "WXPAY";
            }
        });


    }

    private void getOrderInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAY");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("topupType", topupType);
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(1)
                        .createTask());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DEPOSIT_CODE) {
            if (resultCode == AuthenticationActivity.RESULT_FINISH_CODE) {
                Intent intent = new Intent();
                setResult(RESULT_DEPOSIT_CODE, intent);
                finish();
            }
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
