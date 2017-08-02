package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;

import butterknife.BindView;

public class TransRecordDetailsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ic_profile)
    ImageView icProfile;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.tv_value_date)
    TextView tvValueDate;
    @BindView(R.id.tv_value_account)
    TextView tvValueAccount;
    @BindView(R.id.tv_value_amount)
    TextView tvValueAmount;
    private String mProfile;
    private String mNickName;
    private String mAmount;
    private String mAccount;
    private String mDate;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trans_record_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mProfile = intent.getStringExtra("url");
        mNickName = intent.getStringExtra("nickname");
        mAmount = intent.getStringExtra("amount");
        mAccount = intent.getStringExtra("account");
        mDate = intent.getStringExtra("date");
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.transfer_record_details));
        tvTo.setText(mNickName);
        tvValueAmount.setText(mAmount);
        tvValueAccount.setText(mAccount);
        tvValueDate.setText(mDate);
        if (!TextUtils.isEmpty(mProfile)) {
            Glide.with(this)
                    .load(mProfile)
                    .crossFade()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.drawable.user_profile)
                    .into(icProfile);
        }
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
}
