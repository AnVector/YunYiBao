package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.ayb.R;
import com.anyihao.ayb.ui.RoundedCornersTransformation;
import com.anyihao.ayb.utils.CodeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.BindView;

public class QRActivity extends ABaseActivity {

    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String uid;
    private String mAvatar;
    private String mNickName;
    private String mZone;
    private String mGender;
    private Bitmap mProfile;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_qr;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        uid = intent.getStringExtra("uid");
        mAvatar = intent.getStringExtra("avatar");
        mNickName = intent.getStringExtra("nickname");
        mGender = intent.getStringExtra("gender");
        mZone = intent.getStringExtra("zone");
    }

    @Override
    protected void initData() {

        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbarTitleMid.setText(getString(R.string.qr_code_of_mine));
        toolbarTitleMid.setTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setBackgroundColor(Color.parseColor("#000000"));

        if (!TextUtils.isEmpty(mAvatar)) {
            getProfileBitmap(mAvatar);
            Glide.with(this).load(mAvatar)
                    .placeholder(R.drawable.user_profile)
                    .bitmapTransform(new RoundedCornersTransformation(this, 16, 0,
                            RoundedCornersTransformation.CornerType.ALL)).crossFade().into
                    (ivUserProfile);
        }

        if (!TextUtils.isEmpty(mNickName)) {
            tvNickName.setText(mNickName);
        }
        if (!TextUtils.isEmpty(mGender)) {
            if ("女".equals(mGender)) {
                ivSex.setImageResource(R.drawable.ic_female);
            } else {
                ivSex.setImageResource(R.drawable.ic_male);
            }
        }
        if (!TextUtils.isEmpty(mZone)) {
            tvZone.setText(mZone);
        }
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setColor(this, Color.parseColor("#000000"), 0);
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

    private void getProfileBitmap(String url) {
        Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                    glideAnimation) {
                mProfile = getRoundedCornerBitmap(resource, 8);
                if (!TextUtils.isEmpty(uid)) {
                    ivQrCode.setImageBitmap(CodeUtils.createImage(uid, 240, 240, mProfile));
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
            }
        });
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null)
            return null;
        int radix = Math.max(bitmap.getWidth(), bitmap.getWidth());
        Bitmap output = Bitmap.createBitmap(radix, radix, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radix, radix);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }
}
