package com.anyihao.ayb.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.ayb.constant.GlobalConsts;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 必须在包名下 否则无法回调支付结果
 * 回调失败可能原因
 * 1.WXPayEntryActivity错误
 * 2.包名/key错误
 * 3.签名之类的错误
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();
    private IWXAPI wxApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxApi = WXAPIFactory.createWXAPI(this, GlobalConsts.WX_APP_ID);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.d(TAG, "onPayStart, openId = " + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.d(TAG, "onPayFinish, errCode = " + baseResp.errCode);
        finish();
    }
}