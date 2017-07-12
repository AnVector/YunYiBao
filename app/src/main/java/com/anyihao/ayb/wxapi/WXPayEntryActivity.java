package com.anyihao.ayb.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.ABaseActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;


/**
 * 必须在包名下 否则无法回调支付结果
 * 回调失败可能原因
 * 1.WXPayEntryActivity错误
 * 2.包名/key错误
 * 3.签名之类的错误
 */
public class WXPayEntryActivity extends ABaseActivity implements IWXAPIEventHandler {

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();
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
    private IWXAPI wxApi;
    private Bundle mBundle = new Bundle();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        wxApi = WXAPIFactory.createWXAPI(this, GlobalConsts.WX_APP_ID);
        wxApi.handleIntent(getIntent(), this);
        toolbarTitleMid.setText(getString(R.string.pay_succeed));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        tvResult.setText(getString(R.string.payment_suceess));
    }

    @Override
    protected void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.e(TAG, "onPayStart, openId = " + baseReq.openId);
        baseReq.toBundle(mBundle);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Logger.e(TAG, "onPayFinish, errCode = " + baseResp.errCode);
        int errCode = baseResp.errCode;
        switch (errCode) {
            case 0:
                onPaySuccess();
                break;
            case -1:
                onPayFailure();
                break;
            case -2:
                finish();
                break;
            default:
                break;
        }
    }

    private void onPaySuccess() {
    }

    private void onPayFailure() {

    }
}