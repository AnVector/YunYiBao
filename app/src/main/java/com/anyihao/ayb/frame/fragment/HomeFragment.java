package com.anyihao.ayb.frame.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.ClipboardUtils;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.DeviceUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MainAdapter;
import com.anyihao.ayb.bean.AdvertisementBean;
import com.anyihao.ayb.bean.AdvertisementBean.DataBean;
import com.anyihao.ayb.bean.CertificationStatusBean;
import com.anyihao.ayb.bean.IEBoxBean;
import com.anyihao.ayb.bean.NewMessageBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.bean.SsidPwdBean;
import com.anyihao.ayb.bean.UserLevelBean;
import com.anyihao.ayb.bean.WifiInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.common.WifiInfoManager;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.CertificationActivity;
import com.anyihao.ayb.frame.activity.ConnectedDevicesActivity;
import com.anyihao.ayb.frame.activity.DepositActivity;
import com.anyihao.ayb.frame.activity.DeviceCodeActivity;
import com.anyihao.ayb.frame.activity.HelpActivity;
import com.anyihao.ayb.frame.activity.LoginActivity;
import com.anyihao.ayb.frame.activity.MessageActivity;
import com.anyihao.ayb.frame.activity.RechargeActivity;
import com.anyihao.ayb.frame.activity.RentedDevicesActivity;
import com.anyihao.ayb.frame.activity.ScanActivity;
import com.anyihao.ayb.frame.activity.WebActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.anyihao.ayb.ui.WSCircleRotate;
import com.anyihao.ayb.ui.waitingdots.DilatingDotsProgressBar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeFragment extends ABaseFragment implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    private static final int REQUEST_NEW_MSG_CODE = 0x0002;
    private static final int REQUEST_LOGIN_CODE = 0x0003;
    private static final int RC_LOCATION_CONTACTS_PERM = 0x0004;
    @BindView(R.id.progress)
    DilatingDotsProgressBar progress;
    @BindView(R.id.tv_data_amount)
    TextView tvDataAmount;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView mRecyclerView;
    @BindView(R.id.imv_wifi)
    ImageView imvWifi;
    @BindView(R.id.load_rotate)
    WSCircleRotate loadRotate;
    private MainAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private String mPassword;
    private int mProgress;
    private List<WifiInfoBean> mData = new ArrayList<>();
    private UNetWorkReceiver mNetworkReceiver;
    private boolean isLogin;
    private String aliasName;
    private boolean isConnected;
    private List<WifiInfoBean> mWifiList;
    private boolean loading;
    private CountDownTimer mCountDownTimer;

    private class UNetWorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 这个监听wifi的打开与关闭，与wifi的连接无关
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        wifiClosed();
                        break;
                    default:
                        break;
                }
            }
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.isConnected()) {
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            getConnectWifi();// connected to wifi
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void initData() {
        toolbar.setBackground(null);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        toolbarTitle.setText(getString(R.string.app_name));
        toolbarHelp.setText(getString(R.string.help_center));
        toolbar.inflateMenu(R.menu.home_menu);
        tvDataAmount.setText(String.format(mContext.getString(R.string.surplus_amount), "--"));
        isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(), "isLogin", false);
        if (isLogin) {
            getUserCertStatus();
        }
        getAdvertisement();
        initUltimateRV();
        permissionsRequest();
        initNetWorkStateReceiver();
        initTimer();
        progress.setDotColor(Color.parseColor("#d6d7dc"));
        progress.show();
        getUserInfo();
        getUnreadMsg();
        if (loading) {
            startAnimation();
        }
    }

    private void initTimer() {
        mCountDownTimer = new CountDownTimer(120 * 1000, 10 * 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                getSsidPwd();
            }

            @Override
            public void onFinish() {
                loading = false;
                stopAnimation();
                ToastUtils.showToast(mContext.getApplicationContext(), "设备连网超时，请稍后重试");
            }
        };
    }

    private void startAnimation() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
        progress.setDotColors(Color.parseColor("#5dc1ff"), Color.parseColor("#b1e1fe"));
        if (progress != null) {
            progress.show();
            progress.setVisibility(View.VISIBLE);
        }
        imvWifi.setImageDrawable(mContext.getResources().getDrawable(R.drawable
                .ic_connecting));
        tvStatus.setText("设备连网中");
        tvSsid.setText("");
        tvStatus.setTextColor(Color.parseColor("#2DA8F4"));
        loadRotate.setVisibility(View.VISIBLE);
        loadRotate.startAnimator();
    }

    private void setConnectSuccess(String ssid) {
        isConnected = true;
        aliasName = ssid.replace("\"", "");
        if (progress != null) {
            progress.hide();
            progress.setVisibility(View.GONE);
        }
        if (loadRotate != null) {
            loadRotate.setVisibility(View.GONE);
        }
        if (imvWifi != null) {
            imvWifi.setImageDrawable(mContext.getResources().getDrawable(R.drawable
                    .ic_connected));
        }
        if (tvSsid != null) {
            tvSsid.setText(mContext.getString(R.string.IEBox));
            tvSsid.setVisibility(View.VISIBLE);
        }
        if (tvStatus != null) {
            tvStatus.setText("已连接");
            tvStatus.setTextColor(Color.parseColor("#2DA8F4"));
        }

    }

    private void stopAnimation() {
        isConnected = false;
        if (progress != null) {
            progress.setDotColor(Color.parseColor("#d6d7dc"));
            progress.show();
        }
        if (loadRotate != null) {
            loadRotate.stopAnimator();
            loadRotate.setVisibility(View.GONE);
        }

        if (imvWifi != null) {
            imvWifi.setImageDrawable(mContext.getResources().getDrawable(R.drawable
                    .ic_disconnected));
        }

        if (tvSsid != null) {
            tvSsid.setVisibility(View.INVISIBLE);
        }

        if (tvStatus != null) {
            tvStatus.setText("未连接");
            tvStatus.setTextColor(Color.parseColor("#999999"));
        }

        if (mCountDownTimer != null && loading) {
            mCountDownTimer.cancel();
        }
    }

    private List<WifiInfoBean> getIWifiList() {

        WifiInfoManager.getInstance(mContext).openWifi();
        WifiInfoManager.getInstance(mContext).startScan();
        List<ScanResult> results = WifiInfoManager.getInstance(mContext).getWifiList();
        String ssid = WifiInfoManager.getInstance(mContext).getSSid();
        if (results == null || results.isEmpty())
            return null;
//        Logger.d(results);
        List<WifiInfoBean> list = new ArrayList<>();
        WifiInfoBean bean;
        for (ScanResult ele : results) {
            if (ele == null || TextUtils.isEmpty(ele.SSID))
                continue;
            if (ele.SSID.contains("IeBox")) {
                bean = new WifiInfoBean();
                bean.setSsid(ele.SSID);
                Logger.d(ele.SSID);
                if (!TextUtils.isEmpty(ssid) && ssid.replace("\"", "").equals(ele.SSID)) {
                    bean.setConnected(true);
                } else {
                    bean.setConnected(false);
                }
                list.add(bean);
            }
        }
        uploadWifiList(list);
        return list;
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    protected void permissionsRequest() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                .ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(mContext, permissions)) {
            mWifiList = getIWifiList();
            mAdapter.removeAllInternal(mData);
            if (mWifiList != null && !mWifiList.isEmpty()) {
                mAdapter.insert(mWifiList);
            } else {
                mRecyclerView.showEmptyView();
            }
            getConnectWifi();
        } else {
            EasyPermissions.requestPermissions(this, "开启WIFI",
                    RC_LOCATION_CONTACTS_PERM, permissions);
        }
    }

    private void getConnectWifi() {
        String ssid = WifiInfoManager.getInstance(mContext).getSSid();
        if (!TextUtils.isEmpty(ssid)) {
            if (ssid.contains("IeBox")) {
                setConnectSuccess(ssid);
            } else {
                stopAnimation();
            }
            updateWifiList();
        }
    }

    private void updateWifiList() {
        if (mRecyclerView == null)
            return;
        mWifiList = getIWifiList();
        mAdapter.removeAllInternal(mData);
        if (mWifiList != null && !mWifiList.isEmpty()) {
//            Logger.d(mWifiList.size());
            mAdapter.insert(mWifiList);
            mRecyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
            mRecyclerView.hideEmptyView();
        } else {
            mRecyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
            mRecyclerView.showEmptyView();
        }
    }

    private void wifiClosed() {
        stopAnimation();
        mData.clear();
        mAdapter.notifyDataSetChanged();
        if (mRecyclerView != null) {
            mRecyclerView.showEmptyView();
        }
    }

    private void initNetWorkStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mNetworkReceiver = new UNetWorkReceiver();
        mContext.registerReceiver(mNetworkReceiver, filter);
    }

    private void initUltimateRV() {
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new MainAdapter(mData, R.layout.item_main);
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setEmptyView(R.layout.view_empty_main, UltimateRecyclerView
                .EMPTY_CLEAR_ALL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return true;
                }
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivityForResult(intent, REQUEST_NEW_MSG_CODE);
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
                if (!isConnected)
                    return;
                Intent intent = new Intent(mContext, ConnectedDevicesActivity.class);
                intent.putExtra("aliasName", aliasName);
                startActivity(intent);
            }
        });

        tvLeasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                Intent intent = new Intent();
                switch (mProgress) {
                    case 2:
                        intent.setClass(mContext, CertificationActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(mContext, DepositActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        IntentIntegrator intentIntegrator = IntentIntegrator
                                .forSupportFragment(HomeFragment.this).setCaptureActivity
                                        (ScanActivity.class);
                        intentIntegrator
                                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                                .setOrientationLocked(false)//扫描方向固定
                                .setCaptureActivity(ScanActivity.class) //
                                // 设置自定义的activity是CustomActivity
                                .initiateScan(); // 初始化扫描
                        break;
                    default:
                        break;
                }
            }
        });

        tvMyDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                Intent intent = new Intent(mContext, RentedDevicesActivity.class);
                startActivity(intent);
            }
        });

        tvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
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
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                if (o instanceof WifiInfoBean) {
                    if (loading) {
                        ToastUtils.showToast(mContext.getApplicationContext(), "设备连网中，请稍后重试");
                    } else {
                        aliasName = ((WifiInfoBean) o).getSsid();
                        getSsidPwd();
                    }

                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        mRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
                updateWifiList();
            }
        });
    }

    private void showAdvertisement(List<DataBean> advertisement) {
        for (int i = 0; i < advertisement.size(); i++) {
            View ll_content = View.inflate(mContext, R.layout.item_flipper, null);
            ll_content.setTag(advertisement.get(i));
            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object object = v.getTag();
                    if (object instanceof DataBean) {
                        String type = ((DataBean) object).getType();
                        if (!TextUtils.isEmpty(type)) {
                            switch (type) {
                                case "URL":
                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("url", ((DataBean) object).getLinkUrl());
                                    intent.putExtra("title", ((DataBean) object).getTitle());
                                    startActivity(intent);
                                    break;
                                case "PAY":
                                    break;
                                case "SHR":
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            });
            TextView tv_content = (TextView) ll_content.findViewById(R.id.tv_content);
            tv_content.setText(advertisement.get(i).getDesc());
            if (flipper != null) {
                flipper.addView(ll_content);
            }
        }
    }

    private void getSsidPwd() {
        if (TextUtils.isEmpty(aliasName))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "WIFIPWD");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("aliasName", aliasName);
        postForm(params, 1, 2);
    }

    private void getAdvertisement() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AD");
        postForm(params, 1, 3);
    }

    private void getUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MINE");
        params.put("uid", PreferencesUtils.getString(mContext, "uid", ""));
        params.put("userType", PreferencesUtils.getString(mContext, "userType", ""));
        postForm(params, 1, 5);
    }

    private void getUnreadMsg() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "NEWMSG");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        postForm(params, 1, 6);
    }

    private void uploadWifiList(List<WifiInfoBean> list) {
        if (list == null || list.isEmpty())
            return;
        String latitude = PreferencesUtils.getString(mContext.getApplicationContext(),
                "latitude", "");
        String longitude = PreferencesUtils.getString(mContext.getApplicationContext(),
                "longitude", "");
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude))
            return;
        StringBuilder sb = new StringBuilder();
        for (WifiInfoBean bean : list) {
            sb.append(bean.getSsid()).append(",");
        }
        String aliasName = sb.deleteCharAt(sb.length() - 1).toString();
        if (TextUtils.isEmpty(aliasName))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "WIFILISTA");
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("aliasName", aliasName);
        postForm(params, 1, 7);
    }

    private void postForm(Map<String, String> params, int page, int actionType) {
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(page)
                .setActionType(actionType)
                .createTask());
    }

    private void onGetPwdSuccess() {
        showDialog(R.layout.dialog_wifi_password, true);
    }

    private void onGetPwdFailure() {
        showDialog(R.layout.dialog_confirm, false);
    }

    private void startActivityForLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
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
        postForm(params, 1, 0);
    }

    private void addAuthorizedDevice() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZEADD");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("mac", DeviceUtils.getMacAddress(mContext));
        params.put("remarks", "PHONE");
        params.put("addStatus", "1");
        postForm(params, 1, 1);
    }

    private void getIEBoxNum(String merchantId) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "LEND");
        params.put("lendId", PreferencesUtils.getString(mContext.getApplicationContext(), "uid",
                ""));
        params.put("merchantId", merchantId);
        postForm(params, 1, 4);
    }

    private void showDialog(int layoutId, final boolean bool) {
        Holder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(layoutId, null));
        if (bool) {
            TextView tvCopy = (TextView) holder.getInflatedView().findViewById(R.id.btn_copy);
            TextView tvPassword = (TextView) holder.getInflatedView().findViewById(R.id
                    .tv_password);
            tvCopy.setText(getString(R.string.copy_password));
            tvPassword.setText("密码获取成功，选择相应的WIFI热点名称，点击进入" + aliasName + "，长按密码框，粘贴密码即可上网");
        } else {
            TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.dia_title);
            Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
            Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
            tvTitle.setText(getString(R.string.no_authenticate_device_hint));
            btnLeft.setText(getString(R.string.refuse_to_auth));
            btnRight.setText(getString(R.string.agree_to_auth));
        }


        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
                        addAuthorizedDevice();
                        dialog.dismiss();
                        break;
                    case R.id.btn_copy:
                        ClipboardUtils.copyText(mContext, mPassword);
                        ToastUtils.showToast(mContext.getApplicationContext(), "密码复制成功");
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentHeight(DensityUtils.dp2px(mContext, 195))
                .setContentWidth(DensityUtils.dp2px(mContext, 298))
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
                mProgress = bean.getInfoStatus();
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(mContext.getApplicationContext(), "授权成功");
            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 2) {
            SsidPwdBean bean = GsonUtils.getInstance().transitionToBean(result, SsidPwdBean.class);
            if (bean == null)
                return;
            int code = bean.getCode();
            switch (code) {
                case 200://WIFI密码获取成功
                    mPassword = bean.getPassword();
                    onGetPwdSuccess();
                    if (loading) {
                        stopAnimation();
                        loading = false;
                    }
                    break;
                case 491://设备未授权
                    onGetPwdFailure();
                    break;
                case 500:
                    if (!loading) {
                        startAnimation();
                        loading = true;
                    }
                    break;
                case 468://设备正在尝试连接服务器
                    ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
                    break;
                default:
                    ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
                    break;
            }
        }

        if (actionType == 3) {
            AdvertisementBean bean = GsonUtils.getInstance().transitionToBean(result, AdvertisementBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans == null)
                    return;
                if (beans.size() > 0) {
                    showAdvertisement(beans);
                }
            }
        }

        if (actionType == 4) {
            IEBoxBean bean = GsonUtils.getInstance().transitionToBean(result, IEBoxBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                Intent intent = new Intent(mContext, DeviceCodeActivity.class);
                intent.putExtra("vid", bean.getVid());
                startActivity(intent);
            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 5) {
            UserLevelBean bean = GsonUtils.getInstance().transitionToBean(result, UserLevelBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                if (tvDataAmount != null) {
                    tvDataAmount.setText(String.format(getString(R.string.surplus_amount),
                            transferDataAmount(bean.getFlow())));
                }

            }
        }

        if (actionType == 6) {
            NewMessageBean bean = GsonUtils.getInstance().transitionToBean(result, NewMessageBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                if (toolbar != null) {
                    if (bean.getResult() > 0) {
                        toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_news_unread);
                    } else {
                        toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_news);
                    }
                }
            }
        }

        if (actionType == 7) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                Logger.d("附近热点信息上送成功");
            } else {
                Logger.d(bean.getMsg());
            }
        }
    }

    private String transferDataAmount(String amount) {
        if (TextUtils.isEmpty(amount))
            return "--";
        float flow = Float.parseFloat(amount);
        if (flow >= 1024f) {
            float f = flow / 1024f;
            float ft = new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .floatValue();
            return ft + "G";
        } else {
            return amount + "M";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN_CODE) {
            isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(), "isLogin",
                    false);
            if (isLogin) {
                getUserCertStatus();
            }
        }

        if (requestCode == REQUEST_NEW_MSG_CODE) {
            getUnreadMsg();
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                data);
        if (intentResult != null && intentResult.getContents() != null) {
            String result = intentResult.getContents();
            if (TextUtils.isEmpty(result)) {
                ToastUtils.showToast(mContext.getApplicationContext(), "扫码失败");
            } else {
                getIEBoxNum(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNetworkReceiver != null) {
            mContext.unregisterReceiver(mNetworkReceiver);
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}