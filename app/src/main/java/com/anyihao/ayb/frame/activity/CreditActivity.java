package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.CreditAdapter;
import com.anyihao.ayb.bean.CreditBean;
import com.anyihao.ayb.bean.CreditBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.NumberRunningTextView;

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
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_points)
    NumberRunningTextView tvPoints;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    private CreditAdapter mAdapter;
    private List<DataBean> mData = new ArrayList<>();
    private String mCredit;

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
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/W13.TTF");
        tvPoints.setTypeface(fontFace);
        tvPoints.setContent(mCredit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setBackground(null);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        toolbarTitle.setText(getString(R.string.my_points));
        mAdapter = new CreditAdapter(this, R.layout.item_credit);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        getCredits();
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTranslucentForImageView(this, 0, llHeader);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            CreditBean bean = GsonUtils.getInstance().transitionToBean(result, CreditBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                if (bean.getData().size() > 0) {
                    mAdapter.remove(0, mData.size());
                    mData.clear();
                    mData.addAll(bean.getData());
                    mAdapter.add(0, mData.size(), mData);
                } else {
                    ToastUtils.showToast(getApplicationContext(), "暂无积分赠送记录");
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
        }

    }
}
