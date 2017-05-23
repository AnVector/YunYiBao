package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;

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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_message_details;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {

        toolbar.setNavigationIcon(R.drawable.ic_back);
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

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
