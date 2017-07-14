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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.ui.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

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

        if (!StringUtils.isEmpty(mAvatar)) {
            getProfileBitmap(mAvatar);
            Glide.with(this).load(mAvatar)
                    .placeholder(R.drawable.user_profile)
                    .bitmapTransform(new RoundedCornersTransformation(this, 8, 0,
                            RoundedCornersTransformation.CornerType.ALL)).crossFade().into
                    (ivUserProfile);
        }

        if (!StringUtils.isEmpty(mNickName)) {
            tvNickName.setText(mNickName);
        }
        if (!StringUtils.isEmpty(mGender)) {
            if ("女".equals(mGender)) {
                ivSex.setImageResource(R.drawable.ic_female);
            } else {
                ivSex.setImageResource(R.drawable.ic_male);
            }
        }
        if (!StringUtils.isEmpty(mZone)) {
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
                mProfile = getRoundedCornerBitmap(resource, 16);
                if (!StringUtils.isEmpty(uid)) {
                    ivQrCode.setImageBitmap(createQRImage(uid, 280, 280, mProfile));
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
            }
        });
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public static Bitmap createQRImage(String content, int heightPix, int widthPix, Bitmap
            logoBm) {
        try {
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            // hints.put(EncodeHintType.MARGIN, 2); //default is 4
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE,
                    widthPix, heightPix, hints);
            int[] pixels = new int[heightPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < heightPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * heightPix + x] = 0xff000000;
                    } else {
                        pixels[y * heightPix + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, heightPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
