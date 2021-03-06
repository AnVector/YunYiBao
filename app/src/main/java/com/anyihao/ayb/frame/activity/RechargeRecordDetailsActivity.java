package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
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
    private String effectTm;
    private String flow;
    private String payType;
    private String idxOrderID;
    private String status;

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
        effectTm = intent.getStringExtra("effectTm");
        flow = intent.getStringExtra("flow");
        payType = intent.getStringExtra("payType");
        idxOrderID = intent.getStringExtra("idxOrderID");
        status = intent.getStringExtra("status");
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
        if ("5".equals(status) || "6".equals(status)) {
            mData.addAll(convert2Bean("押金缴纳"));
        } else if ("7".equals(status)) {
            mData.addAll(convert2Bean("押金退款"));
        } else {
            mData.addAll(convert2KeyValueBean());
        }
        mAdapter = new RechargeRecordDetailsAdapter(this, R.layout.item_recharge_record_details,
                mData);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        recyclerview.setAdapter(mAdapter);

        if (!TextUtils.isEmpty(fee)) {
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
        beans.add(0, new KeyValueBean().setTitle("订单类型").setValue("流量充值"));
        beans.add(1, new KeyValueBean().setTitle("充值说明").setValue(flow));
        beans.add(2, new KeyValueBean().setTitle("截止日期").setValue(effectTm));
        beans.add(3, new KeyValueBean().setTitle("付款方式").setValue(payType));
        beans.add(4, new KeyValueBean().setTitle("流水号").setValue(idxOrderID));
        beans.add(5, new KeyValueBean().setTitle("创建时间").setValue(crtTime));
        return beans;
    }

    private List<KeyValueBean> convert2Bean(String type) {
        List<KeyValueBean> beans = new LinkedList<>();
        beans.add(0, new KeyValueBean().setTitle("订单类型").setValue(type));
        beans.add(1, new KeyValueBean().setTitle("付款方式").setValue(payType));
        beans.add(2, new KeyValueBean().setTitle("流水号").setValue(idxOrderID));
        beans.add(3, new KeyValueBean().setTitle("创建时间").setValue(crtTime));
        return beans;
    }
}
