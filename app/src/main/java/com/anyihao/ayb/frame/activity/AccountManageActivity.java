package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.AccountManageAdapter;
import com.anyihao.ayb.bean.AccountListInfoBean;
import com.anyihao.ayb.bean.AccountListInfoBean.DataBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class AccountManageActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private AccountManageAdapter mAdapter;
    private List<DataBean> mData = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_manage;
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
        toolbarTitleMid.setText(getString(R.string.account_management));
        mAdapter = new AccountManageAdapter(this, R.layout.item_about_us);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        getAccountList();
    }

    private void getAccountList() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "ACCOUNTLIST");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        postForm(params, 1, 0);
    }

    private void unbindAccount(String accountType) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "ACCOUNTBIND");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("appid", "");
        params.put("type", accountType);
        postForm(params, 1, 1);
    }

    private void bindAccount(String accountType, String appId) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "ACCOUNTBIND");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("appid", appId);
        params.put("type", accountType);
        postForm(params, 1, 2);
    }

    private void postForm(Map<String, String> params, int page, int actionType) {
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(page)
                        .setActionType(actionType)
                        .createTask());
    }

    private void showDialog(final String accountType) {
        Holder holder = new ViewHolder(LayoutInflater.from(this).inflate(R.layout.confirm_dialog,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.dia_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(getString(R.string.account_unbunding_hint));
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
                        unbindAccount(accountType);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "dismiss");
            }
        };

        OnCancelListener cancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "cancel");
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentWidth(DensityUtils.dp2px(this, 298f))
                .setContentHeight(DensityUtils.dp2px(this, 195f))
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
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
                    if (((DataBean) o).getStatus() == 1) {
                        showDialog(((DataBean) o).getType());
                    } else {
                        SHARE_MEDIA media = SHARE_MEDIA.QQ;
                        switch (((DataBean) o).getType()) {
                            case "QQ":
                                media = SHARE_MEDIA.QQ;
                                break;
                            case "WX":
                                media = SHARE_MEDIA.WEIXIN;
                                break;
                            case "WB":
                                media = SHARE_MEDIA.SINA;
                                break;
                            default:
                                break;
                        }
                        UMShareAPI.get(AccountManageActivity.this).getPlatformInfo
                                (AccountManageActivity.this,
                                        media, authListener);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            AccountListInfoBean beans = GsonUtils.getInstance().transitionToBean(result,
                    AccountListInfoBean.class);
            if (beans == null)
                return;
            if (beans.getCode() == 200) {
                mAdapter.remove(0, mData.size());
                mData.clear();
                mData.addAll(beans.getData());
                mAdapter.add(0, mData.size(), mData);
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(this, "账号解绑成功");
                getAccountList();
            } else {
                ToastUtils.showToast(this, bean.getMsg());
            }
        }

        if (actionType == 2) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(this, "账号绑定成功");
                getAccountList();
            } else {
                ToastUtils.showToast(this, bean.getMsg());
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

        if (error.contains("ConnectException")) {
            ToastUtils.showToast(this, "网络连接失败，请检查网络设置");
        }

    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            SocializeUtils.safeShowDialog(dialog);
//            UmengTool.getSignature(LoginActivity.this);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                LogUtils.e(TAG, "Key = " + entry.getKey() + ", Value = " + entry
                        .getValue());
            }
            String type = "";
            String appId = "";
            appId = data.get("uid");
            switch (platform) {
                case QQ:
                    type = "QQ";
                case WEIXIN:
                    type = "WX";
                    break;
                case SINA:
                    type = "WB";
                    break;
                default:
                    break;
            }
            bindAccount(type, appId);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            SocializeUtils.safeCloseDialog(dialog);
            LogUtils.e(TAG, "platform=" + platform);
            LogUtils.e(TAG, "action = " + action);
            LogUtils.e(TAG, "error = " + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            SocializeUtils.safeCloseDialog(dialog);
            LogUtils.e(TAG, "platform=" + platform);
            LogUtils.e(TAG, "action = " + action);
        }
    };
}
