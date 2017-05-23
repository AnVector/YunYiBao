package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class SetPwdActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_verify_code)
    EditText verifyCode;
    @BindView(R.id.input_set_pwd)
    EditText setPwd;
    @BindView(R.id.input_confirm_pwd)
    EditText confirmPwd;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.set_login_pwd));

    }

    @Override
    protected void initEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetPwdActivity.this,BindDeviceActivity.class);
                startActivity(intent);
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
