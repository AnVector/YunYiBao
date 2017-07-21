package com.anyihao.ayb.frame.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.anyihao.ayb.adapter.ConnectedDeviceAdapter;
import com.anyihao.ayb.bean.ConnectedUserBean;
import com.anyihao.ayb.bean.ConnectedUserBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ConnectedDevicesActivity extends ABaseActivity {


    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private ConnectedDeviceAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private List<DataBean> mData = new ArrayList<>();
    private Dialog bottomDialog;
    private TextView tvSayHi;
    private TextView tvBriberyMoney;
    private TextView tvCancel;
    private String phoneNum;
    private String aliasName;
    private int count = 0;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_connected_devices;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        aliasName = intent.getStringExtra("aliasName");
    }

    @Override
    protected void initData() {
        initBottomDialog();
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setBackground(null);
        toolbarTitle.setText(getString(R.string.connected_devices));
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        tvCount.setText(String.format(getString(R.string.connected_devices_hint),
                count));
        initUltimateRV();
        getConnectedUser();
    }

    private void initUltimateRV() {
        recyclerView.setHasFixedSize(false);
        mAdapter = new ConnectedDeviceAdapter(mData, R.layout.item_connected_device_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTranslucentForImageView(this, 0, llContainer);
    }

    private void getConnectedUser() {
        if (TextUtils.isEmpty(aliasName))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("cmd", "USERLIST");
        params.put("aliasName", aliasName);
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .createTask());
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
                phoneNum = o.toString();
                if (bottomDialog != null) {
                    bottomDialog.show();
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        tvSayHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectedDevicesActivity.this, SayHiActivity.class);
                intent.putExtra("uid", phoneNum);
                startActivity(intent);
            }
        });

        tvBriberyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectedDevicesActivity.this, BriberyMoneyActivity
                        .class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomDialog != null) {
                    bottomDialog.dismiss();
                }
            }
        });

    }

    private void initBottomDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View dialogContentView = LayoutInflater.from(this).inflate(R.layout
                .dialog_content_circle, null);
        tvSayHi = (TextView) dialogContentView.findViewById(R.id.tv_say_hi);
        tvBriberyMoney = (TextView) dialogContentView.findViewById(R.id.tv_bribery_money);
        tvCancel = (TextView) dialogContentView.findViewById(R.id.tv_cancle);
        bottomDialog.setContentView(dialogContentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dialogContentView
                .getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtils.dp2px(this,
                16f);
        params.bottomMargin = DensityUtils.dp2px(this, 8f);
        dialogContentView.setLayoutParams(params);
        if (bottomDialog.getWindow() != null) {
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            ConnectedUserBean bean = GsonUtils.getInstance().transitionToBean(result,
                    ConnectedUserBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                count = beans.size();
                if (count > 0) {
                    tvCount.setText(String.format(getString(R.string.connected_devices_hint),
                            count));
                    mAdapter.removeAllInternal(mData);
                    mAdapter.insert(beans);
                }
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bottomDialog == null)
            return;
        if (bottomDialog.isShowing()) {
            bottomDialog.dismiss();
            bottomDialog = null;
        }
    }
}
