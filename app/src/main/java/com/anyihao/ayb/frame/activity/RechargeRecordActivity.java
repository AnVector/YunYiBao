package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.anyihao.ayb.adapter.RechargeRecordAdapter;
import com.anyihao.ayb.adapter.TransferRecordAdapter;
import com.anyihao.ayb.bean.RechargeRecordListBean;
import com.anyihao.ayb.bean.RechargeRecordListBean.DataBean;
import com.anyihao.ayb.bean.TransferListBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;
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
    private TransferRecordAdapter mTransferAdapter;
    private LinearLayoutManager layoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private List<RechargeRecordListBean.DataBean> mRechargeData = new ArrayList<>();
    private List<TransferListBean.DataBean> mTransferData = new ArrayList<>();
    private List<DataBean> mItems;
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
        titleMid.setText(getString(R.string.recharge_record));
        recyclerView.setHasFixedSize(false);
        mRechargeAdapter = new RechargeRecordAdapter(mRechargeData, R.layout
                .item_recharge_record);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
//                (informationAdapter);
//        ultimateRecyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null)
                    return;
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                if (imvError == null)
                    return;
                imvError.setImageDrawable(getResources().getDrawable(R.drawable
                        .ic_no_recharge_record));
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (tvHint == null)
                    return;
                tvHint.setText("暂无充值记录");
            }
        });
//        recyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout
//                .parallax_recyclerview_header, recyclerView.mRecyclerView, false));
//        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRechargeAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView.mRecyclerView);
        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mRechargeAdapter);
        getRechargeRecord();
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
            }
        });
        mRechargeAdapter.setOnDragStartListener(new UltimateViewAdapter.OnStartDragListener() {

            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
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
                    intent.putExtra("idxOrderID", ((DataBean) o).getIdxOrderID());
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
        mRechargeAdapter.removeAllInternal(mRechargeData);
        mRechargeAdapter.insert(mItems);
        recyclerView.setRefreshing(false);
        //   ultimateRecyclerView.scrollBy(0, -50);
        layoutManager.scrollToPosition(0);
//        recyclerView.scrollVerticallyTo(0);
        //ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
        //simpleRecyclerViewAdapter.notifyDataSetChanged();
//        ultimateRecyclerView.disableLoadmore();
    }

    private void onLoadMore() {
        mRechargeAdapter.insert(mItems);
        if (mItems.size() < PAGE_SIZE) {
            recyclerView.disableLoadmore();
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
                .setActionType(1)
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
                List<RechargeRecordListBean.DataBean> beans = bean.getData();
                if (beans.size() > 0) {
                    ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                    mItems = beans;
                    if (isRefresh) {
                        onFireRefresh();
                    } else {
                        onLoadMore();
                    }
                } else {
                    if (page == 1) {
                        ToastUtils.showToast(getApplicationContext(), "暂无充值记录");
                    }

                }
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(getApplicationContext(), error);
        }
    }
}
