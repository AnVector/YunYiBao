package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
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

public class UpdateInfoActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar_title_right)
    TextView titleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static final String INFORMATION_KEY = "information_key";
    public static final String INFORMATION_VALUE = "information_value";
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.edt_info)
    PowerfulEditText edtInfo;
    private String key;
    private String value;
    private String newValue;
    private String property;
    public static final int RESULT_UPDATE_SUCCESS_CODE = 0X0002;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_info;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        key = intent.getStringExtra(INFORMATION_KEY);
        value = intent.getStringExtra(INFORMATION_VALUE);
        setProperty(key);

    }

    private void setProperty(String key) {
        if (TextUtils.isEmpty(key))
            return;
        switch (key) {
            case "昵称":
                property = "nickname";
                break;
            case "性别":
                property = "sex";
                break;
            case "邮箱":
                property = "email";
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleRight.setText(getString(R.string.save));
        if (!TextUtils.isEmpty(key)) {
            titleMid.setText(key);
            tvInfo.setText(key + "：");
        }
        if (!TextUtils.isEmpty(value)) {
            edtInfo.setText(value);
        }
    }

    @Override
    protected void initEvent() {

        titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newValue = edtInfo.getText().toString().trim();
                if (TextUtils.isEmpty(newValue)) {
                    ToastUtils.showToast(getApplicationContext(), "信息不能为空", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                if (value.equals(newValue)) {
                    ToastUtils.showToast(getApplicationContext(), "不能与原始信息相同", R.layout.toast, R.id
                            .tv_message);
                    return;
                }
                updateInfo();

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void updateInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PERSONSAVE");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType",
                ""));
        params.put(property, newValue);
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
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                    .tv_message);
            if (bean.getCode() == 200) {
                setResult(RESULT_UPDATE_SUCCESS_CODE);
                finish();
            }
        }

    }
}
