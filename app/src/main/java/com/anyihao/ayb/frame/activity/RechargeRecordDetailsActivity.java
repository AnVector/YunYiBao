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
import com.anyihao.ayb.adapter.RechargeRecordDetailsAdapter;
import com.anyihao.ayb.bean.KeyValueBean;
import com.anyihao.ayb.bean.RechargeDetailsBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RechargeRecordDetailsActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private RechargeRecordDetailsAdapter mAdapter;
    private String idxOrderID;
    private List<KeyValueBean> mData = new LinkedList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge_record_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        idxOrderID = intent.getStringExtra("idxOrderID");
        if (!StringUtils.isEmpty(idxOrderID)) {
            getOrderDetails();
        }
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.recharge_record_details));
        mData.clear();
        mData.addAll(convert2KeyValueBean(null));
        mAdapter = new RechargeRecordDetailsAdapter(this, R.layout.item_recharge_record_details,
                mData);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
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

    private void getOrderDetails() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAYDETAILS");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("idxOrderID", idxOrderID);
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
            RechargeDetailsBean bean = GsonUtils.getInstance().transitionToBean(result,
                    RechargeDetailsBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                mData.clear();
                mData.addAll(convert2KeyValueBean(bean));
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

    }

    private List<KeyValueBean> convert2KeyValueBean(RechargeDetailsBean bean) {
        List<KeyValueBean> beans = new LinkedList<>();
        if (bean == null) {
            beans.add(0, new KeyValueBean().setTitle("流量充值").setValue("--"));
            beans.add(1, new KeyValueBean().setTitle("截止日期").setValue("--"));
            beans.add(2, new KeyValueBean().setTitle("付款方式").setValue("--"));
            beans.add(3, new KeyValueBean().setTitle("流水号").setValue("--"));
            beans.add(4, new KeyValueBean().setTitle("支付时间").setValue("--"));
        } else {
            beans.add(0, new KeyValueBean().setTitle("流量充值").setValue(bean.getFlow()));
            beans.add(1, new KeyValueBean().setTitle("截止日期").setValue(bean.getEffectTm()));
            beans.add(2, new KeyValueBean().setTitle("付款方式").setValue(bean.getTopupType()));
            beans.add(3, new KeyValueBean().setTitle("流水号").setValue(bean.getIdxOrderID()));
            beans.add(4, new KeyValueBean().setTitle("支付时间").setValue(bean.getCrtTm()));
        }
        return beans;
    }
}
