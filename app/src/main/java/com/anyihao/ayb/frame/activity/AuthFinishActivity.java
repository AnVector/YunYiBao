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
import com.anyihao.ayb.bean.IEBoxBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class AuthFinishActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_mobile_cert)
    TextView tvMobileCert;
    @BindView(R.id.tv_user_cert)
    TextView tvUserCert;
    @BindView(R.id.tv_deposite)
    TextView tvDeposite;
    @BindView(R.id.tv_step_four)
    TextView tvStepFour;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.imv_finished)
    ImageView imvFinished;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    public static final int RESULT_FINISH_CODE = 0X0004;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_auth_finish;
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
        toolbarTitleMid.setText(getString(R.string.finished));
        tvStepFour.setBackground(getResources().getDrawable(R.drawable.ic_step_yes));

    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(AuthFinishActivity.this);
                integrator
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setOrientationLocked(false)//扫描方向固定
                        .setCaptureActivity(ScanActivity.class) //
                        // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
            }
        });

    }

    private void getIEBoxNum(String merchantId) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "LEND");
        params.put("lendId", PreferencesUtils.getString(getApplicationContext(), "uid",
                ""));
        params.put("merchantId", merchantId);
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .createTask());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                data);
        if (intentResult != null && intentResult.getContents() != null) {
            String result = intentResult.getContents();
            if (TextUtils.isEmpty(result)) {
                ToastUtils.showToast(getApplicationContext(), "扫码失败");
            } else {
                getIEBoxNum(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            IEBoxBean bean = GsonUtils.getInstance().transitionToBean(result, IEBoxBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                Intent intent = new Intent(this, DeviceCodeActivity.class);
                intent.putExtra("vid", bean.getVid());
                startActivity(intent);
                setResult(RESULT_FINISH_CODE);
                finish();
            } else {
                ToastUtils.showToast(this.getApplicationContext(), bean.getMsg());
            }
        }
    }
}
