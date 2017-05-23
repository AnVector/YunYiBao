package com.anyihao.ayb.wxapi;


//import com.umeng.weixin.callback.WXCallbackActivity;

import android.content.Intent;
import android.os.Bundle;

import com.anyihao.androidbase.utils.LogUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UmengTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.util.Map;

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengTool.checkWx(this);
    }

    @Override
    protected void handleIntent(Intent intent) {
        mWxHandler.setAuthListener(new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                LogUtils.e("UMWXHandler 333");
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

                LogUtils.e("UMWXHandler 222");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

                LogUtils.e("UMWXHandler 111");
            }
        });
        super.handleIntent(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
