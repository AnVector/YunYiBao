package com.anyihao.ayb.frame.activity;

import com.anyihao.androidbase.acitivity.BKBaseActivity;
import com.anyihao.androidbase.mvp.IView;
import com.anyihao.androidbase.utils.StatusBarUtils;
import com.anyihao.ayb.common.PresenterFactory;

public abstract class ABaseActivity extends BKBaseActivity implements IView<Integer> {

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtils.setTransparent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PresenterFactory.getInstance().remove(this);
    }
}
