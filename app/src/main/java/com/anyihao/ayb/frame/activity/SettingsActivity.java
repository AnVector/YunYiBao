package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.androidbase.manager.ActivityManager;
import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.SettingsAdapter;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SettingsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_logout)
    AppCompatButton btnLogout;
    private SettingsAdapter mAdapter;
    String[] array = new String[]{"修改密码", "常见问题", "意见反馈", "押金说明", "账号管理", "关于云逸宝"};
    private List<String> mData = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.settings));
        mAdapter = new SettingsAdapter(this, R.layout.item_settings);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        recyclerview.setHasFixedSize(true);
        mAdapter.add(0, mData.size(), mData);
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
                if (o instanceof String) {
                    Intent intent;
                    String title = o.toString();
                    switch (title) {
                        case "常见问题":
                            intent = new Intent(SettingsActivity.this, QuestionsActivity.class);
                            startActivity(intent);
                            break;
                        case "意见反馈":
                            intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
                            startActivity(intent);
                            break;
                        case "修改设备信息":
                            intent = new Intent(SettingsActivity.this, ModifyDeviceInfoActivity
                                    .class);
                            startActivity(intent);
                            break;
                        case "修改密码":
                            intent = new Intent(SettingsActivity.this, RetrievePwdActivity
                                    .class);
                            intent.putExtra(RetrievePwdActivity.REQUEST_TYPE, RetrievePwdActivity
                                    .VERIFY_ORIGINAL_PHONE);
                            startActivity(intent);
                            break;
                        case "修改手机":
                            intent = new Intent(SettingsActivity.this, RetrievePwdActivity.class);
                            intent.putExtra(RetrievePwdActivity.REQUEST_TYPE, RetrievePwdActivity
                                    .VERIFY_ORIGINAL_PHONE);
                            startActivity(intent);
                            break;
                        case "账号管理":
                            intent = new Intent(SettingsActivity.this, AccountManageActivity.class);
                            startActivity(intent);
                            break;
                        case "关于云逸宝":
                            intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

    }

    private void logOut() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "OUT");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setContent("{}")
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
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                        .tv_message);
                PreferencesUtils.putString(getApplicationContext(), "uid", "");
                PreferencesUtils.putString(getApplicationContext(), "userType", "");
                ActivityManager.getInstance().finishActivity(MainFragmentActivity.class);
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
