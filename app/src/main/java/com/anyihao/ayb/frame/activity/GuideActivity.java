package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anyihao.ayb.R;

import butterknife.BindView;


public class GuideActivity extends ABaseActivity {

    private static final int[] mGuidePics = {R.drawable.guide_pic_1,
            R.drawable.guide_pic_2, R.drawable.guide_pic_3};
    @BindView(R.id.guide_viewpager)
    ViewPager mGuideViewpager;
    @BindView(R.id.btn_go)
    AppCompatButton btnGo;
    private int currentIndex = 0;

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
                currentIndex = (Integer) v.getTag();
                return false;
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == 2) {
                    Intent intent = new Intent(GuideActivity.this, MainFragmentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initViewPager() {

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
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
