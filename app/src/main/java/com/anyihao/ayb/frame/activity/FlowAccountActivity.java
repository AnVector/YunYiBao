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
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.DataFlowAssortAdapter;
import com.anyihao.ayb.bean.FlowAccountBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.ui.DashboardView;

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
    private List<String> mData = new LinkedList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_flow_account;
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
        titleMid.setText(getString(R.string.my_balance));
        mAdapter = new DataFlowAssortAdapter(this, R.layout.item_data_assortment);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        for (int i = 0; i < 3; i++) {
            mData.add("0%");
        }
        mAdapter.add(0, mData.size(), mData);
        getFlowAccount();
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
                mData.set(0, bean.getInitUseFlow() / bean.getInitFlow() + "%");
                mData.set(1, bean.getBuyUseFlow() / bean.getBuyFlow() + "%");
                mData.set(2, bean.getTaskUseFlow() / bean.getTaskFlow() + "%");
                dashboardView.setCreditValueWithAnim((int) ((bean.getTotalUseFlow() / bean
                                .getTotalFlow() * 100))
                        , bean.getTotalFlow() + "", bean.getTotalUseFlow() + "");
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(getApplicationContext(), error);
        }
    }
}
