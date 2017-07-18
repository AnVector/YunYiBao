package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.ui.pagetransformer.DepthPageTransformer;

import butterknife.BindView;


public class GuideActivity extends ABaseActivity {

    private static final int[] mGuidePics = {R.drawable.guide_pic_1,
            R.drawable.guide_pic_2, R.drawable.guide_pic_3};
    @BindView(R.id.guide_viewpager)
    ViewPager mGuideViewpager;
    private AppCompatButton btnGo;
    private int currentIndex = 0;

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
    }

    @Override
    protected void setStatusBarTheme() {
    }

    private void initViewPager() {
//        mGuideViewpager.setPageTransformer(true, new DepthPageTransformer());
        mGuideViewpager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View contentView = getLayoutInflater().inflate(R.layout.guide_layout, null);
                ImageView img = (ImageView) contentView.findViewById(R.id.iv_guide);
                if (position == 2) {
                    btnGo = (AppCompatButton) contentView.findViewById(R.id.btn_go);
                    btnGo.setVisibility(View.VISIBLE);
                    btnGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentIndex == 2) {
                                Intent intent = new Intent(GuideActivity.this,
                                        MainFragmentActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                img.setImageResource(mGuidePics[position]);
                container.setTag(position);//为container添加tag
                container.addView(contentView);
                return contentView;
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
}
