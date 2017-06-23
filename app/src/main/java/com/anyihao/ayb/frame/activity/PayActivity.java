package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.constant.GlobalConsts;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;

public class PayActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_pay_amount)
    TextView tvPayAmount;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.rbt_alipay)
    RadioButton rbtAlipay;
    @BindView(R.id.rbt_wx)
    RadioButton rbtWx;
    @BindView(R.id.btn_confirm_to_pay)
    AppCompatButton btnConfirmToPay;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private IWXAPI wxApi;
    private boolean isWxPaySupported;
    private String money;
    private String amount;
    private String expires;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        money = intent.getStringExtra("money");
        amount = intent.getStringExtra("amount");
        expires = intent.getStringExtra("expires");

    }

    @Override
    protected void initData() {

        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.pay));
        tvPayAmount.setText(String.format(tvPayAmount.getText().toString(), money));
        tvAmount.setText(String.format(tvAmount.getText().toString(), amount));
        tvValidity.setText(String.format(tvValidity.getText().toString(), expires));

        wxApi = WXAPIFactory.createWXAPI(this, GlobalConsts.WX_APP_ID);
        isWxPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rbtAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtWx.setChecked(false);
                rbtAlipay.setChecked(true);
            }
        });

        rbtWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtWx.setChecked(true);
                rbtAlipay.setChecked(false);
            }
        });

        btnConfirmToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWxPaySupported) {
                    ToastUtils.showLongToast(PayActivity.this, "获取订单中...");
                    PayReq request = new PayReq();
                    request.appId = GlobalConsts.WX_APP_ID;
                    request.partnerId = "1400678202";
                    request.prepayId = "wx20170325141040aef727979f0063920600";
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = "K9SDVAEFZ26TQE6AV3H2LOH9LXZ5WYAZ";
                    request.timeStamp = "1490422239";
                    request.sign = "1779AB4E59398A8CAE06DE6EA59243AA";
                    wxApi.sendReq(request);
                }
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
