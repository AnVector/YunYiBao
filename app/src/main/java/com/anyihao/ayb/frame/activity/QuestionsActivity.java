package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.QuestionsAdapter;
import com.anyihao.ayb.bean.QuestionListBean;
import com.anyihao.ayb.bean.QuestionListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class QuestionsActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private QuestionsAdapter mAdapter;

    private List<DataBean> mData = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_questions;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.common_questions));
        mAdapter = new QuestionsAdapter(this, R.layout.item_settings);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        getQuestions();
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof DataBean) {
                    Intent intent = new Intent(QuestionsActivity.this, QuestionDetailsActivity
                            .class);
                    intent.putExtra("details", (DataBean) o);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    private void getQuestions() {

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "HELP");
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
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            QuestionListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    QuestionListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                mData.addAll(bean.getData());
                mAdapter.add(0, mData.size(), mData);
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

    }
}
