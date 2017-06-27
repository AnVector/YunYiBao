package com.anyihao.ayb.frame.activity;

import android.content.Intent;
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
import com.anyihao.ayb.adapter.RentedDeviceAdapter;
import com.anyihao.ayb.bean.RentDeviceBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RentedDevicesActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private RentedDeviceAdapter mAdapter;
    private List<String> mData = new LinkedList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rented_devices;
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
        toolbarTitleMid.setText(getString(R.string.rented_devices));
        toolbarTitleRight.setText(getString(R.string.rented_history));
        mAdapter = new RentedDeviceAdapter(this, R.layout.item_rented_devices);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        getRentedDevice();
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(RentedDevicesActivity.this, RentHistoryActivity.class);
//                startActivity(intent);
//                return true;
//            }
//        });
        toolbarTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentedDevicesActivity.this, RentHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getRentedDevice() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MYDEV");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
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
            RentDeviceBean bean = GsonUtils.getInstance().transitionToBean(result, RentDeviceBean
                    .class);
            if (bean == null)
                return;
            ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                    .tv_message);
            mData.clear();
            if (bean.getCode() == 200) {
                mData.add(bean.getVid());
                mData.add(bean.getVidVer());
                mData.add(bean.getVidStatus());
                mData.add(bean.getLeaseTime());
                mData.add(bean.getShopName());
                mData.add(bean.getShopAddr());
                mData.add(bean.getContact());
            } else {
                for (int i = 0; i < 7; i++) {
                    mData.add("--");
                }
            }
            mAdapter.add(0, mData.size(), mData);
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ToastUtils.showToast(getApplicationContext(), error, R.layout.toast, R.id
                .tv_message);
    }
}
