package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.chaychan.viewlib.PowerfulEditText;

import butterknife.BindView;

public class CertificationActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_step_one)
    TextView tvStepOne;
    @BindView(R.id.tv_mobile_cert)
    TextView tvMobileCert;
    @BindView(R.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R.id.tv_user_cert)
    TextView tvUserCert;
    @BindView(R.id.tv_step_three)
    TextView tvStepThree;
    @BindView(R.id.tv_deposite)
    TextView tvDeposite;
    @BindView(R.id.tv_step_four)
    TextView tvStepFour;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.edt_user_name)
    PowerfulEditText edtUserName;
    @BindView(R.id.edt_identification_no)
    PowerfulEditText edtIdentificationNo;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;

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
        toolbar.setNavigationIcon(R.drawable.ic_back);
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
                Intent intent = new Intent(CertificationActivity.this, DepositActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
