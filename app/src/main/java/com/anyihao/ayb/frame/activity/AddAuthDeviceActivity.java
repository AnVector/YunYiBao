package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class AddAuthDeviceActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_mac_address)
    EditText etMacAddress;
    @BindView(R.id.tv_help)
    TextView tvHelp;
    @BindView(R.id.btn_add_auth_device)
    AppCompatButton btnAddAuthDevice;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_auth_device;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitleMid.setText(getString(R.string.add_auth_device));


    }

    @Override
    protected void initEvent() {

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
