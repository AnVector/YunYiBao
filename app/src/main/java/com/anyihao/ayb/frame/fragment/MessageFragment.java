package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
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
import com.anyihao.ayb.frame.activity.MessageDetailsActivity;
import com.anyihao.ayb.frame.activity.RedEnvelopeActivity;
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
    UltimateRecyclerView recyclerView;
    private MessageAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private List<DataBean> mData = new ArrayList<>();
    private String type;
    private int page = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            getMessage();
        }

        recyclerView.setHasFixedSize(false);
        mAdapter = new MessageAdapter(mData);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
//                (informationAdapter);
//        ultimateRecyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setEmptyView(R.layout.empty_view_no_message, UltimateRecyclerView
                .EMPTY_CLEAR_ALL);
//        recyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout
//                .parallax_recyclerview_header, recyclerView.mRecyclerView, false));
//        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback
                (mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView.mRecyclerView);
//        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        recyclerView.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
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
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {

            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, RedEnvelopeActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, MessageDetailsActivity.class);
                startActivity(intent);
                return false;
            }
        });

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

        if (actionType == 0) {
            MessageBean bean = GsonUtils.getInstance().transitionToBean(result, MessageBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans.size() > 0) {
                    ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg(), R.layout
                            .toast, R.id.tv_message);
                    mData.clear();
                    mData.addAll(beans);
                } else {
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
