package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;


/**
 * 必须在包名下 否则无法回调支付结果
 * 回调失败可能原因
 * 1.WXPayEntryActivity错误
 * 2.包名/key错误
 * 3.签名之类的错误
 */
public class PayResultActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ic_result)
    ImageView icResult;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.tv_purchase_value)
    TextView tvPurchaseValue;
    @BindView(R.id.tv_validity_value)
    TextView tvValidityValue;
    @BindView(R.id.tv_fee_value)
    TextView tvFeeValue;
    @BindView(R.id.btn_back)
    AppCompatButton btnBack;
    @BindView(R.id.btn_to_recharge_record)
    AppCompatButton btnToRechargeRecord;
    private String amount;
    private String money;
    private String expires;
    public static final int RESULT_GO_TO_HOME_CODE = 0x0008;
    public static final int RESULT_GO_TO_RECORD_CODE = 0x0009;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        amount = intent.getStringExtra("amount");
        money = intent.getStringExtra("money");
        expires = intent.getStringExtra("expires");
    }

    @Override
    protected void initData() {
        toolbarTitleMid.setText(getString(R.string.pay_succeed));
        icResult.setImageResource(R.drawable.ic_success);
        tvResult.setText(getString(R.string.payment_suceess));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        tvPurchaseValue.setText(amount);
        tvFeeValue.setText(money);
        tvValidityValue.setText(expires.replace("全国流量，即时生效，", ""));
    }

    @Override
    protected void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_GO_TO_HOME_CODE);
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnToRechargeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayResultActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
                setResult(RESULT_GO_TO_RECORD_CODE);
                finish();
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }
}