package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.DataFlowAssortAdapter;
import com.anyihao.ayb.ui.DashboardView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class FlowAccountActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dashboard_view)
    DashboardView dashboardView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_data_flow_chart)
    TextView tvDataFlowChart;
    @BindView(R.id.tv_data_flow_charge)
    TextView tvDataFlowCharge;
    @BindView(R.id.tv_recharge_record)
    TextView tvRechargeRecord;
    private DataFlowAssortAdapter mAdapter;
    String[] array = new String[]{"初始赠送剩余流量43.36%", "购买会员剩余流量25.45%", "任务赠送剩余流量43.60%"};
    private List<String> mData = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_flow_account;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.my_balance));
        dashboardView.setCreditValueWithAnim(new Random().nextInt(100 - 35) + 35);
        mAdapter = new DataFlowAssortAdapter(this, R.layout.item_data_assortment);
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
        tvDataFlowChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowAccountActivity.this, FlowChartActivity.class);
                startActivity(intent);
            }
        });

        tvDataFlowCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowAccountActivity.this, RechargeActivity.class);
                startActivity(intent);
            }
        });

        tvRechargeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowAccountActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
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
