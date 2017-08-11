package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MessageAdapter;
import com.anyihao.ayb.bean.MessageBean;
import com.anyihao.ayb.bean.MessageBean.DataBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.MessageDetailsActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MessageFragment extends ABaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;
    private MessageAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private List<DataBean> mData = new LinkedList<>();
    private String type;
    private int page = 1;
    private static final int PAGE_SIZE = 8;
    private boolean isRefresh;
    private boolean isInited = false;
    private boolean networkError;

    @Override
    protected void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            if (!isInited) {
                isInited = true;
                getMessage();
            }
        }
        initUltimateRV();
    }

    private void initUltimateRV() {
        ultimateRecyclerView.setHasFixedSize(false);
        mAdapter = new MessageAdapter(mData, R.layout.item_message);
        layoutManager = new LinearLayoutManager(mContext);
        ultimateRecyclerView.setLayoutManager(layoutManager);
        ultimateRecyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null)
                    return;
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (imvError != null && tvHint != null) {
                    if (networkError) {
                        tvHint.setText("网络异常");
                        imvError.setImageDrawable(getResources().getDrawable(R.drawable
                                .ic_no_network));
                    } else {
                        tvHint.setText("暂无消息");
                        imvError.setImageDrawable(getResources().getDrawable(R.drawable
                                .ic_no_message));
                    }

                }
            }
        });
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
                ultimateRecyclerView.reenableLoadmore();
                page = 1;
                isRefresh = true;
                getMessage();
            }
        });

        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                ++page;
                isRefresh = false;
                getMessage();
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

                Intent intent = new Intent();
                if (o instanceof DataBean) {
                    if (((DataBean) o).getStatus() == 0) {
                        view.findViewById(R.id.red_dot).setVisibility(View.GONE);
                    }
                    DataBean bean = (DataBean) o;
                    intent.setClass(mContext, MessageDetailsActivity.class);
                    intent.putExtra("details", bean);
                    flagMessage(bean.getKeyId());
                    startActivity(intent);
                }


            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {

                return false;
            }
        });

    }

    private void onFireRefresh(List<DataBean> beans) {
        if (ultimateRecyclerView == null)
            return;
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(beans);
        ultimateRecyclerView.setRefreshing(false);
        layoutManager.scrollToPosition(0);
        if (beans.size() < PAGE_SIZE) {
            ultimateRecyclerView.disableLoadmore();
        }
    }

    private void onLoadMore(List<DataBean> beans) {
        if (ultimateRecyclerView == null)
            return;
        mAdapter.insert(beans);
        if (beans.size() < PAGE_SIZE) {
            ultimateRecyclerView.disableLoadmore();
        }
    }

    private void onLoadNoData(int page) {
        isInited = false;
        if (ultimateRecyclerView == null)
            return;
        ultimateRecyclerView.disableLoadmore();
        if (page == 1) {
            ultimateRecyclerView.showEmptyView();
        }
    }

    private void getMessage() {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "INFORMATION");
        params.put("page", page + "");
        params.put("pagesize", PAGE_SIZE + "");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("type", type);
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setParams(params)
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setPage(page)
                .setActionType(0)
                .createTask());
    }

    private void flagMessage(int keyId) {

        if (TextUtils.isEmpty(type)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MSGRESULT");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("type", type);
        params.put("keyId", keyId + "");

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setParams(params)
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setPage(1)
                .setActionType(1)
                .createTask());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        networkError = false;
        if (actionType == 0) {
            MessageBean bean = GsonUtils.getInstance().transitionToBean(result, MessageBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans == null)
                    return;
                if (beans.size() > 0) {
                    if (isRefresh) {
                        onFireRefresh(beans);
                    } else {
                        onLoadMore(beans);
                    }
                } else {
                    onLoadNoData(page);
                }
            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
                onLoadNoData(page);
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                Logger.d(TAG, "消息标记为已读成功");
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        super.onFailure(error, page, actionType);
        if (ultimateRecyclerView == null)
            return;
        if (isRefresh) {
            networkError = true;
            ultimateRecyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
//            ultimateRecyclerView.showEmptyView();
        }
        ultimateRecyclerView.disableLoadmore();

    }
}