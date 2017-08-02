package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.MD5;
import com.anyihao.androidbase.utils.PwdCheckUtils;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ResetPwdActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    @BindView(R.id.edt_new_pwd)
    PowerfulEditText edtNewPwd;
    @BindView(R.id.edt_comfirm_pwd)
    PowerfulEditText edtComfirmPwd;
    private String phoneNum;
    private String newPwd;
    private String confirmPwd;
    public static final int RESULT_RESET_PWD_SUCCESS_CODE = 0x0001;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reset_pwd;
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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.reset_pwd));
    }

    @Override
    protected void initEvent() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPwd = edtNewPwd.getText().toString().trim();
                confirmPwd = edtComfirmPwd.getText().toString().trim();
                if (TextUtils.isEmpty(newPwd)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入新密码", R.layout.toast, R.id
                            .tv_message);
                    return;
                }

                if (newPwd.length() < 6 || newPwd.length() > 16) {
                    ToastUtils.showToast(getApplicationContext(), "密码长度必须在6-16位之间", R.layout
                            .toast, R.id
                            .tv_message);
                    return;
                }
                if (!PwdCheckUtils.containsLetterDigit(newPwd)) {
                    ToastUtils.showToast(getApplicationContext(), "密码必须同时包含字母和数字", R.layout
                            .toast, R.id
                            .tv_message);
                    return;
                }
                if (!newPwd.equals(confirmPwd)) {
                    ToastUtils.showToast(getApplicationContext(), "两次输入的密码不一致", R.layout.toast, R.id
                            .tv_message);
                    return;
                }

                ModifyPwd();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void ModifyPwd() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MODIFYPWD");
        params.put("phoneNumber", phoneNum);
        params.put("pwd", MD5.string2MD5(newPwd));
        params.put("ckpwd", MD5.string2MD5(confirmPwd));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                Intent intent = new Intent();
                setResult(RESULT_RESET_PWD_SUCCESS_CODE, intent);
                finish();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

    }
}
