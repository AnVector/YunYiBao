package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.AliOrderInfoBean;
import com.anyihao.ayb.bean.PayResult;
import com.anyihao.ayb.bean.WxOrderInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.rl_ali_pay)
    RelativeLayout rlAliPay;
    @BindView(R.id.rl_wx_pay)
    RelativeLayout rlWxPay;
    private IWXAPI wxApi;
    private boolean isWxPaySupported;
    private boolean isWxInstalled = true;
    private String money;
    private String amount;
    private String expires;
    private String packageID;
    private String topupType = "ALIPAY";

    private static final int SDK_PAY_FLAG = 0x0001;
    private static final int REQUEST_PAY_RESULT_CODE = 0x0007;
    public static final int RESULT_PAY_CODE = 0x0010;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    handleAlipayResult((Map<String, String>) msg.obj);
                    break;
                default:
                    break;
            }
        }

    };

    private void handleAlipayResult(Map<String, String> result) {
        if (result == null)
            return;
        PayResult payResult = new PayResult(result);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        Logger.e(resultInfo);
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            Intent intent = new Intent(PayActivity.this, PayResultActivity.class);
            intent.putExtra("amount", amount);
            intent.putExtra("money", money);
            intent.putExtra("expires", expires);
            startActivityForResult(intent, REQUEST_PAY_RESULT_CODE);
//            ToastUtils.showToast(PayActivity.this, "支付成功");
        } else if (TextUtils.equals(resultStatus, "6001")) {
            ToastUtils.showToast(getApplicationContext(), "支付取消");
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            ToastUtils.showToast(getApplicationContext(), "支付失败");
        }
    }

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
        packageID = intent.getStringExtra("packageID");
        PreferencesUtils.putString(getApplicationContext(), "payType", "common");
        PreferencesUtils.putString(getApplicationContext(), "money", money);
        PreferencesUtils.putString(getApplicationContext(), "amount", amount);
        if (expires == null) {
            expires = "";
        }
        PreferencesUtils.putString(getApplicationContext(), "expires", expires.replace
                ("全国流量，即时生效，", ""));

    }

    @Override
    protected void initData() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.pay));
        SpannableString spannableString = new SpannableString(String.format(tvPayAmount.getText()
                .toString(), money));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ff1919"));
        spannableString.setSpan(colorSpan, 5, spannableString.length(), Spanned
                .SPAN_INCLUSIVE_EXCLUSIVE);
        tvPayAmount.setText(spannableString);
        tvAmount.setText(String.format(tvAmount.getText().toString(), amount));
        tvValidity.setText(String.format(tvValidity.getText().toString(), expires.replace
                ("全国流量，即时生效，", "")));
        wxApi = WXAPIFactory.createWXAPI(getApplicationContext(), null);
        wxApi.registerApp(GlobalConsts.WX_APP_ID);
        isWxPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        isWxInstalled = wxApi.isWXAppSupportAPI();
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
                topupType = "ALIPAY";
            }
        });

        rlAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtWx.setChecked(false);
                rbtAlipay.setChecked(true);
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

        rlWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtWx.setChecked(true);
                rbtAlipay.setChecked(false);
                topupType = "WXPAY";
            }
        });

        btnConfirmToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderInfo();
            }
        });
    }


    private void payByWx(String appId, String partnerId, String prepayId, String packageValue,
                         String nonceStr, String timeStamp, String sign) {
        if (isWxPaySupported) {
            ToastUtils.showToast(getApplicationContext(), "订单获取中...");
            PayReq request = new PayReq();
            request.appId = appId;
            request.partnerId = partnerId;
            request.prepayId = prepayId;
            request.packageValue = packageValue;
            request.nonceStr = nonceStr;
            request.timeStamp = timeStamp;
            request.sign = sign;
            wxApi.sendReq(request);
        }
    }

    private void payByAliPay(final String orderInfo) {
        if (StringUtils.isEmpty(orderInfo))
            return;
        ToastUtils.showToast(getApplicationContext(), "订单获取中...");
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, false);
                Logger.e(result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    private void getOrderInfo() {
        if (StringUtils.isEmpty(topupType) || StringUtils.isEmpty(packageID))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAY");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("topupType", topupType);
        params.put("keyPackageID", packageID);
        int actionType = 0;
        if ("WXPAY".equals(topupType)) {
            actionType = 1;
        }
        if (actionType == 1 && !isWxInstalled) {
            ToastUtils.showToast(getApplicationContext(), "微信客户端未安装");
            return;
        }
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(actionType)
                        .createTask());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == WXPayEntryActivity.RESULT_PAY_RESULT_CODE) {
            this.finish();
        }

        if (requestCode == REQUEST_PAY_RESULT_CODE) {
            if (resultCode == PayResultActivity.RESULT_GO_TO_HOME_CODE || resultCode ==
                    PayResultActivity.RESULT_GO_TO_RECORD_CODE) {
                setResult(RESULT_PAY_CODE);
                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            AliOrderInfoBean bean = GsonUtils.getInstance().transitionToBean(result,
                    AliOrderInfoBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                payByAliPay(bean.getOrderInfo());
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 1) {
            WxOrderInfoBean bean = GsonUtils.getInstance().transitionToBean(result,
                    WxOrderInfoBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                payByWx(bean.getAppid(), bean.getPartnerid(), bean.getPrepayid(), bean
                        .getPackageX(), bean.getNoncestr(), bean.getTimestamp(), bean.getSign());
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }
}
