package com.anyihao.ayb.frame.activity;

import android.graphics.Color;
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
import com.anyihao.ayb.adapter.TransferRecordAdapter;
import com.anyihao.ayb.bean.TransferListBean;
import com.anyihao.ayb.bean.TransferListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TransferRecordActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    private static final int PAGE_SIZE = 20;
    private TransferRecordAdapter mTransferAdapter;
    private LinearLayoutManager layoutManager;
    private List<TransferListBean.DataBean> mTransferData = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge_record;
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
        titleMid.setText(getString(R.string.transfer_record));
        initUltimateRV();
        getPresentRecord();
    }

    private void initUltimateRV() {
        recyclerView.setHasFixedSize(false);
        mTransferAdapter = new TransferRecordAdapter(mTransferData, R.layout
                .item_recharge_record);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
                            .ic_no_transfer_record));
                }
                if (tvHint != null) {
                    tvHint.setText("暂无转赠记录");
                }
            }
        });
        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mTransferAdapter);
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
                getPresentRecord();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                ++page;
                isRefresh = false;
                getPresentRecord();
            }
        });

        mTransferAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof DataBean) {

                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });


    }

    private void onFireRefresh(List<DataBean> beans) {
        mTransferAdapter.removeAllInternal(mTransferData);
        mTransferAdapter.insert(beans);
        recyclerView.setRefreshing(false);
        layoutManager.scrollToPosition(0);
        recyclerView.reenableLoadmore();
    }

    private void onLoadMore(List<DataBean> beans) {
        mTransferAdapter.insert(beans);
        if (beans.size() < PAGE_SIZE) {
            recyclerView.disableLoadmore();
        }
    }

    private void onLoadNoData() {
        if (recyclerView == null)
            return;
        recyclerView.showEmptyView();

    }

    private void getPresentRecord() {

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "TRANSFER");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("page", page + "");
        params.put("pagesize", PAGE_SIZE + "");

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
            TransferListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    TransferListBean.class);
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
                    if (page == 1) {
                        onLoadNoData();
                    }
                }
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                if (page == 1) {
                    onLoadNoData();
                }
            }
        }
    }
}
