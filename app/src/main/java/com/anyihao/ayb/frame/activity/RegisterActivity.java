package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.chaychan.viewlib.PowerfulEditText;

import butterknife.BindView;

public class RegisterActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar_title_right)
    TextView titleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_next)
    AppCompatButton btnNext;
    @BindView(R.id.input_phone_num)
    PowerfulEditText inputPhoneNum;
    private static final int REQUEST_SET_PWD_CODE = 0x0001;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.register_by_phone));
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = inputPhoneNum.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNo)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入手机号");
                    return;
                }

                if (phoneNo.length() != 11 || !"1".equals(phoneNo.substring(0, 1))) {
                    ToastUtils.showToast(getApplicationContext(), "请输入正确的手机号");
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, SetPwdActivity.class);
                intent.putExtra("phoneNo", phoneNo);
                startActivityForResult(intent, REQUEST_SET_PWD_CODE);
            }
        });

    }


    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SET_PWD_CODE) {
            if (resultCode == SetPwdActivity.RESULT_SET_PWD_SUCCESS_CODE) {
                this.finish();
            }
        }
    }
}
