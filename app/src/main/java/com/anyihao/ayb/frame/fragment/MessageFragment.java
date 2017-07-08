package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MessageAdapter;
import com.anyihao.ayb.bean.MessageBean;
import com.anyihao.ayb.bean.MessageBean.DataBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.MessageDetailsActivity;
import com.anyihao.ayb.frame.activity.RedEnvelopeActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;

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
    private ItemTouchHelper mItemTouchHelper;
    private List<DataBean> mData = new LinkedList<>();
    private List<DataBean> mNormalMsg;
    private String type;
    private int page = 1;
    private static final int PAGE_SIZE = 8;
    private boolean isRefresh;

    @Override
    protected void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            getMessage();
        }

        ultimateRecyclerView.setHasFixedSize(false);
        mAdapter = new MessageAdapter(mData, R.layout.item_message);
        layoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(layoutManager);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
//                (informationAdapter);
//        ultimateRecyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
        ultimateRecyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null)
                    return;
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                if (imvError == null)
                    return;
                imvError.setImageDrawable(getResources().getDrawable(R.drawable
                        .ic_no_message));
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (tvHint == null)
                    return;
                tvHint.setText("暂无消息");
            }
        });
//        recyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout
//                .parallax_recyclerview_header, recyclerView.mRecyclerView, false));
//        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback
                (mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(ultimateRecyclerView.mRecyclerView);
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        ultimateRecyclerView.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
            }
        });
        mAdapter.setOnDragStartListener(new UltimateViewAdapter.OnStartDragListener() {

            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
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
                    DataBean bean = (DataBean) o;
                    if ("PERSON".equals(type) || "SYSTEM".equals(type)) {
                        intent.setClass(mContext, MessageDetailsActivity.class);
                        intent.putExtra("details", bean);
                        flagMessage(bean.getKeyId());
                    } else {
                        intent.setClass(mContext, RedEnvelopeActivity.class);
                    }
                    startActivity(intent);
                }


            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {

                return false;
            }
        });

    }

    private void onFireRefresh() {
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(mNormalMsg);
        ultimateRecyclerView.setRefreshing(false);
        //   ultimateRecyclerView.scrollBy(0, -50);
        layoutManager.scrollToPosition(0);
//        recyclerView.scrollVerticallyTo(0);
        //ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
        //simpleRecyclerViewAdapter.notifyDataSetChanged();
//        ultimateRecyclerView.disableLoadmore();
    }

    private void onLoadMore() {
        mAdapter.insert(mNormalMsg);
        if (mNormalMsg.size() < PAGE_SIZE) {
            ultimateRecyclerView.disableLoadmore();
        }
    }

    private void onLoadNoData() {
        ToastUtils.showToast(mContext.getApplicationContext(), "暂无消息", R.layout
                .toast, R.id.tv_message);
        ultimateRecyclerView.showEmptyView();
    }

    private void getMessage() {
        if (StringUtils.isEmpty(type)) {
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

        if (StringUtils.isEmpty(type)) {
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

        if (actionType == 0) {
            MessageBean bean = GsonUtils.getInstance().transitionToBean(result, MessageBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans.size() > 0) {
                    mNormalMsg = beans;
                    if (isRefresh) {
                        onFireRefresh();
                    } else {
                        onLoadMore();
                    }
                } else {
                    if (page == 1) {
                        onLoadNoData();
                    }

                }
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                Log.d(TAG, "消息标记为已读成功");
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "网络连接失败，请检查网络设置");
            ultimateRecyclerView.showEmptyView();
        } else if (error.contains("404")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(mContext.getApplicationContext(), error);
        }
    }
}
