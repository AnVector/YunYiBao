package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RentedDevicesAdapter;

import java.util.Arrays;
import java.util.List;

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
    private RentedDevicesAdapter mAdapter;
    private String[] array = new String[]{"设备编号", "设备版本", "设备状态", "租赁时间", "租赁状态", "商家名称", "商家地址",
            "商家联系方式"};
    private List<String> data = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rented_devices;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitleMid.setText(getString(R.string.rented_devices));
        toolbarTitleRight.setText(getString(R.string.rented_history));
        mAdapter = new RentedDevicesAdapter(this, R.layout.item_rented_devices);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        recyclerview.setHasFixedSize(true);
        mAdapter.add(0, data.size(), data);
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
