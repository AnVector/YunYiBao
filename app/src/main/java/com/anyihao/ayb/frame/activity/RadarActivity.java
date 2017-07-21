package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class RadarActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.btn_call)
    AppCompatButton btnCall;
    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;
    private String shopAddr;
    private String shopName;
    private String contact;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_radar;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        shopAddr = intent.getStringExtra("shopAddr");
        shopName = intent.getStringExtra("shopName");
        contact = intent.getStringExtra("contact");
    }

    @Override
    protected void initData() {
        toolbarTitleMid.setText(getString(R.string.radar));
        tvLocation.setText(shopAddr);
        tvShopName.setText(shopName);
        tvPhoneNo.setText(contact);
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }
}
