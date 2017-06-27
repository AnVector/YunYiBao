package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RdoCashRatioAdapter;
import com.anyihao.ayb.bean.SuccessRatioBean;
import com.anyihao.ayb.bean.SuccessRatioBean.DataBean.ClassificationBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview
        .StickyRecyclerHeadersDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;


public class ReportActivity extends ABaseActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    public static final String SERVICE_TYPE = "service_type";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CLIENT_ID = "client_id";
    public static final String EXCLUDE_AEE = "exclude_aee";
    public static final String CUSTOMER = "customer";
    public static final String SERVICE = "service";
    protected RdoCashRatioAdapter mAdapter = null;
    protected LinearLayoutManager layoutManager;
    @BindView(R.id.circle_progress_bar)
    CircularProgressBar progressBar;
    @BindView(R.id.tv_loading_hint)
    TextView tvLoadingHint;
    private ItemTouchHelper mItemTouchHelper;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<SuccessRatioBean> ratioBeans;
    private List<ClassificationBean> data = new LinkedList<>();
    List<ClassificationBean> beans = new LinkedList<>();
    private JSONObject json;
    private int page = 1;
    private int type = 0;
    private int size = 0;
    private String serviceType;
    private String fromDate;
    private String toDate;
    private String customerId;
    private String clientId;
    private int excludeAee;
    private String customer;
    private String service;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_report;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        serviceType = intent.getStringExtra(SERVICE_TYPE);
        fromDate = intent.getStringExtra(FROM_DATE);
        toDate = intent.getStringExtra(TO_DATE);
        customerId = intent.getStringExtra(CUSTOMER_ID);
        clientId = intent.getStringExtra(CLIENT_ID);
        excludeAee = intent.getIntExtra(EXCLUDE_AEE, 0);
        customer = intent.getStringExtra(CUSTOMER);
        service = intent.getStringExtra(SERVICE);
    }

    @Override
    protected void initData() {

        toolbarTitle.setText(customer + service + "报表");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        updateValues();
        recyclerView.setHasFixedSize(false);
        mAdapter = new RdoCashRatioAdapter(data);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
                (mAdapter);
        recyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
//        recyclerView.setEmptyView(R.layout.empty_view_no_message, UltimateRecyclerView
//                .EMPTY_CLEAR_ALL);
//        recyclerView.hideEmptyView();
        recyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout
                .parallax_recyclerview_header, recyclerView.mRecyclerView, false));
//        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback
                (mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView.mRecyclerView);
//        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mAdapter);
//        ultimateRecyclerView.showFloatingButtonView();//
//        recyclerView.setItemAnimator(AnimatorType.SlideInRight.getAnimator());
//        recyclerView.getItemAnimator().setAddDuration(500);
//        recyclerView.getItemAnimator().setRemoveDuration(500);
        fetch();
    }

    private void fetch() {

        json = new JSONObject();
        try {
            json.put("serviceType", serviceType);
            json.put("fromDate", fromDate);
            json.put("toDate", toDate);
            json.put("customerId", customerId);
            json.put("clientId", clientId);
            json.put("excludeAEE", excludeAee);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url;
        if ("njxbdjf".equals(customerId)) {
            url = "http://115.233.216.130:9062/jf/platform/rdoCash/getSuccessRate";
        } else {
            url = "http://115.233.216.130:9059/jf/platform/rdoCash/getSuccessRate";
        }
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(url)
                        .setContent(json.toString())
                        .setPage(page)
                        .setActionType(type)
                        .createTask());
    }

    private String getCurrentTime() {
        Calendar date = Calendar.getInstance();//系统当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String formatTime = format.format(date.getTime());
        return formatTime.replace("-", "")
                .replace(":", "")
                .replace(" ", "");
    }

    private String formatTime(String dateTime) {
        StringBuilder sb = new StringBuilder(dateTime);
        return sb.insert(12, ":").insert(10, ":").insert(8, " ").insert(6, "-").insert(4, "-")
                .toString();
    }

    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                .sweepSpeed(1f)
                .rotationSpeed(1f)
                .strokeWidth(dpToPx(3))
                .style(CircularProgressDrawable.Style.ROUNDED);
        b.sweepInterpolator(new LinearInterpolator());
        progressBar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                progressBar.getWidth(),
                progressBar.getHeight());
//        progressBar.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressBar.getIndeterminateDrawable()).start();

    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        recyclerView.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
//            @Override
//            public void onParallaxScroll(float percentage, float offset, View parallax) {
//            }
//        });
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
                toDate = getCurrentTime();
                ++page;
                fetch();
            }
        });
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                recyclerView.disableLoadmore();
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        SuccessRatioBean bean = GsonUtils.getInstance().transitionToBean(result, SuccessRatioBean
                .class);
        ((CircularProgressDrawable) progressBar.getIndeterminateDrawable()).stop();
        tvLoadingHint.setVisibility(View.GONE);
        if (bean == null)
            return;
        if (bean.getResultCode() != 200) {
            ToastUtils.showLongToast(getApplicationContext(), bean.getResultMessage());
            return;
        }
        size = bean.getSize();
        if (size == 0) {
            ToastUtils.showLongToast(getApplicationContext(), "暂无数据！");
        } else {
            mAdapter.setStartTime(formatTime(fromDate));
            mAdapter.setEndTime(formatTime(toDate));
            mAdapter.setTotalCount(bean.getData().getTotalCount());
            data = bean.getData().getClassification();
            int count = 0;
            for (ClassificationBean element : data) {
                if (element.getRemark().contains("balance")) {
                    count += Integer.parseInt(element.getCount());
                    continue;
                }
                if (element.getRemark().contains("成功")) {
                    beans.add(0, element);
                } else {
                    beans.add(element);
                }
            }
            ClassificationBean header = new ClassificationBean();
            header.setRemark(getString(R.string.description));
            header.setCount(getString(R.string.count));
            beans.add(0, header);
            if (count > 0) {
                ClassificationBean payErr = new ClassificationBean();
                payErr.setRemark("balance_not_pay_err");
                payErr.setCount(count + "");
                beans.add(2, payErr);
            }
            ClassificationBean footer = new ClassificationBean();
            footer.setRemark(getString(R.string.total));
            footer.setCount(bean.getData().getTotalCount() + "");
            beans.add(beans.size(), footer);
            if (page == 1) {
                mAdapter.insert(beans);
            } else {
                mAdapter.clear();
                mAdapter.insert(beans);
                recyclerView.setRefreshing(false);
                layoutManager.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ((CircularProgressDrawable) progressBar.getIndeterminateDrawable()).stop();
        tvLoadingHint.setVisibility(View.GONE);
        ToastUtils.showLongToast(getApplicationContext(), "数据获取失败，请稍后重试！");
//        recyclerView.showEmptyView();
    }
}
