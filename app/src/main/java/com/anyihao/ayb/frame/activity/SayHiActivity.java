package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SayHiActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_content)
    PowerfulEditText edtContent;
    private String mRecieveUid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_say_hi;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mRecieveUid = intent.getStringExtra("uid");
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.say_hi));
        toolbarTitleRight.setText(getString(R.string.send_hi));
        toolbarTitleRight.setTextColor(getResources().getColor(R.color.light_gray));

    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbarTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入打招呼的内容");
                    return;
                }
                sendMessage(content);
            }
        });

    }

    private void sendMessage(String content) {
        if (TextUtils.isEmpty(mRecieveUid))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "SENDMSG");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("receiveUid", mRecieveUid);
        params.put("content", content);

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
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            if (bean.getCode() == 200) {
                finish();
            }
        }

    }
}
