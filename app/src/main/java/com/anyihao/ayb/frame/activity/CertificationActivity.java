package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
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
import com.anyihao.ayb.bean.CertificationStatusBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class CertificationActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_mobile_cert)
    TextView tvMobileCert;
    @BindView(R.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R.id.edt_user_name)
    PowerfulEditText edtUserName;
    @BindView(R.id.edt_identification_no)
    PowerfulEditText edtIdentificationNo;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    private String userName;
    private String IDNumber;


    /**
     * 获取布局文件Id
     *
     * @return layoutId
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_certification;
    }

    /**
     * 获取从上一页面传递的参数
     */
    @Override
    protected void getExtraParams() {

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.authentication));
        tvStepTwo.setBackground(getResources().getDrawable(R.drawable.ic_step_yes));
    }

    /**
     * 初始化事件
     */
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
                userName = edtUserName.getText().toString().trim();
                IDNumber = edtIdentificationNo.getText().toString().trim();
                if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(IDNumber)) {
                    ToastUtils.showToast(getApplicationContext(), "请填写完整的用户信息", R.layout.toast, R
                            .id.tv_message);
                    return;
                }
                certificate();
            }
        });
    }

    private void certificate() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "UPDATESTATUS");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("username", userName);
        params.put("idcard", IDNumber);

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            CertificationStatusBean bean = GsonUtils.getInstance().transitionToBean(result,
                    CertificationStatusBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                    .tv_message);
            if (bean.getCode() == 200) {
                Intent intent = new Intent(CertificationActivity.this, DepositActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ToastUtils.showToast(getApplicationContext(), error, R.layout.toast, R.id
                .tv_message);
    }
}
