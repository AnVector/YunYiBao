package com.anyihao.androidbase.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

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

    protected void permissionsRequest(int requestCode, String decription, String... permissions) {
        if (requestCode == -1 || permissions == null || permissions.length == 0 || getActivity()
                == null)
            return;
        if (EasyPermissions.hasPermissions(getContext(), permissions)) {
            // Have permission, do the thing!
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, decription,
                    requestCode, permissions);
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }
}
