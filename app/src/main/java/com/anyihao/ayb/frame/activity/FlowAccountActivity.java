package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.DataFlowAssortAdapter;
import com.anyihao.ayb.bean.FlowAccountBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.ui.DashboardView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private List<Double> mData = new LinkedList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_flow_account;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setBackground(null);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        titleMid.setText(getString(R.string.my_balance));
        titleMid.setTextColor(Color.parseColor("#FFFFFF"));
        for (int i = 0; i < 4; i++) {
            mData.add(0d);
        }
        mAdapter = new DataFlowAssortAdapter(this, R.layout.item_data_assortment, mData);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        getFlowAccount();
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTransparent(FlowAccountActivity.this);
    }


    private void getFlowAccount() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "FLOW");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
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
        if (actionType == 0) {
            FlowAccountBean bean = GsonUtils.getInstance().transitionToBean(result,
                    FlowAccountBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                mData.set(0, generateProportion(bean.getInitUseFlow(), bean.getInitFlow()));
                mData.set(1, generateProportion(bean.getBuyUseFlow(), bean.getBuyFlow()));
                mData.set(2, generateProportion(bean.getTransferUseFlow(), bean.getTransferFlow()));
                mData.set(3, generateProportion(bean.getTaskUseFlow(), bean.getTaskFlow()));
                dashboardView.setCreditValueWithAnim((int) generateProportion(bean
                                .getTotalUseFlow(), bean.getTotalFlow())
                        , generateTotal(bean.getTotalFlow()), generateTotalUse(bean
                                .getTotalUseFlow()));
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }

    private double generateProportion(double numerator, int denominator) {
        if (denominator == 0)
            return 0;
        double percent = numerator* 100 / denominator;
        BigDecimal bg = new BigDecimal(percent);
        percent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return percent;
    }

    private String generateTotal(int total) {
        if (total < 1024) {
            return "总：" + total + "M";
        }
        BigDecimal bg = new BigDecimal(total / 1024);
        double percent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return "总：" + percent + "G";
    }

    private String generateTotalUse(Double totalUse) {
        if (totalUse < 1024) {
            return "可用：" + totalUse + "M";
        }
        BigDecimal bg = new BigDecimal(totalUse / 1024);
        double percent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return "可用：" + percent + "G";
    }
}
