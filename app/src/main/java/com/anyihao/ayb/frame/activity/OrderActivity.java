package com.anyihao.ayb.frame.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.anyihao.ayb.R;
import com.anyihao.ayb.alipay.AuthResult;
import com.anyihao.ayb.alipay.H5PayDemoActivity;
import com.anyihao.ayb.alipay.PayResult;
import com.anyihao.ayb.util.OrderInfoUtil2_0;

import java.util.Map;

public class OrderActivity extends ABaseActivity {


    // 商户PID
    public static final String PARTNER = "2088421685287993";
    // 商户收款账号
    public static final String SELLER = "18958100066@189.cn";

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALtILnky5MQVw4eMTwkflSMpDdq5q0xs5VIei69ybmBI0wpSFyE28lSW/pP2OmtQ0qKGjCyqZG6aIVNBDOHTjwY4350V8wsfa8j5N/9makF/10eejZpa9zmf6ITRs+fdbGh6Y4Au/cXUXpsJksvryrDtr/HMz3rzeiudHjLTFPRDAgMBAAECgYEAqo8ZK+2qo92CPh8NF6z4oJXR9UGkfKyryDbjVfwsA2ovMLYENI6a8Wi2HsBfAb8QpU1NuZvQbV3XPb//YGbQV/XWz/tJsEMxDiGbW2jD9S9UdwgjJO0878CxYYqj81jC/CYnyD754iSLhuFbJM3FMdsoP/vWUPnJTC1/uBN3YakCQQDn3wTKXfnrQVm74xH60+kdZk4vNS9ywBZSn/lyiMGKa661EyuJQ18WizlVqUmt3a32l9ckIQ5xKmWQbTIzQ/K9AkEAzsVRsvl5g1JBAgXpeCp32mGb47gCqT3991PZhl+2tlj2kcD4tJgJLxiOSPp2lZIvdshhZVGDKOcCrH1e/WZy/wJAUjPMfPnoGjEm4OdVfnkWEegtG6tdUO8sespgIuy8wJgAbg2Hx7fsxA9DmkzT5CHNBLk7+oEFn7UKILO1slsKeQJAfXdADuDQef30UlzyASeL2Gh4JmKmwrlKHMS1bpMvlFBBNcopX7QNhpVY6TGJuVKeGG6YotkmRDCA79eXRx3eUwJBALpnMTcw2aNlWzIww2JHB4aP9WtvhZxxD6LiRE3iV8I0AqspAQc2dAowNm+fqtLU6hKG+Cw8kvC/5DAIpPZI+Xk=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private String tradeNO;
    private String productName;
    private String productDescription;
    private String amount;
    private String notifyURL;
    private String payInfo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

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
                        Toast.makeText(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(OrderActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(OrderActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderActivity.this);
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

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(OrderActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private String getOrderInfo(String subject, String body, String price) {

        StringBuilder orderInfo = new StringBuilder();

        orderInfo.append("partner=" + "\"" + PARTNER + "\"")  // 签约合作者身份ID
                .append("&seller_id=" + "\"" + SELLER + "\"")// 签约卖家支付宝账号
                .append("&out_trade_no=" + "\"" + tradeNO + "\"")// 商户网站唯一订单号
                .append("&subject=" + "\"" + subject + "\"")// 商品名称
                .append("&body=" + "\"" + body + "\"")// 商品详情
                .append("&total_fee=" + "\"" + price + "\"")// 商品金额
                .append("&notify_url=" + "\"" + notifyURL + "\"")// 服务器异步通知页面路径
                .append("&service=\"mobile.securitypay.pay\"")// 服务接口名称， 固定值
                .append("&payment_type=\"1\"")// 支付类型， 固定值
                .append("&_input_charset=\"utf-8\"")// 参数编码， 固定值
                // 设置未付款交易的超时时间
                // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
                // 取值范围：1m～15d。
                // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
                // 该参数数值不接受小数点，如1.5h，可转换为90m。
                .append("&it_b_pay=\"30m\"")
                // extern_token为经过快登授权获取到的TPAlipay_open_id,带上此参数用户将使用授权的账户进行支付
                // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
                // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
                .append("&return_url=\"m.alipay.com\"");
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo.toString();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
