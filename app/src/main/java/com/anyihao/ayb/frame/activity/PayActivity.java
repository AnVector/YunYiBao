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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.AliOrderInfoBean;
import com.anyihao.ayb.bean.WxOrderInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
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

        wxApi = WXAPIFactory.createWXAPI(this, null);
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

        rbtWx.setOnClickListener(new View.OnClickListener() {
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
            ToastUtils.showToast(PayActivity.this, "订单获取中...");
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

    private void payByAliPay(String orderInfo) {
        if (StringUtils.isEmpty(orderInfo))
            return;

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

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(getApplicationContext(), error);
        }
    }
}
