package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RechargeRecordAdapter;
import com.anyihao.ayb.bean.RechargeRecordListBean;
import com.anyihao.ayb.bean.RechargeRecordListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview
        .StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RechargeRecordActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    private static final int PAGE_SIZE = 10;
    private RechargeRecordAdapter mRechargeAdapter;
    private LinearLayoutManager layoutManager;
    private List<RechargeRecordListBean.DataBean> mRechargeData = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge_record;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.recharge_record));
        initUltimateRV();
        getRechargeRecord();
    }

    private void initUltimateRV() {
        recyclerView.setHasFixedSize(false);
        mRechargeAdapter = new RechargeRecordAdapter(mRechargeData, R.layout
                .item_recharge_record);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
                (mRechargeAdapter);
        recyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null)
                    return;
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (imvError != null) {
                    imvError.setImageDrawable(getResources().getDrawable(R.drawable
                            .ic_no_recharge_record));
                }
                if (tvHint != null) {
                    tvHint.setText("暂无充值记录");
                }
            }
        });
        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mRechargeAdapter);
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
                recyclerView.reenableLoadmore();
                page = 1;
                isRefresh = true;
                getRechargeRecord();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                ++page;
                isRefresh = false;
                getRechargeRecord();
            }
        });

        mRechargeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof DataBean) {
                    Intent intent = new Intent(RechargeRecordActivity.this,
                            RechargeRecordDetailsActivity
                                    .class);
                    intent.putExtra("fee", ((DataBean) o).getAmount());
                    intent.putExtra("crtTime", ((DataBean) o).getCrtTm());
                    intent.putExtra("effectTm", ((DataBean) o).getEffecTm());
                    intent.putExtra("flow", ((DataBean) o).getFlow());
                    intent.putExtra("payType", ((DataBean) o).getTopupType());
                    intent.putExtra("idxOrderID", ((DataBean) o).getIdxOrderID());
                    intent.putExtra("status", ((DataBean) o).getStatus());
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
        if (recyclerView == null)
            return;
        mRechargeAdapter.removeAllInternal(mRechargeData);
        mRechargeAdapter.insert(beans);
        recyclerView.setRefreshing(false);
        layoutManager.scrollToPosition(0);
        if (beans.size() < PAGE_SIZE) {
            recyclerView.disableLoadmore();
        }
    }

    private void onLoadMore(List<DataBean> beans) {
        if (recyclerView == null)
            return;
        mRechargeAdapter.insert(beans);
        if (beans.size() < PAGE_SIZE) {
            recyclerView.disableLoadmore();
        }
    }

    private void onLoadNoData(int page) {
        if (recyclerView == null)
            return;
        recyclerView.disableLoadmore();
        if (page == 1) {
            recyclerView.showEmptyView();
        }
    }

    private void getRechargeRecord() {

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAYLIST");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("page", page + "");
        params.put("pagesize", PAGE_SIZE + "");
        params.put("flowType", "TOPUP");

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(page)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            RechargeRecordListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    RechargeRecordListBean.class);
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
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                onLoadNoData(page);
            }
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        super.onFailure(error, page, actionType);
        if (recyclerView == null)
            return;
        if (isRefresh) {
            recyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
//            recyclerView.showEmptyView();
        }
        recyclerView.disableLoadmore();
    }
}
