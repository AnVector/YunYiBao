package com.anyihao.androidbase.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(),container,false);
        mContext = getContext();
        init();
        return mRootView;
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();
    protected abstract int getContentViewId();

    protected void init(){
        initView();
        initData();
        initEvent();
    }

    protected String getPageName(){
        return this.getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }
}
