package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.ui.pagetransformer.DepthPageTransformer;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;


public class GuideActivity extends ABaseActivity {

    private static final int[] mGuidePics = {R.drawable.guide_pic_1,
            R.drawable.guide_pic_2, R.drawable.guide_pic_3, 0};
    @BindView(R.id.guide_viewpager)
    ViewPager mGuideViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
    }

    @Override
    protected void initEvent() {
        mGuideViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((Integer) v.getTag() == 3) {
//                    Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                    Intent intent = new Intent(GuideActivity.this, MainFragmentActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    private void initViewPager() {

        mGuideViewpager.setPageTransformer(false, new DepthPageTransformer());
        mGuideViewpager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView img = new ImageView(GuideActivity.this);
                img.setImageResource(mGuidePics[position]);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.setTag(position);//为container添加tag
                container.addView(img);
                return img;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return mGuidePics.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
