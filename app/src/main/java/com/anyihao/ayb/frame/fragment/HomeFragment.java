package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MainAdapter;
import com.anyihao.ayb.bean.CertificationStatusBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.CertificationActivity;
import com.anyihao.ayb.frame.activity.ConnectedDevicesActivity;
import com.anyihao.ayb.frame.activity.HelpActivity;
import com.anyihao.ayb.frame.activity.LoginActivity;
import com.anyihao.ayb.frame.activity.MessageActivity;
import com.anyihao.ayb.frame.activity.RechargeActivity;
import com.anyihao.ayb.frame.activity.RentedDevicesActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class HomeFragment extends ABaseFragment {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.toolbar_help)
    TextView toolbarHelp;
    @BindView(R.id.tv_leasing)
    TextView tvLeasing;
    @BindView(R.id.tv_my_devices)
    TextView tvMyDevices;
    @BindView(R.id.tv_shop)
    TextView tvShop;
    @BindView(R.id.tv_ssid)
    TextView tvSsid;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.network_ll)
    LinearLayout networkLl;
    @BindView(R.id.flipper)
    ViewFlipper flipper;
    @BindView(R.id.iv_bell)
    ImageView ivBell;
    private static int REQUEST_LOGIN_CODE = 0x00003;
    private MainAdapter mAdapter;
    String[] advertisement = new String[]{"流量大减价，一律两元！一律两元！", "流量大减价，一律两元！一律两元！"};
    String[] array = new String[]{"AYB-10086", "AYB-10010"};
    private List<String> mData = Arrays.asList(array);
    private AnimationDrawable animationDrawable;
    private boolean isLogin;
    private String userStatus;

    @Override
    protected void initData() {
        animationDrawable = (AnimationDrawable) ivBell.getDrawable();
        animationDrawable.start();
        toolbar.setBackground(null);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        toolbarTitle.setText(getString(R.string.app_name));
        toolbarHelp.setText(getString(R.string.help_center));
        toolbar.inflateMenu(R.menu.main_menu);
        mAdapter = new MainAdapter(getContext(), R.layout.item_main);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        recyclerview.setHasFixedSize(true);
        mAdapter.add(0, mData.size(), mData);

        for (int i = 0; i < advertisement.length; i++) {
            View ll_content = View.inflate(getActivity(), R.layout.item_flipper, null);
            TextView tv_content = (TextView) ll_content.findViewById(R.id.tv_content);
            tv_content.setText(advertisement[i]);
            flipper.addView(ll_content);
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            isLogin = bundle.getBoolean("isLogin", false);
        }

        if (isLogin) {
            getUserCertStatus();
        }
    }


    @Override
    protected void initEvent() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!isLogin) {
                    startActivityForLogin();
                    return true;
                }
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
                return true;
            }
        });

        toolbarHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HelpActivity.class);
                startActivity(intent);
            }
        });

        networkLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConnectedDevicesActivity.class);
                startActivity(intent);
            }
        });

        tvLeasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
//                IntentIntegrator intentIntegrator = IntentIntegrator
//                        .forSupportFragment(HomeFragment.this).setCaptureActivity
//                                (ScanActivity.class);
//                intentIntegrator
//                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//                        .setOrientationLocked(false)//扫描方向固定
//                        .setCaptureActivity(ScanActivity.class) //
//                        // 设置自定义的activity是CustomActivity
//                        .initiateScan(); // 初始化扫描
                Intent intent = new Intent(mContext, CertificationActivity.class);
                startActivity(intent);
            }
        });

        tvMyDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RentedDevicesActivity.class);
                startActivity(intent);
            }
        });

        tvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                Intent intent = new Intent(mContext, RechargeActivity.class);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                showDialog();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void startActivityForLogin() {
        if (getActivity() == null)
            return;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_CODE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    private void getUserCertStatus() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "QUERYSTATUS");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
    }

    private void showDialog() {
        Holder holder = new ViewHolder(R.layout.confirm_dialog);
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
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

        final DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            CertificationStatusBean bean = GsonUtils.getInstance().transitionToBean(result,
                    CertificationStatusBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                userStatus = bean.getInfoStatus();
            }

        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        Logger.d(TAG, error);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN_CODE && resultCode == LoginActivity
                .RESULT_LOGIN_CODE) {
            isLogin = true;
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                data);
        if (intentResult != null && intentResult.getContents() != null) {
            String result = intentResult.getContents();
            ToastUtils.showLongToast(getContext(), result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animationDrawable == null) {
            return;
        }
        animationDrawable.stop();
    }
}
