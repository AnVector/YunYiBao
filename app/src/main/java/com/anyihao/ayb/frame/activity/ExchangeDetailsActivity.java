package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ExchangeDetailsBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author Admin
 */
public class ExchangeDetailsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_exchange)
    AppCompatButton btnExchange;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_exchange_content)
    TextView tvExchangeContent;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.imv_header)
    ImageView imvHeader;
    private int exchangeId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_exchange_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        exchangeId = intent.getIntExtra("exchangeId", -1);
        if (exchangeId != -1) {
            getExchangeDetails();
        }

    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.exchange_details));
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeFlow();
            }
        });
    }

    private void getExchangeDetails() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "DETAILS");
        params.put("exchangeId", exchangeId + "");
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void exchangeFlow() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "REDEEM");
        params.put("exchangeId", exchangeId + "");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(1)
                        .createTask());
    }

    private void displayImage(ExchangeDetailsBean bean) {
        tvDescription.setText(bean.getIntroduction());
        tvExchangeContent.setText(bean.getMethod());
        tvDuration.setText("活动时间：截止至" + bean.getActiveTm());
        if (!TextUtils.isEmpty(bean.getUpImage())) {
            Glide.with(this)
                    .load(bean.getUpImage())
                    .into(imvHeader);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            ExchangeDetailsBean bean = GsonUtils.getInstance().transitionToBean(result,
                    ExchangeDetailsBean.class);
            if (bean == null) {
                return;
            }
            if (bean.getCode() == 200) {
                displayImage(bean);
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null) {
                return;
            }
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), "流量兑换成功");
                finish();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }
}
