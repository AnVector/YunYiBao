package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.QuestionListBean.DataBean;

import butterknife.BindView;

public class QuestionDetailsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    private DataBean mBean;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_details;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mBean = (DataBean) intent.getSerializableExtra("details");

    }

    @Override
    protected void initData() {
        if (mBean != null) {
            tvQuestion.setText(mBean.getQuestion());
            tvAnswer.setText(mBean.getAnswer());
        }
        toolbarTitleMid.setText(getString(R.string.question_details));
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

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
