package com.anyihao.ayb.frame.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.anyihao.ayb.R;
import com.anyihao.ayb.alipay.AuthResult;
import com.anyihao.ayb.alipay.PayResult;
import com.anyihao.ayb.alipay.SignUtils;
import com.anyihao.ayb.constant.GlobalConsts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

public class AliPayActivity extends ABaseActivity {

    @BindView(R.id.ali_pay_btn)
    Button aliPayBtn;
    private String orderInfo;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(AliPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(AliPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult
                            .getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(AliPayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode())
                                , Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(AliPayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()),
                                Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ali_pay;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        Map<String, String> params = buildOrderParamMap();
        String orderParam = buildOrderParam(params);
        String sign = getSign(params, GlobalConsts.RSA_PRIVATE, false);
        orderInfo = orderParam + "&" + sign;
        orderInfo = "app_id=2016101702214300&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%222088421685287993%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%224.0%22%2C%22subject%22%3A%22100%22%2C%22body%22%3A%22100M%E6%B5%81%E9%87%8F%EF%BC%8C%E6%9C%89%E6%95%88%E6%9C%9F%E4%B8%80%E5%B9%B4%22%2C%22out_trade_no%22%3A%22AYB1703251710247187434%22%7D&charset=utf-8&format=JSON&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F180.153.57.53%3A8099%2Fvrsws%2Fnotify_url%2FALIPAY&sign_type=RSA&timestamp=2017-03-25+17%3A10%3A24&version=1.0&sign=KxTDhXbHIVDru3vp1geHOIRGW7S8j0SpwfptFTppzdUR3BoVl8%2BioHBKbr9D%2BOj58VaJaL9Wy1n%2BZfiJyxbw3H0DPa57pP2CRqgl1%2Fp5oIYx5OjIuyUbm0zLaJQGmgr2dMGsHavUlhb9VdB01NzCUjEaPtL9KXJshhU1hwTejDA%3D";
    }

    @Override
    protected void initEvent() {

        aliPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(AliPayActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        });

    }


    /**
     * 构造支付订单参数列表
     */
    public Map<String, String> buildOrderParamMap() {

        Map<String, String> keyValues = new HashMap<>();
        keyValues.put("app_id", GlobalConsts.ALIPAY_APP_ID);
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("charset", "utf-8");
        keyValues.put("sign_type", "RSA");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        keyValues.put("timestamp", sdf.format(new Date()));
        keyValues.put("version", "1.0");
        keyValues.put("notify_url", "");
        return keyValues;
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public String buildOrderParam(Map<String, String> map) {

        List<String> keys = new ArrayList<>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    public String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
