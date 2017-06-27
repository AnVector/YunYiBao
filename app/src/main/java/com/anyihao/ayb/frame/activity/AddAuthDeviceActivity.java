package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class AddAuthDeviceActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_help)
    TextView tvHelp;
    @BindView(R.id.btn_add_auth_device)
    AppCompatButton btnAddAuthDevice;
    @BindView(R.id.et_mac_address)
    PowerfulEditText etMacAddress;
    private String macAddress;
    public static final int RESULT_ADD_AUTH_DEVICE_CODE = 0X0007;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_auth_device;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.add_auth_device));
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddAuthDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                macAddress = etMacAddress.getText().toString().trim();
                if (StringUtils.isEmpty(macAddress)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入设备Mac地址", R.layout.toast, R
                            .id.tv_message);
                    return;
                }
                addAuthorizedDevice();
            }
        });

    }

    private void addAuthorizedDevice() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZEADD");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("mac", macAddress);
        params.put("remarks", "");
        params.put("addStatus", "");

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
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
                Intent intent = new Intent();
                setResult(RESULT_ADD_AUTH_DEVICE_CODE, intent);
                finish();
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ToastUtils.showToast(getApplicationContext(), error, R.layout.toast, R.id
                .tv_message);
    }
}
