package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class ShareActivity extends ABaseActivity {

    @BindView(R.id.menu_bottom)
    TextView menuBottom;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_share;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(ShareActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.MORE)
                .addButton("umeng_sharebutton_copy", "umeng_sharebutton_copy",
                        "umeng_socialize_copy", "umeng_socialize_copy")
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl",
                        "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {
                            ToastUtils.showLongToast(ShareActivity.this, "复制文本按钮");
                        } else if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            ToastUtils.showLongToast(ShareActivity.this, "复制链接按钮");

                        } else {
                            new ShareAction(ShareActivity.this).withText("来自友盟自定义分享面板")
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });

    }

    @Override
    protected void initEvent() {
        menuBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareBoardConfig config = new ShareBoardConfig();
//                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
//                mShareAction.open(config);
                mShareAction.open();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<ShareActivity> mActivity;

        private CustomShareListener(ShareActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                ToastUtils.showLongToast(mActivity.get(), platform + " 收藏成功啦");
            } else {
                ToastUtils.showLongToast(mActivity.get(), platform + " 分享成功啦");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            ToastUtils.showLongToast(mActivity.get(), platform + " 分享失败啦");
            if (t != null) {
                LogUtils.d("throw", "throw:" + t.getMessage());
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showLongToast(mActivity.get(), platform + " 分享取消了");
        }
    }

}
