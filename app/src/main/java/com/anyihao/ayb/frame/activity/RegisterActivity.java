package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
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
                if (phoneNo.length() != 11 || !"1".equals(phoneNo.substring(0, 1))) {
                    ToastUtils.showToast(getApplicationContext(), "手机号码不正确，请重新输入！", R.layout
                            .toast, R.id.tv_message);
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, SetPwdActivity.class);
                intent.putExtra("phoneNo", phoneNo);
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
