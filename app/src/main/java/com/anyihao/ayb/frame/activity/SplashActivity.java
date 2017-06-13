package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.ayb.R;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;

public class SplashActivity extends ABaseActivity {


    @BindView(R.id.timer_tv)
    TextView mTimerTv;
    //static params
    private static final int GO_HOME = 1001;
    private static final int GO_GUIDE = 1002;
    private static final int TIMER_TICK = 1003;
    private static final int TIMER_TICK_FINISHED = 1004;
    //time left params
    private String mTimeHint;
    private byte mTimeLeft;
    private CountDownTimer mCountDownTimer;
//    private ComponentName mDefault;
//    private ComponentName mNewCN;
//    private PackageManager mPackageManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    handleGoHome();
                    break;
                case GO_GUIDE:
                    handleGoGuide();
                    break;
                case TIMER_TICK:
                    handleTimerTick();
                    break;
                case TIMER_TICK_FINISHED:
                    handleTimerTickFinished();
                    break;
                default:
                    break;
            }
        }
    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mDefault = getComponentName();
//        mNewCN = new ComponentName(getBaseContext(), "com.anyihao.ayb.frame.activity.NewIcon");
//        mPackageManager = getApplicationContext().getPackageManager();
//        setNewIcon();
//    }

//    private void setNewIcon(){
//        disableComponent(mDefault);
//        enableComponent(mNewCN);
//    }
//
//    private void setDefault(){
//        disableComponent(mNewCN);
//        enableComponent(mDefault);
//    }

//    private void enableComponent(ComponentName componentName) {
//        mPackageManager.setComponentEnabledSetting(componentName, PackageManager
//                .COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//    }
//
//    private void disableComponent(ComponentName componentName) {
//        mPackageManager.setComponentEnabledSetting(componentName, PackageManager
//                .COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void initData() {
        mTimeHint = getResources().getString(R.string.timer_seconds);
        mCountDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = (byte) (millisUntilFinished / 1000);
                mHandler.sendEmptyMessage(TIMER_TICK);
            }

            @Override
            public void onFinish() {
                mTimeLeft = 0;
                mHandler.sendEmptyMessage(TIMER_TICK_FINISHED);
            }
        };
        mCountDownTimer.start();
    }

    @Override
    protected void initEvent() {
        mTimerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                isAppFirstRun();
            }
        });
    }

    private void handleGoHome() {
        Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleGoGuide() {
//        Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleTimerTick() {
        mTimerTv.setText(String.format(mTimeHint, mTimeLeft));
    }

    private void handleTimerTickFinished() {
        mTimerTv.setText(String.format(mTimeHint, mTimeLeft));
        isAppFirstRun();
    }

    private void isAppFirstRun() {
        boolean isFirstRun = PreferencesUtils.getBoolean(this, "isFirstRun", true);
        if (isFirstRun) {
            PreferencesUtils.putBoolean(this, "isFirstRun", false);
            mHandler.sendEmptyMessage(GO_GUIDE);
        } else {
            mHandler.sendEmptyMessage(GO_HOME);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
