package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.SwipeCardBean;
import com.anyihao.ayb.listener.TanTanCallback;
import com.bumptech.glide.Glide;
import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.mcxtzhang.layoutmanager.swipecard.CardConfig;
import com.mcxtzhang.layoutmanager.swipecard.OverLayCardLayoutManager;

import java.util.List;

import butterknife.BindView;

public class DetailActivity extends ABaseActivity {

    private CommonAdapter<SwipeCardBean> mAdapter;
    private List<SwipeCardBean> mDatas;
    private String id = null;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    private static final String TAG = "zxt/Adapter";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_detail;
    }
    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        Bundle bundle;
        if(null!=intent){
            bundle = intent.getExtras();
            if(null!=bundle){
                id = bundle.getString("id");
            }
        }
        if(null!=id){
            ToastUtils.showShortToast(this,"第"+id+"条数据详细信息");
        }
    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new OverLayCardLayoutManager());
    }

    @Override
    protected void initEvent() {

        mRecyclerView.setAdapter(mAdapter = new CommonAdapter<SwipeCardBean>(this, mDatas = SwipeCardBean.initDatas(), R.layout.item_swipe_card) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder() called with: parent = [" + parent + "], viewType = [" + viewType + "]");
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
                super.onBindViewHolder(holder, position);
            }

            @Override
            public void convert(ViewHolder viewHolder, SwipeCardBean swipeCardBean) {
                Log.d(TAG, "convert() called with: viewHolder = [" + viewHolder + "], swipeCardBean = [" + swipeCardBean + "]");
                viewHolder.setText(R.id.tvName, swipeCardBean.getName());
                viewHolder.setText(R.id.tvPrecent, swipeCardBean.getPostition() + " /" + mDatas.size());
                Glide.with(DetailActivity.this).load(swipeCardBean.getUrl()).into((ImageView) viewHolder.getView(R.id.iv));
            }
        });

        CardConfig.initConfig(this);

        final TanTanCallback callback = new TanTanCallback(mRecyclerView, mAdapter, mDatas);

        //测试竖直滑动是否已经不会被移除屏幕
        //callback.setHorizontalDeviation(Integer.MAX_VALUE);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
