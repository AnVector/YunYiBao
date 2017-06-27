package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class PayResultActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_purchase_value)
    TextView tvPurchaseValue;
    @BindView(R.id.tv_validity_value)
    TextView tvValidityValue;
    @BindView(R.id.btn_back)
    AppCompatButton btnBack;
    @BindView(R.id.btn_to_recharge_record)
    AppCompatButton btnToRechargeRecord;
    @BindView(R.id.ic_result)
    ImageView icResult;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.tv_fee_value)
    TextView tvFeeValue;
    @BindView(R.id.tv_fee)
    TextView tvFee;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay_result;
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
        titleMid.setText(getString(R.string.pay_succeed));
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
