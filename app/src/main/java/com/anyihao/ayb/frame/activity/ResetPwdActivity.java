package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class ResetPwdActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_new_pwd)
    EditText edtNewPwd;
    @BindView(R.id.edt_comfirm_pwd)
    EditText edtComfirmPwd;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.reset_pwd));
    }

    @Override
    protected void initEvent() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
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

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
