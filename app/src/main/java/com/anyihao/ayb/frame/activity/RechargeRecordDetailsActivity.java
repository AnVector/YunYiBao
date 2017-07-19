package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RechargeRecordDetailsAdapter;
import com.anyihao.ayb.bean.KeyValueBean;

import java.util.LinkedList;
import java.util.List;

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
    private List<KeyValueBean> mData = new LinkedList<>();
    private String fee;
    private String crtTime;
    private String flow;
    private String payType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge_record_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        fee = intent.getStringExtra("fee");
        crtTime = intent.getStringExtra("crtTime");
        flow = intent.getStringExtra("flow");
        payType = intent.getStringExtra("payType");
        if ("WXPAY".equals(payType)) {
            payType = "微信支付";
        } else {
            payType = "支付宝支付";
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
        mData.addAll(convert2KeyValueBean());
        mAdapter = new RechargeRecordDetailsAdapter(this, R.layout.item_recharge_record_details,
                mData);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        if (!StringUtils.isEmpty(fee)) {
            tvPrice.setText(fee + "元");
        }
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

    private List<KeyValueBean> convert2KeyValueBean() {
        List<KeyValueBean> beans = new LinkedList<>();
        beans.add(0, new KeyValueBean().setTitle("流量充值").setValue(flow));
        beans.add(1, new KeyValueBean().setTitle("截止日期").setValue("--"));
        beans.add(2, new KeyValueBean().setTitle("付款方式").setValue(payType));
        beans.add(3, new KeyValueBean().setTitle("流水号").setValue("--"));
        beans.add(4, new KeyValueBean().setTitle("支付时间").setValue(crtTime));
        return beans;
    }
}
