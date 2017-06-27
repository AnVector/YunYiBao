package com.anyihao.ayb.frame.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

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
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MessageFragment extends ABaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;
    private MessageAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private List<DataBean> mData = new ArrayList<>();
    private List<DataBean> mItems;
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
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_CLEAR_ALL);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
//                (informationAdapter);
//        ultimateRecyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
        ultimateRecyclerView.setDefaultSwipeToRefreshColorScheme(R.array.gplus_colors);
        ultimateRecyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        ultimateRecyclerView.setEmptyView(R.layout.empty_view_no_message, UltimateRecyclerView
                .EMPTY_CLEAR_ALL);
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
//                Intent intent = new Intent(mContext, RedEnvelopeActivity.class);
//                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
//                Intent intent = new Intent(mContext, MessageDetailsActivity.class);
//                startActivity(intent);
                return false;
            }
        });

    }

    private void onFireRefresh() {
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(mItems);
        ultimateRecyclerView.setRefreshing(false);
        //   ultimateRecyclerView.scrollBy(0, -50);
        layoutManager.scrollToPosition(0);
//        recyclerView.scrollVerticallyTo(0);
        //ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
        //simpleRecyclerViewAdapter.notifyDataSetChanged();
//        ultimateRecyclerView.disableLoadmore();
    }

    private void onLoadMore() {
        mAdapter.insert(mItems);
        if (mItems.size() < PAGE_SIZE) {
            ultimateRecyclerView.disableLoadmore();
        }
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

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        MessageBean bean = GsonUtils.getInstance().transitionToBean(result, MessageBean.class);
        if (bean == null)
            return;
        if (bean.getCode() == 200) {
            List<DataBean> beans = bean.getData();
            if (beans.size() > 0) {
                mItems = beans;
                if (isRefresh) {
                    onFireRefresh();
                } else {
                    onLoadMore();
                }
            } else {
                if (page == 1) {
                    ToastUtils.showToast(mContext.getApplicationContext(), "暂无消息", R.layout
                            .toast, R.id.tv_message);
                }

            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
