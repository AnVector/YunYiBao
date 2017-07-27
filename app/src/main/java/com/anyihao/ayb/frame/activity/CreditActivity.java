package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.CreditAdapter;
import com.anyihao.ayb.bean.CreditBean;
import com.anyihao.ayb.bean.CreditBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.NumberRunningTextView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CreditActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    private NumberRunningTextView tvPoints;
    private LinearLayoutManager layoutManager;
    private CreditAdapter mAdapter;
    private List<DataBean> mData = new ArrayList<>();
    private String mCredit;
    private boolean isRefresh;
    private int page = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_credit;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mCredit = intent.getStringExtra("integral");
        if (!StringUtils.isEmpty(mCredit)) {
            mCredit = mCredit.replace(" 积分", "");
        }
    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitle.setText(getString(R.string.my_points));
        initUltimateRV();
        getCredits();
    }

    private void initUltimateRV() {
        recyclerView.setHasFixedSize(false);
        mAdapter = new CreditAdapter(mData, R.layout.item_credit);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
                            .ic_no_message));
                }
                if (tvHint != null) {
                    tvHint.setText("暂无积分记录");
                }
            }
        });
        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        View headerView = getLayoutInflater().inflate(R.layout
                .view_credit_header_layout, recyclerView.mRecyclerView, false);
        tvPoints = (NumberRunningTextView) headerView.findViewById(R.id.tv_points);
        if (tvPoints != null) {
            Typeface fontFace = Typeface.createFromAsset(getAssets(),
                    "fonts/W13.TTF");
            tvPoints.setTypeface(fontFace);
            tvPoints.setContent(mCredit);
        }
        recyclerView.setNormalHeader(headerView);
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
                getCredits();
            }
        });

        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                ++page;
                isRefresh = false;
                getCredits();
            }
        });

    }

    private void onFireRefresh(List<DataBean> beans) {
        mData.clear();
        mData.addAll(beans);
        mAdapter.notifyDataSetChanged();
        recyclerView.setRefreshing(false);
        layoutManager.scrollToPosition(0);
        recyclerView.reenableLoadmore();
    }

    private void onLoadMore(List<DataBean> beans) {
        mAdapter.insert(beans);
        if (beans.size() < PAGE_SIZE) {
            recyclerView.disableLoadmore();
        }
    }

    private void onLoadNoData() {
        if (recyclerView != null) {
            recyclerView.showEmptyView();
        }
    }

    private void getCredits() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MYSCORES");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("page", page + "");
        params.put("pagesize", PAGE_SIZE + "");
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(page)
                        .setActionType(0)
                        .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            CreditBean bean = GsonUtils.getInstance().transitionToBean(result, CreditBean.class);
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
