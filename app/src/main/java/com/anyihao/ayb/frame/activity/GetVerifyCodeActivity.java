package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class GetVerifyCodeActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_next)
    AppCompatButton btnNext;
    @BindView(R.id.edt_phone_num)
    PowerfulEditText edtPhoneNum;
    @BindView(R.id.input_verify_code)
    PowerfulEditText inputVerifyCode;
    @BindView(R.id.tv_time_ticker)
    TextView tvTimeTicker;
    private String title;
    private String action;
    private static final int TIMER_TICK = 1001;
    private static final int TIMER_TICK_FINISHED = 1002;
    private static final int REQUEST_CODE = 1003;
    private static final int RESULT_CODE = 1004;
    private String mTimeHint;
    private byte mTimeLeft;
    private CountDownTimer mCountDownTimer;
    private String phoneNum;
    private String verifyCode;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_retrieve_pwd;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        title = intent.getStringExtra("title");
        action = intent.getStringExtra("action");
        phoneNum = intent.getStringExtra("phoneNum");
    }

    @Override
    protected void initData() {
        mTimeHint = getResources().getString(R.string.re_get_after_60s);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(title);
        if (action.equals("REBIND")) {
            btnNext.setText(getString(R.string.submit));
        }
    }

    @Override
    protected void initEvent() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode = inputVerifyCode.getText().toString().trim();
                if (StringUtils.isEmpty(verifyCode)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入验证码", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                if (verifyCode.length() != 6) {
                    ToastUtils.showToast(getApplicationContext(), "请输入正确的验证码", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                checkVerifyCode();
            }
        });

        tvTimeTicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = edtPhoneNum.getText().toString().trim();
                if (StringUtils.isEmpty(phoneNum)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入手机号", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                if (phoneNum.length() != 11 || !"1".equals(phoneNum.substring(0, 1))) {
                    ToastUtils.showToast(getApplicationContext(), "请输入正确的手机号", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                tvTimeTicker.setText(String.format(mTimeHint, 60));
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
        params.put("action", action);
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

    private void handleTimerTick() {
        tvTimeTicker.setText(String.format(mTimeHint, mTimeLeft));
    }

    private void handleTimerTickFinished() {
        tvTimeTicker.setText(getString(R.string.get_verify_code));
        tvTimeTicker.setEnabled(true);
        tvTimeTicker.setTextColor(Color.parseColor("#2DA8F4"));
//        tvTimeTicker.setBackground(null);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean
                .class);
        if (bean == null)
            return;
        if (actionType == 0) {
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast,
                        R.id.tv_message);
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast,
                        R.id.tv_message);
            }
        }
        if (actionType == 1) {
            if (bean.getCode() == 200) {
                Intent intent;
                switch (action) {
                    case "ORIGINAL":
                        intent = new Intent(GetVerifyCodeActivity.this, GetVerifyCodeActivity
                                .class);
                        intent.putExtra("action", "REBIND");
                        intent.putExtra("title", "设置新手机");
                        intent.putExtra("phoneNum", "");
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case "MODIFYPWD":
                        intent = new Intent(GetVerifyCodeActivity.this, ResetPwdActivity
                                .class);
                        intent.putExtra("phoneNo", phoneNum);
                        startActivity(intent);
                        break;
                    case "REBIND":
                        ToastUtils.showToast(getApplicationContext(), "新手机号绑定成功", R.layout.toast,
                                R.id.tv_message);
                        intent = new Intent();
                        intent.putExtra("result", 1);
                        setResult(RESULT_CODE, intent);
                        this.finish();
                        break;
                    default:
                        break;
                }

            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast,
                        R.id.tv_message);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            if (data == null)
                return;
            int result = data.getIntExtra("result", 0);
            if (result == 1) {
                this.finish();
            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer == null)
            return;
        mCountDownTimer.cancel();
    }
}
