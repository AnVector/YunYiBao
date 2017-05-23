package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.AuthDeviceAdapter;
import com.anyihao.ayb.adapter.CreditAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class DeviceManageActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_add_auth_device)
    Button btnAddAuthDevice;
    private AuthDeviceAdapter mAdapter;
    String[] array = new String[]{"0a:ee:32:44:23:55", "0a:ee:32:44:23:55", "0a:ee:32:44:23:55", "0a:ee:32:44:23:55"};
    private List<String> mData = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_manage;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitleMid.setText(getString(R.string.authorization_device));
        mAdapter = new AuthDeviceAdapter(this, R.layout.item_auth_device);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        recyclerview.setHasFixedSize(true);
        mAdapter.add(0, mData.size(), mData);
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
