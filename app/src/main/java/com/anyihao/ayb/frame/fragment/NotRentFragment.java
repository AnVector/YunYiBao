package com.anyihao.ayb.frame.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.NotRentAdapter;
import com.anyihao.ayb.bean.NotRentBean;
import com.anyihao.ayb.bean.NotRentBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class NotRentFragment extends ABaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;
    private NotRentAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private List<DataBean> mData = new ArrayList<>();
    private String status;
    private boolean isRefresh;

    @Override
    protected void initData() {
        initUltimateRV();
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString("status");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getBelongs();
    }

    private void initUltimateRV() {
        ultimateRecyclerView.setHasFixedSize(false);
        mAdapter = new NotRentAdapter(mData, R.layout.item_not_rent_device);
        layoutManager = new LinearLayoutManager(mContext);
        ultimateRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null) {
                    return;
                }
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (imvError != null) {
                    imvError.setImageDrawable(getResources().getDrawable(R.drawable
                            .ic_no_auth_devices));
                }
                if (tvHint != null) {
                    tvHint.setText("暂无设备");
                }
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    private void getBelongs() {
        if (TextUtils.isEmpty(status)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MERCHANTDEVLIST");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("status", status);
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .createTask());
    }

    private void onLoadMore(List<DataBean> beans) {
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(beans);
    }

    private void onFireRefresh(List<DataBean> beans) {
        if (ultimateRecyclerView == null) {
            return;
        }
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(beans);
        ultimateRecyclerView.setRefreshing(false);
        layoutManager.scrollToPosition(0);
    }

    private void onLoadNoData() {
        if (ultimateRecyclerView == null) {
            return;
        }
        ultimateRecyclerView.showEmptyView();
    }

    @Override
    protected void initEvent() {
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getBelongs();
            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            NotRentBean bean = GsonUtils.getInstance().transitionToBean(result, NotRentBean.class);
            if (bean == null) {
                return;
            }
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans == null) {
                    return;
                }
                if (beans.size() > 0) {
                    if (isRefresh) {
                        onFireRefresh(beans);
                    } else {
                        onLoadMore(beans);
                    }
                } else {
                    onLoadNoData();
                }
            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
                onLoadNoData();
            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        super.onFailure(error, page, actionType);
        if (ultimateRecyclerView == null) {
            return;
        }
        if (isRefresh) {
            ultimateRecyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
        }
    }
}
