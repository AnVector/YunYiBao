package com.anyihao.ayb.frame.fragment;


import android.support.v4.app.Fragment;

import com.anyihao.androidbase.fragment.BKBaseFragment;
import com.anyihao.androidbase.mvp.IView;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.common.PresenterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ABaseFragment extends BKBaseFragment implements IView<Integer> {

    @Override
    public void onDestroy() {
        super.onDestroy();
        PresenterFactory.getInstance().remove(this);
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (TextUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "未知异常");
        } else if (error.contains("SocketTimeoutException")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "网络连接超时，请稍后重试");
        } else {
            ToastUtils.showToast(mContext.getApplicationContext(), error);
        }
    }
}
