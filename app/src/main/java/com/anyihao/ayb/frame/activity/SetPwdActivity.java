package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DeviceUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.MD5;
import com.anyihao.androidbase.utils.PwdCheckUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.RegisterBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SetPwdActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_confirm_pwd)
    EditText confirmPwd;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;
    @BindView(R.id.input_verify_code)
    PowerfulEditText inputVerifyCode;
    @BindView(R.id.input_set_pwd)
    PowerfulEditText inputSetPwd;
    @BindView(R.id.tv_time_ticker)
    TextView tvTimeTicker;
    private static final int TIMER_TICK = 1001;
    private static final int TIMER_TICK_FINISHED = 1002;
    public static final int RESULT_SET_PWD_SUCCESS_CODE = 1003;
    private String mTimeHint;
    private byte mTimeLeft;
    private CountDownTimer mCountDownTimer;
    private String phoneNum;
    private String verifyCode;
    private String setPwd;
    private String checkPwd;
    private UHandler mHandler = new UHandler(this);

    private static class UHandler extends Handler {
        private WeakReference<SetPwdActivity> mActivity;

        private UHandler(SetPwdActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_TICK:
                    if (mActivity.get() != null) {
                        mActivity.get().handleTimerTick();
                    }
                    break;
                case TIMER_TICK_FINISHED:
                    if (mActivity.get() != null) {
                        mActivity.get().handleTimerTickFinished();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        phoneNum = intent.getStringExtra("phoneNo");
    }

    @Override
    protected void initData() {
        mTimeHint = getResources().getString(R.string.re_get_after_60s);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.set_login_pwd));

    }

    @Override
    protected void initEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode = inputVerifyCode.getText().toString().trim();
                setPwd = inputSetPwd.getText().toString().trim();
                checkPwd = confirmPwd.getText().toString().trim();
                if (StringUtils.isEmpty(verifyCode)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入验证码", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                if (verifyCode.length() != 6) {
                    ToastUtils.showToast(getApplicationContext(), "验证码错误", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                if (StringUtils.isEmpty(setPwd)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入密码", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                if (setPwd.length() < 6 || setPwd.length() > 16) {
                    ToastUtils.showToast(getApplicationContext(), "密码长度必须在6-16位之间", R.layout
                            .toast, R.id
                            .tv_message);
                    return;
                }
                if (!PwdCheckUtils.containsLetterDigit(setPwd)) {
                    ToastUtils.showToast(getApplicationContext(), "密码必须同时包含字母和数字", R.layout
                            .toast, R.id
                            .tv_message);
                    return;
                }
                if (!setPwd.equals(checkPwd)) {
                    ToastUtils.showToast(getApplicationContext(), "两次输入的密码不一致", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                checkVerifyCode();
            }
        });

        tvTimeTicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTimeTicker.setText(String.format(mTimeHint, 60));
                tvTimeTicker.setTextSize(14f);
                tvTimeTicker.setEnabled(false);
                tvTimeTicker.setTextColor(Color.parseColor("#B5B5B5"));
//                tvTimeTicker.setBackgroundColor(Color.parseColor("#F5F5F9"));
                getVerifyCode();
                mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
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
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getVerifyCode() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "SJH");
        params.put("phoneNumber", phoneNum);
        params.put("action", "REGISTER");
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void checkVerifyCode() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "YZM");
        params.put("phoneNumber", phoneNum);
        params.put("identifyingCode", verifyCode);
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(1)
                        .createTask());
    }

    private void register() {

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "RGT");
        params.put("phoneNumber", phoneNum);
        params.put("pwd", MD5.string2MD5(setPwd));
        params.put("ckpwd", MD5.string2MD5(checkPwd));
        params.put("addrMAC", DeviceUtils.getMacAddress(getApplicationContext()));

        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(2)
                        .createTask());
    }


    private void handleTimerTick() {
        tvTimeTicker.setText(String.format(mTimeHint, mTimeLeft));
    }

    private void handleTimerTickFinished() {
        tvTimeTicker.setText(getString(R.string.get_verify_code));
        tvTimeTicker.setTextSize(16f);
        tvTimeTicker.setEnabled(true);
        tvTimeTicker.setTextColor(Color.parseColor("#2DA8F4"));
    }

    private void onVerificationFinished() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        handleTimerTickFinished();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast,
                        R.id.tv_message);
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast,
                        R.id.tv_message);
            }
        }
        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean
                    .class);
            if (bean == null)
                return;
            onVerificationFinished();
            if (bean.getCode() == 200) {
                register();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast,
                        R.id.tv_message);
            }
        }

        if (actionType == 2) {
            RegisterBean bean = GsonUtils.getInstance().transitionToBean(result, RegisterBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                Intent intent = new Intent();
                setResult(RESULT_SET_PWD_SUCCESS_CODE, intent);
                finish();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
