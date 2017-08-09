package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.AliOrderBean;
import com.anyihao.ayb.bean.PackageListBean;
import com.anyihao.ayb.bean.PackageListBean.DataBean;
import com.anyihao.ayb.bean.PayResult;
import com.anyihao.ayb.bean.WxOrderBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
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
    private static final int SDK_PAY_FLAG = 0x0001;
    private static final int REQUEST_DEPOSIT_CODE = 0X0002;
    public static final int RESULT_DEPOSIT_CODE = 0X0003;
    private String topupType = "ALIPAY";
    private String packageID;
    private String pkgType;
    private IWXAPI wxApi;
    private boolean isWxPaySupported;
    private boolean isWxInstalled = true;
    private UHandler mHandler = new UHandler(this);

    private static class UHandler extends Handler {
        private final WeakReference<DepositActivity> mActivity;

        private UHandler(DepositActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    if (mActivity.get() != null) {
                        mActivity.get().handleAlipayResult((Map<String, String>) msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_deposit;
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
        getDepositInfo();
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderInfo();

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
        if (android.text.TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            Intent intent = new Intent(DepositActivity.this, AuthFinishActivity.class);
            startActivityForResult(intent, REQUEST_DEPOSIT_CODE);
        } else if (android.text.TextUtils.equals(resultStatus, "6001")) {
            ToastUtils.showToast(getApplicationContext(), "支付取消");
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            ToastUtils.showToast(getApplicationContext(), "支付失败");
        }
    }

    private void payByWx(String appId, String partnerId, String prepayId, String packageValue,
                         String nonceStr, String timeStamp, String sign) {
        PreferencesUtils.putString(getApplicationContext(), "payType", "deposit");
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
        if (TextUtils.isEmpty(orderInfo))
            return;
        ToastUtils.showToast(getApplicationContext(), "订单获取中...");
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(DepositActivity.this);
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

    private void getDepositInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAYINFO");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("flowType", "DEPOSIT");
        params.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType", ""));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void getOrderInfo() {
        if (TextUtils.isEmpty(topupType) || TextUtils.isEmpty(packageID))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAY");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("topupType", topupType);
        params.put("keyPackageID", packageID);
        int actionType = 1;
        if ("WXPAY".equals(topupType)) {
            actionType = 2;
        }

        if (actionType == 2 && !isWxInstalled) {
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DEPOSIT_CODE) {
            if (resultCode == AuthFinishActivity.RESULT_FINISH_CODE) {
                setResult(RESULT_DEPOSIT_CODE);
                finish();
            }
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            PackageListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    PackageListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans == null)
                    return;
                if (beans.size() > 0) {
                    DataBean dataBean = beans.get(0);
                    tvMoney.setText(dataBean.getPrice());
                    tvCashPledgeHint.setText(dataBean.getPkgDesc().replace("\\n", "\n"));
                    packageID = dataBean.getPackageID();
                    pkgType = dataBean.getPkgType();
                }
            }
        }

        if (actionType == 1) {
            AliOrderBean bean = GsonUtils.getInstance().transitionToBean(result,
                    AliOrderBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                payByAliPay(bean.getOrderInfo());
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 2) {
            WxOrderBean bean = GsonUtils.getInstance().transitionToBean(result,
                    WxOrderBean.class);
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
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
