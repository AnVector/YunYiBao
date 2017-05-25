package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.chaychan.viewlib.PowerfulEditText;

import butterknife.BindView;

public class UpdateSelfDataActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar_title_right)
    TextView titleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_self_info)
    TextView tvSelfInfo;
    public static final String INFORMATION_KEY = "information_key";
    public static final String INFORMATION_VALUE = "information_value";
    @BindView(R.id.input_self_info)
    PowerfulEditText inputSelfInfo;
    private String key;
    private String value;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_self_data;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        key = intent.getStringExtra(INFORMATION_KEY);
        value = intent.getStringExtra(INFORMATION_VALUE);

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleRight.setText(getString(R.string.save));
        if (!StringUtils.isEmpty(key)) {
            titleMid.setText(key);
            tvSelfInfo.setText(key + "：");
        }
        if (!StringUtils.isEmpty(value)) {
            inputSelfInfo.setText(value);
        }
    }

    @Override
    protected void initEvent() {

        titleRight.setOnClickListener(new View.OnClickListener() {
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
