package com.anyihao.ayb.frame.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends ABaseFragment {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initData() {
        titleMid.setText(getString(R.string.task));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_task;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
