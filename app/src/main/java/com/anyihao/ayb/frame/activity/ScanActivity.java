package com.anyihao.ayb.frame.activity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.jaeger.library.StatusBarUtil;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;

/**
 * Created by IBM on 2016/10/13.
 */

public class ScanActivity extends ABaseActivity implements DecoratedBarcodeView.TorchListener {
    @BindView(R.id.dbv_custom)
    DecoratedBarcodeView mDBV;
    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 实现相关接口
    // 添加一个按钮用来控制闪光灯，同时添加两个按钮表示其他功能，先用Toast表示
    private CaptureManager captureManager;
    private boolean isLightOn = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_customscan;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.QR_code));
        toolbarTitleMid.setTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setBackgroundColor(Color.parseColor("#000000"));
        // 如果没有闪光灯功能，就去掉相关按钮
//        if (!hasFlash()) {
//        }
        //重要代码，初始化捕获

        //选择闪关灯
//        swichLight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isLightOn) {
//                    mDBV.setTorchOff();
//                } else {
//                    mDBV.setTorchOn();
//                }
//            }
//        });

    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.black));
    }

    @Override
    protected void saveInstanceState(Bundle savedInstanceState) {
        super.saveInstanceState(savedInstanceState);
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
    }

    @Override
    protected void initEvent() {
        mDBV.setTorchListener(this);
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
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    // torch 手电筒
    @Override
    public void onTorchOn() {
        ToastUtils.showShortToast(this, "torch on");
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        ToastUtils.showShortToast(this, "torch off");
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

}
