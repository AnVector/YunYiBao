package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.MessageBean.DataBean;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;

import butterknife.BindView;

public class MessageDetailsActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    private DataBean messageBean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_message_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        messageBean = (DataBean) intent.getSerializableExtra("details");
    }

    @Override
    protected void initData() {

        if (messageBean != null) {
            tvUserName.setText(messageBean.getSendName());
            Glide.with(this)
                    .load(messageBean.getSendAvatar())
                    .crossFade()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.drawable.user_profile)
                    .into(ivUserProfile);
            tvMessage.setText(messageBean.getMessage());
            tvDate.setText(messageBean.getCrtTm());
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.message_details));
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
