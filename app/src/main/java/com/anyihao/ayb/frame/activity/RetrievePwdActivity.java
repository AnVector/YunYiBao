package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.chaychan.viewlib.PowerfulEditText;

import butterknife.BindView;

public class RetrievePwdActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_next)
    AppCompatButton btnNext;
    public static final int RETRIEVE_ORIGINAL_PASSWORD = 0;
    public static final int VERIFY_ORIGINAL_PHONE = 1;
    public static String REQUEST_TYPE = "type";
    @BindView(R.id.edt_phone_num)
    PowerfulEditText edtPhoneNum;
    @BindView(R.id.input_verify_code)
    PowerfulEditText inputVerifyCode;
    private int type;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_retrieve_pwd;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        type = intent.getIntExtra(REQUEST_TYPE, 0);
    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        if (type == 0) {
            titleMid.setText(getString(R.string.retrieve_pwd));
        } else {
            titleMid.setText(getString(R.string.verify_original_phone));
        }
    }

    @Override
    protected void initEvent() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetrievePwdActivity.this, ResetPwdActivity.class);
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
