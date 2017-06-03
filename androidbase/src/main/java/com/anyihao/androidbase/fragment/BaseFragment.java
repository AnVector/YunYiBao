package com.anyihao.androidbase.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhy.m.permission.MPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected View mRootView;
    protected String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        mContext = getContext();
        init(savedInstanceState);
        return mRootView;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract int getContentViewId();

    protected void saveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;
        Logger.d(TAG, "saveInstanceState: " + savedInstanceState);
    }

    protected void init(Bundle savedInstanceState) {
        TAG = getPageName();
        initView();
        initData();
        saveInstanceState(savedInstanceState);
        initEvent();
    }

    protected String getPageName() {
        return this.getClass().getSimpleName();
    }

    protected void permissionsRequest(int requestCode, String... permissions) {
        if (requestCode == -1 || permissions == null || permissions.length == 0 || getActivity()
                == null)
            return;
        if (!MPermissions.shouldShowRequestPermissionRationale(getActivity(), permissions[0],
                requestCode)) {
            MPermissions.requestPermissions(this, requestCode, permissions);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
