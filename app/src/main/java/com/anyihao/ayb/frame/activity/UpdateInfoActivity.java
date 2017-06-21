package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import org.json.JSONException;
import org.json.JSONObject;

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
        if (StringUtils.isEmpty(key))
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
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleRight.setText(getString(R.string.save));
        if (!StringUtils.isEmpty(key)) {
            titleMid.setText(key);
            tvInfo.setText(key + "：");
        }
        if (!StringUtils.isEmpty(value)) {
            edtInfo.setText(value);
        }
    }

    @Override
    protected void initEvent() {

        titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newValue = edtInfo.getText().toString().trim();
                if (StringUtils.isEmpty(newValue)) {
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
        JSONObject json = new JSONObject();
        try {
            json.put("cmd", "PERSONSAVE");
            json.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
            json.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType",
                    ""));
            json.put(property, newValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL + "?cmd=PERSONSAVE" + "&" + "uid=" +
                                PreferencesUtils.getString(getApplicationContext(), "uid", "") +
                                "&" + "userType=" + PreferencesUtils.getString
                                (getApplicationContext(), "userType",
                                        "") + "&" + property + "=" + newValue)
                        .setContent(json.toString())
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
                finish();
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (actionType == 0) {
            ToastUtils.showToast(getApplicationContext(), error, R.layout.toast, R.id
                    .tv_message);
        }

    }
}
