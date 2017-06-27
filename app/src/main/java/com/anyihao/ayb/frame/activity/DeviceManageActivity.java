package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.AuthDeviceAdapter;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DeviceManageActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_add_auth_device)
    AppCompatButton btnAddAuthDevice;
    private AuthDeviceAdapter mAdapter;
    private List<DataBean> mData = new ArrayList<>();
    public static final int REQUEST_ADD_AUTH_DEVICE_CODE = 0X00006;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_manage;
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
        toolbarTitleMid.setText(getString(R.string.authorization_device));
        mAdapter = new AuthDeviceAdapter(this, R.layout.item_auth_device);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        getAuthorizedDevices();
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
                Intent intent = new Intent(DeviceManageActivity.this, AddAuthDeviceActivity.class);
                startActivityForResult(intent, REQUEST_ADD_AUTH_DEVICE_CODE);
            }
        });

    }

    private void getAuthorizedDevices() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZELIST");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));

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
            AuthorizedDeviceListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    AuthorizedDeviceListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans.size() > 0) {
                    mData.clear();
                    mData.addAll(beans);
                    mAdapter.add(0, mData.size(), mData);
                } else {
                    ToastUtils.showToast(getApplicationContext(), "暂无绑定设备", R.layout.toast, R.id
                            .tv_message);
                }
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                        .tv_message);
            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_AUTH_DEVICE_CODE && resultCode == AddAuthDeviceActivity
                .RESULT_ADD_AUTH_DEVICE_CODE) {
            getAuthorizedDevices();
        }
    }
}
