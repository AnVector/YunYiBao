package com.anyihao.ayb.frame.activity;

import android.graphics.Color;

import com.anyihao.androidbase.acitivity.BKBaseActivity;
import com.anyihao.androidbase.mvp.IView;
import com.anyihao.ayb.common.PresenterFactory;
import com.jaeger.library.StatusBarUtil;

public abstract class ABaseActivity extends BKBaseActivity implements IView<Integer> {

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setColor(ABaseActivity.this, Color.parseColor("#FFFFFF"), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PresenterFactory.getInstance().remove(this);
    }
}
