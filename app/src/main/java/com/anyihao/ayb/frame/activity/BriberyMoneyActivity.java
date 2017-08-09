package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class BriberyMoneyActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_confirm)
    AppCompatButton btnConfirm;
    @BindView(R.id.edt_user_name)
    PowerfulEditText edtUserName;
    @BindView(R.id.edt_data_amount)
    PowerfulEditText edtDataAmount;
    @BindView(R.id.edt_remark)
    PowerfulEditText edtRemark;
    private String phoneNum;
    private int type;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bribery_money;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        phoneNum = intent.getStringExtra("phoneNum");
        type = intent.getIntExtra("type", 0);
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.gift_data));
        toolbarTitleRight.setText(getString(R.string.gift_history));
        toolbarTitleRight.setTextColor(getResources().getColor(R.color.light_gray));
        if (type == 0) {
            if (!TextUtils.isEmpty(phoneNum)) {
                setTextHintSize(edtUserName, phoneNum);
                edtUserName.setEnabled(false);
            }
        }
    }

    private void setTextHintSize(EditText edt, String phone) {
        if (TextUtils.isEmpty(phone) || edt == null)
            return;
        SpannableString s = new SpannableString(phone);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13, true);
        s.setSpan(ass, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        edt.setHint(s);
    }

    @Override
    protected void initEvent() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName;
                if (type == 0) {
                    userName = phoneNum;
                } else {
                    userName = edtUserName.getText().toString().trim();
                }
                String data = edtDataAmount.getText().toString().trim();
                String remark = edtRemark.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(data)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入要转赠的流量值");
                    return;
                }
                if (TextUtils.isEmpty(remark)) {
                    remark = "";
                }
                present(userName, data, remark);
            }
        });

        toolbarTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BriberyMoneyActivity.this, TransferRecordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void present(String phoneNum, String flow, String remarks) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "INCREASE");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType", ""));
        params.put("phoneNumber", phoneNum);
        params.put("flow", flow);
        params.put("remarks", remarks);

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
            ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            if (bean.getCode() == 200) {
                finish();
            }
        }
    }
}
