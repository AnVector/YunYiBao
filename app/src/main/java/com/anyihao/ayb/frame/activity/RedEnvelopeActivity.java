package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class RedEnvelopeActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.btn_unfold_it)
    AppCompatButton btnUnfoldIt;
    @BindView(R.id.tv_envelope_value)
    TextView tvEnvelopeValue;
    @BindView(R.id.tv_use_expires)
    TextView tvUseExpires;
    @BindView(R.id.btn_go_to_account)
    TextView btnGoToAccount;
    @BindView(R.id.ll_open)
    LinearLayout llOpen;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.ll_close)
    LinearLayout llClose;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    private String sendName;
    private String sendAvatar;
    private String flow;
    private String crtTime;
    private String effectTime;
    private int status;
    private int keyId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_red_envelope;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        keyId = intent.getIntExtra("keyId", 0);
        sendName = intent.getStringExtra("sendName");
        sendAvatar = intent.getStringExtra("sendAvatar");
        flow = intent.getStringExtra("flow");
        crtTime = intent.getStringExtra("crtTime");
        effectTime = intent.getStringExtra("effectTime");
        status = intent.getIntExtra("status", 0);
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.red_envelope));

        if (status == 0) {
            llClose.setVisibility(View.VISIBLE);
            llOpen.setVisibility(View.GONE);
            if (!StringUtils.isEmpty(sendAvatar)) {
                Glide.with(this).load(sendAvatar)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .placeholder(R.drawable.user_profile)
                        .crossFade().into(ivUserProfile);
            }
        } else {
            openEnvelope();
        }
    }

    private void openEnvelope() {
        llClose.setVisibility(View.GONE);
        llOpen.setVisibility(View.VISIBLE);
        if (!StringUtils.isEmpty(sendAvatar)) {
            Glide.with(this).load(sendAvatar)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.drawable.user_profile)
                    .crossFade().into(ivProfile);
        }

        tvEnvelopeValue.setText(flow);
        tvUseExpires.setText("使用期限："+getDate(crtTime) + "至" + getDate(effectTime));
        tvFrom.setText(String.format(getString(R.string.fried_name), sendName));
    }

    private String getDate(String time) {
        if (StringUtils.isEmpty(time) || time.length() < 10)
            return "";
        return time.substring(0, 10);
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnUnfoldIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagEnvelope();
                openEnvelope();
            }
        });

        btnGoToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RedEnvelopeActivity.this, FlowAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void flagEnvelope() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "REDPACK");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("keyId", keyId + "");

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
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), "红包领取成功");
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
