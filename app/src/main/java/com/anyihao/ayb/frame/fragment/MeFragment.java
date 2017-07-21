package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MeAdapter;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.bean.UserLevelBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.CreditActivity;
import com.anyihao.ayb.frame.activity.DeviceManageActivity;
import com.anyihao.ayb.frame.activity.FlowAccountActivity;
import com.anyihao.ayb.frame.activity.FlowChartActivity;
import com.anyihao.ayb.frame.activity.InviteFriendsActivity;
import com.anyihao.ayb.frame.activity.LoginActivity;
import com.anyihao.ayb.frame.activity.MeActivity;
import com.anyihao.ayb.frame.activity.MerchantPrivilegeActivity;
import com.anyihao.ayb.frame.activity.RechargeActivity;
import com.anyihao.ayb.frame.activity.RechargeRecordActivity;
import com.anyihao.ayb.frame.activity.SettingsActivity;
import com.anyihao.ayb.frame.activity.SystemRecordActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;
import com.chaychan.viewlib.PowerfulEditText;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends ABaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.greeting)
    TextView tvGreeting;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.rl_profile)
    RelativeLayout rlProfile;
    @BindView(R.id.ic_profile)
    ImageView icProfile;
    Unbinder unbinder;
    private MeAdapter mAdapter;
    private static int REQUEST_SETTINGS_CODE = 0x00001;
    private static int REQUEST_LOGIN_CODE = 0x00003;
    private boolean isLogin = false;
    private List<String> mData = new ArrayList<>();
    private String mIntegral;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 9; i++) {
            mData.add(i, "");
        }
        fakeStatusBar.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbarTitle.setText(mContext.getString(R.string.me));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager
                .VERTICAL, false));
        mAdapter = new MeAdapter(mContext, R.layout.item_me, mData);
        mRecyclerview.setAdapter(mAdapter);
        getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin == PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                "isLogin", false))
            return;
        getUserInfo();
    }

    private void getUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MINE");
        params.put("uid", PreferencesUtils.getString(mContext, "uid", ""));
        params.put("userType", PreferencesUtils.getString(mContext, "userType", ""));
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void pointsRedeem(String reqCode) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "REQCODE");
        params.put("uid", PreferencesUtils.getString(mContext, "uid", ""));
        params.put("reqCode", reqCode);
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
    protected void initEvent() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                intent.putExtra("isLogin", isLogin);
                startActivityForResult(intent, REQUEST_SETTINGS_CODE);
                return true;
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (view.getTag() instanceof String) {
                    if (!isLogin) {
                        startActivityForLogin();
                        return;
                    }
                    Intent intent;
                    switch (view.getTag().toString()) {
                        case "我的流量":
                            intent = new Intent(mContext, FlowAccountActivity.class);
                            startActivity(intent);
                            break;
                        case "流量商城":
                            intent = new Intent(mContext, RechargeActivity.class);
                            startActivity(intent);
                            break;
                        case "流量报表":
                            intent = new Intent(mContext, FlowChartActivity.class);
                            startActivity(intent);
                            break;
                        case "充值记录":
                            intent = new Intent(mContext, RechargeRecordActivity.class);
                            startActivity(intent);
                            break;
                        case "邀请好友":
                            intent = new Intent(mContext, InviteFriendsActivity.class);
                            startActivity(intent);
                            break;
                        case "输入邀请码":
                            showDialog();
                            break;
                        case "系统赠送记录":
                            intent = new Intent(mContext, SystemRecordActivity.class);
                            startActivity(intent);
                            break;
                        case "我的积分":
                            intent = new Intent(mContext, CreditActivity.class);
                            intent.putExtra("integral", mIntegral);
                            startActivity(intent);
                            break;
                        case "商家特权":
                            intent = new Intent(mContext, MerchantPrivilegeActivity.class);
                            startActivity(intent);
                            break;
                        case "授权设备管理":
                            intent = new Intent(mContext, DeviceManageActivity.class);
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

        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    Intent intent = new Intent(mContext, MeActivity.class);
                    intent.putExtra("uid", PreferencesUtils.getString(mContext, "uid", ""));
                    intent.putExtra("userType", PreferencesUtils.getString(mContext, "userType",
                            ""));
                    startActivity(intent);
                } else {
                    startActivityForLogin();
                }

            }
        });
    }

    private void startActivityForLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_CODE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTINGS_CODE || requestCode == REQUEST_LOGIN_CODE) {
            if (isLogin == PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                    "isLogin", false))
                return;
            getUserInfo();
        }
    }

    private void showDialog() {
        Holder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout
                .me_dialog_content, null));
        final PowerfulEditText powerfulEditText = (PowerfulEditText) holder.getInflatedView()
                .findViewById(R.id.edt_exchange_code);
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
                        if (powerfulEditText != null) {
                            String code = powerfulEditText.getText().toString().trim();
                            if (StringUtils.isEmpty(code)) {
                                ToastUtils.showToast(mContext.getApplicationContext(),
                                        "请输入邀请码或兑换码");
                            } else {
                                pointsRedeem(code);
                                dialog.dismiss();
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
//                ToastUtils.showLongToast(mContext, "dismiss");
            }
        };

        OnCancelListener cancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogPlus dialog) {
//                ToastUtils.showLongToast(mContext, "cancel");
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
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
            UserLevelBean bean = GsonUtils.getInstance().transitionToBean(result, UserLevelBean
                    .class);
            if (bean == null)
                return;
            int k = mData.size();
            mData.clear();
            mAdapter.remove(0, k);
            int count = 9;
            if ("BUSINESS".equals(bean.getIdentity())) {
                count = 10;
            }
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    mData.add(0, bean.getFlow());
                } else if (i == 7) {
                    mData.add(7, bean.getIntegral());
                } else {
                    mData.add(i, "");
                }
            }
            mAdapter.add(0, mData.size(), mData);
            if (bean.getCode() == 200) {
                isLogin = true;
                mIntegral = bean.getIntegral();
                setHeaderData(200, bean.getAvatar(), bean.getNickname());
            }
            if (bean.getCode() == 437) {
                isLogin = false;
                setHeaderData(437, null, null);
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
        }
    }

    private void setHeaderData(int code, String url, String nickname) {
        if(tvGreeting == null||icProfile == null)
            return;
        if (code == 200) {
            tvGreeting.setText(String.format(mContext.getResources().getString(R.string
                    .say_hello), nickname));
            Glide.with(this).load(url)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .placeholder(R.drawable.user_profile)
                    .crossFade().into(icProfile);
        } else {
            icProfile.setImageResource(R.drawable.user_profile);
            tvGreeting.setText("未登录");
        }
    }
}
