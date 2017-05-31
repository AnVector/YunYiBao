package com.anyihao.ayb.frame.activity;

import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class InviteFriendsActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.invite_code_tv)
    TextView inviteCodeTv;
    @BindView(R.id.share_count_tv)
    TextView shareCountTv;
    @BindView(R.id.share_wxf_btn)
    ImageButton shareWxfBtn;
    @BindView(R.id.share_wxq_btn)
    ImageButton shareWxqBtn;
    @BindView(R.id.share_wb_btn)
    ImageButton shareWbBtn;
    @BindView(R.id.share_qq_btn)
    ImageButton shareQqBtn;
    @BindView(R.id.share_qqzone_btn)
    ImageButton shareQqzoneBtn;
    @BindView(R.id.invite_code_hint)
    TextView inviteCodeHint;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        setTextFont(inviteCodeHint);
        setTextFont(inviteCodeTv);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        titleMid.setText(getString(R.string.invite_friends));
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shareWxfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        shareWxqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        shareWbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        shareQqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        shareQqzoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setTextFont(TextView tv) {
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/W12.ttc");
        tv.setTypeface(fontFace);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
