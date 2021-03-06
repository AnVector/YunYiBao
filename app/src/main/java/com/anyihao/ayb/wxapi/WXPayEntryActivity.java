package com.anyihao.ayb.wxapi;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyihao.androidbase.manager.ActivityManager;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.ABaseActivity;
import com.anyihao.ayb.frame.activity.CertificationActivity;
import com.anyihao.ayb.frame.activity.DepositActivity;
import com.anyihao.ayb.frame.activity.PayActivity;
import com.anyihao.ayb.frame.activity.RechargeActivity;
import com.anyihao.ayb.frame.activity.RechargeRecordActivity;
import com.anyihao.ayb.frame.activity.ScanActivity;
import com.google.zxing.integration.android.IntentIntegrator;
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
    @BindView(R.id.rl_common)
    RelativeLayout rlCommon;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    @BindView(R.id.ll_deposit)
    LinearLayout llDeposit;
    @BindView(R.id.tv_step_four)
    TextView tvStepFour;
    private IWXAPI wxApi;
    public static final int RESULT_PAY_RESULT_CODE = 0x0007;

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
        toolbar.setNavigationIcon(R.drawable.ic_back);
        wxApi = WXAPIFactory.createWXAPI(getApplicationContext(), GlobalConsts.WX_APP_ID);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().finishActivity(PayActivity.class);
                ActivityManager.getInstance().finishActivity(RechargeActivity.class);
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
                Intent intent = new Intent(WXPayEntryActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
                ActivityManager.getInstance().finishActivity(PayActivity.class);
                ActivityManager.getInstance().finishActivity(RechargeActivity.class);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(WXPayEntryActivity.this);
                integrator
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setOrientationLocked(false)//扫描方向固定
                        .setCaptureActivity(ScanActivity.class) //
                        // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                ActivityManager.getInstance().finishActivity(DepositActivity.class);
                ActivityManager.getInstance().finishActivity(CertificationActivity.class);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.e(TAG, "onPayStart, openId = " + baseReq.openId);
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
                ToastUtils.showToast(getApplicationContext(), "支付失败");
                finish();
                break;
            case -2:
                ToastUtils.showToast(getApplicationContext(), "支付取消");
                finish();
                break;
            default:
                break;
        }
    }

    private void onPaySuccess() {
        String payType = PreferencesUtils.getString(getApplicationContext(), "payType", "common");
        if (payType.equals("common")) {
            llDeposit.setVisibility(View.GONE);
            rlCommon.setVisibility(View.VISIBLE);
            toolbarTitleMid.setText(getString(R.string.pay_succeed));
            icResult.setImageResource(R.drawable.ic_success);
            tvResult.setText(getString(R.string.payment_suceess));
            String amount = PreferencesUtils.getString(getApplicationContext(), "amount", "");
            String money = PreferencesUtils.getString(getApplicationContext(), "money", "");
            String expires = PreferencesUtils.getString(getApplicationContext(), "expires", "");
            tvPurchaseValue.setText(amount);
            tvFeeValue.setText(money);
            tvValidityValue.setText(expires);
        } else {
            llDeposit.setVisibility(View.VISIBLE);
            rlCommon.setVisibility(View.GONE);
            toolbarTitleMid.setText(getString(R.string.finished));
            tvStepFour.setBackground(getResources().getDrawable(R.drawable.ic_step_yes));
        }
    }
}