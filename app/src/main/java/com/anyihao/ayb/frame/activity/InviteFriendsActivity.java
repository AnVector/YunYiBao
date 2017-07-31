package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ShareBean;
import com.anyihao.ayb.bean.ShareTypeBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class InviteFriendsActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.tv_share_count)
    TextView tvShareCount;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.tv_remind)
    TextView tvRemind;
    private String mIntegral;
    private String mFriends;
    private UMWeb web;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        mFriends = getString(R.string.share_account);
        mIntegral = getString(R.string.remind_words);
        setTextFont(inviteCodeHint);
        setTextFont(tvInviteCode);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setBackground(null);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.invite_friends));
        titleMid.setTextColor(Color.parseColor("#FFFFFF"));
        getInviteCode();
        getShareContent();
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
                new ShareAction(InviteFriendsActivity.this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(shareListener).share();
            }
        });

        shareWxqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(InviteFriendsActivity.this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(shareListener).share();
            }
        });

        shareWbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(InviteFriendsActivity.this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(shareListener).share();
            }
        });

        shareQqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(InviteFriendsActivity.this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(shareListener).share();
            }
        });

        shareQqzoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(InviteFriendsActivity.this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(shareListener).share();
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showToast(getApplicationContext(), "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (StringUtils.isEmpty(t.toString()))
                return;
            if (t.toString().contains("2008")) {
                ToastUtils.showToast(getApplicationContext(), "应用未安装");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showToast(getApplicationContext(), "分享取消");
        }
    };

    private void setTextFont(TextView tv) {
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/W12.ttc");
        tv.setTypeface(fontFace);
    }

    private void getInviteCode() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "SHARETYPE");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("shareType", "1");
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void getShareContent() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "SHR");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("keyId", "1");
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(1)
                        .createTask());
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTransparent(InviteFriendsActivity.this);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            ShareBean bean = GsonUtils.getInstance().transitionToBean(result, ShareBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                tvShareCount.setText(String.format(mFriends, bean.getNumber() + ""));
                tvInviteCode.setText(bean.getReqCode());
                tvRemind.setText(String.format(mIntegral, bean.getIntegral() + ""));
            }
        }

        if (actionType == 1) {
            ShareTypeBean bean = GsonUtils.getInstance().transitionToBean(result, ShareTypeBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                web = new UMWeb(bean.getShareUrl());
                web.setTitle(bean.getShareTitle());
                web.setDescription(bean.getShareContent());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
