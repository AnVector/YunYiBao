package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DeviceUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private UHandler mHandler = new UHandler(this);
    //private ComponentName mDefault;
//    private ComponentName mNewCN;
//    private PackageManager mPackageManager;

    private static class UHandler extends Handler {
        private WeakReference<SplashActivity> mActivity;

        private UHandler(SplashActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case GO_HOME:
                        activity.handleGoHome();
                        break;
                    case GO_GUIDE:
                        activity.handleGoGuide();
                        break;
                    case TIMER_TICK:
                        activity.handleTimerTick();
                        break;
                    case TIMER_TICK_FINISHED:
                        activity.handleTimerTickFinished();
                        break;
                    default:
                        break;
                }
            }

        }
    }
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
    protected void setStatusBarTheme() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void initData() {
        mTimeHint = getResources().getString(R.string.timer_seconds);
        boolean isLogin = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin", false);
        if (isLogin) {
            addAuthorizedDevice();
        }
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

    private void addAuthorizedDevice() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZEADD");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("mac", DeviceUtils.getMacAddress(this));
        params.put("remarks", "PHONE");
        params.put("addStatus", "1");

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
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
        if (actionType == 0) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
//                ToastUtils.showToast(getApplicationContext(), "授权成功");
                Logger.d("授权成功");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
