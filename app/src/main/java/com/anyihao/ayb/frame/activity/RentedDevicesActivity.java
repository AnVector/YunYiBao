package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RentedDeviceAdapter;
import com.anyihao.ayb.bean.KeyValueBean;
import com.anyihao.ayb.bean.RentDetailsBean;
import com.anyihao.ayb.bean.RentDeviceBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

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
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerview;
    private RentedDeviceAdapter mAdapter;
    private List<KeyValueBean> mData = new LinkedList<>();
    private String keyId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rented_devices;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        keyId = intent.getStringExtra("keyId");
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (TextUtils.isEmpty(keyId)) {
            toolbarTitleMid.setText(getString(R.string.rented_devices));
            toolbarTitleRight.setText(getString(R.string.rented_history));
            getRentedDevice();
        } else {
            toolbarTitleMid.setText(getString(R.string.rent_details));
            getRentDetails();
        }
        initUltimateRV();

    }

    private void initUltimateRV() {
        recyclerview.setHasFixedSize(false);
        mAdapter = new RentedDeviceAdapter(mData, R.layout
                .item_rented_devices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
//        recyclerview.setParallaxHeader(getLayoutInflater().inflate(R.layout
//                .parallax_recyclerview_header, recyclerview.mRecyclerView, false));
        recyclerview.setNormalHeader(getLayoutInflater().inflate(R.layout
                .parallax_recyclerview_header, recyclerview.mRecyclerView, false));
//        recyclerview.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
//            @Override
//            public void onParallaxScroll(float percentage, float offset, View parallax) {
//                Drawable c = toolbar.getBackground();
//                c.setAlpha(255);
//                toolbar.setBackgroundDrawable(c);
//            }
//        });
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    private void getRentDetails() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "RENT");
        params.put("merchantId", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("keyId", keyId);

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(1)
                .setActionType(1)
                .createTask());
    }

    private List<KeyValueBean> generateKeyValueBean(RentDeviceBean bean) {
        List<KeyValueBean> beans = new LinkedList<>();
        if (bean != null) {
            beans.add(0, new KeyValueBean().setTitle("设备编号").setValue(bean.getVid()));
            beans.add(1, new KeyValueBean().setTitle("设备版本").setValue(bean.getVidVer()));
            beans.add(2, new KeyValueBean().setTitle("设备状态").setValue(bean.getVidStatus()));
            beans.add(3, new KeyValueBean().setTitle("租赁时间").setValue(bean.getLeaseTime()));
            beans.add(4, new KeyValueBean().setTitle("商家名称").setValue(bean.getShopName()));
            beans.add(5, new KeyValueBean().setTitle("商家地址").setValue(bean.getShopAddr()));
            beans.add(6, new KeyValueBean().setTitle("商家联系方式").setValue(bean.getContact()));
        } else {
            beans.add(0, new KeyValueBean().setTitle("设备编号").setValue("--"));
            beans.add(1, new KeyValueBean().setTitle("设备版本").setValue("--"));
            beans.add(2, new KeyValueBean().setTitle("设备状态").setValue("--"));
            beans.add(3, new KeyValueBean().setTitle("租赁时间").setValue("--"));
            beans.add(4, new KeyValueBean().setTitle("商家名称").setValue("--"));
            beans.add(5, new KeyValueBean().setTitle("商家地址").setValue("--"));
            beans.add(6, new KeyValueBean().setTitle("商家联系方式").setValue("--"));
        }
        return beans;
    }


    private List<KeyValueBean> generateRentDetailsBean(RentDetailsBean bean) {
        List<KeyValueBean> beans = new LinkedList<>();
        beans.add(0, new KeyValueBean().setTitle("设备编号").setValue(bean.getVid()));
        beans.add(1, new KeyValueBean().setTitle("租赁用户").setValue(bean.getLendNickname()));
        beans.add(2, new KeyValueBean().setTitle("用户联系方式").setValue(bean.getLendPhoneNumber()));
        beans.add(3, new KeyValueBean().setTitle("租赁时间").setValue(bean.getLendTm()));
        beans.add(4, new KeyValueBean().setTitle("租赁状态").setValue(bean.getStatus()));
        return beans;
    }


    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            RentDeviceBean bean = GsonUtils.getInstance().transitionToBean(result, RentDeviceBean
                    .class);
            if (bean == null)
                return;
            List<KeyValueBean> beans;
            if (bean.getCode() == 200) {
                beans = generateKeyValueBean(bean);
            } else {
                beans = generateKeyValueBean(null);
            }
            mAdapter.insert(beans);
        }

        if (actionType == 1) {
            RentDetailsBean bean = GsonUtils.getInstance().transitionToBean(result,
                    RentDetailsBean.class);
            if (bean == null)
                return;
            List<KeyValueBean> beans;
            if (bean.getCode() == 200) {
                beans = generateRentDetailsBean(bean);
                mAdapter.insert(beans);
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }

        }
    }
}
