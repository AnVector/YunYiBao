package com.anyihao.androidbase.acitivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.anyihao.androidbase.R;
import com.anyihao.androidbase.manager.ActivityManager;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhy.m.permission.MPermissions;

/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    protected String getTag() {
        return this.getClass().getSimpleName();
    }

    /**
     * 获取布局文件Id
     *
     * @return layoutId
     */
    protected abstract int getContentViewId();

    /**
     * 获取状态栏主题色
     */
    protected abstract void setStatusBarTheme();

    /**
     * 获取从上一页面传递的参数
     */
    protected abstract void getExtraParams();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    protected void init(Bundle savedInstanceState) {
        TAG = getTag();
        setContentView(getContentViewId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityManager.getInstance().addActivity(this);
        getExtraParams();
        initView();
        saveInstanceState(savedInstanceState);
        initData();
        initEvent();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBarTheme();
    }

    protected void saveInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState == null)
            return;
        Logger.d(TAG, "saveInstanceState: " + savedInstanceState);

    }

    protected void permissionsRequest(int requestCode, String... permissions) {
        if (requestCode == -1 || permissions == null || permissions.length == 0)
            return;
        if (!MPermissions.shouldShowRequestPermissionRationale(this, permissions[0], requestCode)) {
            MPermissions.requestPermissions(this, requestCode, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //此切换动画应用在本Activity的退出和目的Acitity的进入
//      overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
//        overridePendingTransition(R.anim.activity_zoom_in, R.anim.activity_zoom_out);
        overridePendingTransition(R.anim.activity_slide_out_left_anim, R.anim
                .activity_slide_in_left_anim);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
        //overridePendingTransition(R.anim.activity_zoom_in, R.anim.activity_zoom_out);
        //bug fix startActivity和finish方法同时加入切换动画会出现闪屏问题
        // overridePendingTransition( R.anim.activity_slide_in_left_anim,R.anim
        // .activity_slide_in_left_anim);
    }
}
