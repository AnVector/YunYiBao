package com.anyihao.ayb.frame.activity;

import android.content.Intent;
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
import com.anyihao.ayb.bean.RechargeDetailsBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SysRecordDetailsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_data_amount)
    TextView tvDataAmount;
    @BindView(R.id.tv_expires)
    TextView tvExpires;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private String mIdxOrderID;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sys_record_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mIdxOrderID = intent.getStringExtra("idxOrderID");
        if (!StringUtils.isEmpty(mIdxOrderID)) {
            getSysRecordDetails();
        }
    }

    @Override
    protected void initData() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.record_details));
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

    private void getSysRecordDetails() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAYDETAILS");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("idxOrderID", mIdxOrderID);

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
                tvDataAmount.setText(bean.getFlow());
                tvExpires.setText(bean.getCrtTm() + "至" + bean.getEffectTm());
                tvDate.setText(bean.getCrtTm());
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
        } else {
            ToastUtils.showToast(getApplicationContext(), error);
        }
    }
}
