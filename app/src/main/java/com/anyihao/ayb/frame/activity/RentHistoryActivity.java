package com.anyihao.ayb.frame.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RentHistoryAdapter;
import com.anyihao.ayb.bean.RentHistoryListBean;
import com.anyihao.ayb.bean.RentHistoryListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RentHistoryActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    private static final int PAGE_SIZE = 10;
    private RentHistoryAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<DataBean> mData = new ArrayList<>();
    private List<DataBean> mItems;
    private int page = 1;
    private boolean isRefresh;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_rent_history;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.rented_history));
        initUltimateRV();
        getRentHistory();
    }

    private void initUltimateRV() {
        recyclerView.setHasFixedSize(false);
        mAdapter = new RentHistoryAdapter(mData, R.layout.item_rent_history);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //bug 设置加载更多动画会使添加的数据延迟显示
//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null)
                    return;
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                if (imvError != null) {
                    imvError.setImageDrawable(getResources().getDrawable(R.drawable
                            .ic_no_rent_record));
                }
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (tvHint != null) {
                    tvHint.setText("暂无租赁记录");
                }
            }
        });
        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                getRentHistory();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                ++page;
                isRefresh = false;
                getRentHistory();
            }
        });
    }

    private void onFireRefresh() {
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(mItems);
        recyclerView.setRefreshing(false);
        layoutManager.scrollToPosition(0);
    }

    private void onLoadMore() {
        mAdapter.insert(mItems);
        if (mItems.size() < PAGE_SIZE) {
            recyclerView.disableLoadmore();
        }
    }

    private void getRentHistory() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "LEASERCD");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("page", page + "");
        params.put("pagesize", PAGE_SIZE + "");
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            RentHistoryListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    RentHistoryListBean.class);
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
                }
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }
}
