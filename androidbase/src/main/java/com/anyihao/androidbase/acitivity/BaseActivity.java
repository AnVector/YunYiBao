package com.anyihao.androidbase.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anyihao.androidbase.R;
import com.anyihao.androidbase.manager.ActivityManager;
import com.umeng.analytics.MobclickAgent;

/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 获取布局文件Id
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

    protected void init() {
        setContentView(getContentViewId());
        setStatusBarTheme();
        ActivityManager.getInstance().addActivity(this);
        getExtraParams();
        initView();
        initData();
        initEvent();
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
        overridePendingTransition( R.anim.activity_slide_out_left_anim,R.anim.activity_slide_in_left_anim);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
        //overridePendingTransition(R.anim.activity_zoom_in, R.anim.activity_zoom_out);
        //bug fix startActivity和finish方法同时加入切换动画会出现闪屏问题
        // overridePendingTransition( R.anim.activity_slide_in_left_anim,R.anim.activity_slide_in_left_anim);
    }
}
