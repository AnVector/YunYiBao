package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.SettingsAdapter;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.bean.UserLevelBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class SettingsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_logout)
    AppCompatButton btnLogout;
    @BindView(R.id.activity_settings)
    LinearLayout activitySettings;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    private SettingsAdapter mAdapter;
    String[] array = new String[]{"修改密码", "常见问题", "意见反馈", "账号管理", "关于云逸宝"};
    private List<String> mData = Arrays.asList(array);
    private boolean isLogin;
    public static final int REQUEST_LOGIN_CODE = 0x00005;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        isLogin = intent.getBooleanExtra("isLogin", false);
    }

    @Override
    protected void initData() {
        if (!isLogin) {
            btnLogout.setText(getString(R.string.not_login));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fakeStatusBar.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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
                            if (!isLogin) {
                                startActivityForLogin();
                            } else {
                                intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case "修改密码":
                            if (!isLogin) {
                                startActivityForLogin();
                            } else {
                                intent = new Intent(SettingsActivity.this, GetVerifyCodeActivity
                                        .class);
                                intent.putExtra("title", "修改密码");
                                intent.putExtra("action", "MODIFYPWD");
                                intent.putExtra("phoneNum", "");
                                startActivity(intent);
                            }
                            break;
                        case "账号管理":
                            if (!isLogin) {
                                startActivityForLogin();
                            } else {
                                intent = new Intent(SettingsActivity.this, AccountManageActivity
                                        .class);
                                startActivity(intent);
                            }
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

                if (!isLogin) {
                    ToastUtils.showToast(getApplicationContext(), "您还未登录，请先登录", R.layout.toast, R
                            .id.tv_message);
                    return;
                }
                showDialog();
            }
        });

    }

    @Override
    protected void setStatusBarTheme() {
        super.setStatusBarTheme();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setTranslucentForImageView(SettingsActivity.this, 0, activitySettings);
        }
    }

    private void startActivityForLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_CODE);
    }

    private void getUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MINE");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType", ""));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(1)
                        .createTask());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_CODE) {
            getUserInfo();
        }
    }

    private void logOut() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "OUT");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void showDialog() {
        Holder holder = new ViewHolder(LayoutInflater.from(this).inflate(R.layout.dialog_confirm,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.tv_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(getString(R.string.logout_prompt));
        btnLeft.setText(getString(R.string.cancel));
        btnRight.setText(getString(R.string.ok));
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
                        logOut();
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setInAnimation(R.anim.fade_in_center)
                .setOutAnimation(R.anim.fade_out_center)
                .setOnClickListener(clickListener)
                .setContentWidth(DensityUtils.dp2px(this, 298f))
                .setContentHeight(DensityUtils.dp2px(this, 195f))
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                JPushInterface.deleteAlias(this, -1);
                PreferencesUtils.putString(getApplicationContext(), "uid", "");
                PreferencesUtils.putString(getApplicationContext(), "userType", "");
                PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", false);
                finish();
            }
        }

        if (actionType == 1) {
            UserLevelBean bean = GsonUtils.getInstance().transitionToBean(result, UserLevelBean
                    .class);
            if (bean == null)
                return;

            if (bean.getCode() == 200) {
                isLogin = true;
                btnLogout.setText("退出登录");
            }
        }
    }
}
