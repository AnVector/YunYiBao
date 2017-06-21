package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MeAdapter;
import com.anyihao.ayb.bean.UserLevelBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.CreditActivity;
import com.anyihao.ayb.frame.activity.DeviceManageActivity;
import com.anyihao.ayb.frame.activity.FlowAccountActivity;
import com.anyihao.ayb.frame.activity.FlowChartActivity;
import com.anyihao.ayb.frame.activity.InviteFriendsActivity;
import com.anyihao.ayb.frame.activity.MeActivity;
import com.anyihao.ayb.frame.activity.MerchantPrivilegeActivity;
import com.anyihao.ayb.frame.activity.RechargeActivity;
import com.anyihao.ayb.frame.activity.RechargeRecordActivity;
import com.anyihao.ayb.frame.activity.SettingsActivity;
import com.anyihao.ayb.frame.activity.SystemRecordActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
    @BindView(R.id.ic_profile)
    ImageView icProfile;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    private MeAdapter mAdapter;

    private List<String> mData = new LinkedList<>();
    private String uid;
    private String userType;
    private String nickName;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initData() {
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        if(null!=actionBar){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        uid = PreferencesUtils.getString(mContext, "uid", "");
        userType = PreferencesUtils.getString(mContext, "userType", "");
        getUserInfo();
        fakeStatusBar.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbarTitle.setText(getString(R.string.me));
        icProfile.setImageResource(R.drawable.user_profile);
        mAdapter = new MeAdapter(getContext(), R.layout.item_me);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
    }


    private void getUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MINE");
        params.put("uid", uid);
        params.put("userType", userType);
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
    protected void initEvent() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof String) {
                    Intent intent;
                    switch (o.toString()) {
                        case "我的流量":
                            intent = new Intent(getActivity(), FlowAccountActivity.class);
                            startActivity(intent);
                            break;
                        case "流量商城":
                            intent = new Intent(getActivity(), RechargeActivity.class);
                            startActivity(intent);
                            break;
                        case "流量报表":
                            intent = new Intent(getActivity(), FlowChartActivity.class);
                            startActivity(intent);
                            break;
                        case "充值记录":
                            intent = new Intent(getActivity(), RechargeRecordActivity.class);
                            startActivity(intent);
                            break;
                        case "邀请好友":
                            intent = new Intent(getActivity(), InviteFriendsActivity.class);
                            startActivity(intent);
                            break;
                        case "输入邀请码":
                            showDialog();
                            break;
                        case "系统赠送记录":
                            intent = new Intent(getActivity(), SystemRecordActivity.class);
                            startActivity(intent);
                            break;
                        case "我的积分":
                            intent = new Intent(getActivity(), CreditActivity.class);
                            startActivity(intent);
                            break;
                        case "商家特权":
                            intent = new Intent(getActivity(), MerchantPrivilegeActivity.class);
                            startActivity(intent);
                            break;
                        case "授权设备管理":
                            intent = new Intent(getActivity(), DeviceManageActivity.class);
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

        tvGreeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("userType", userType);
                startActivity(intent);
            }
        });
    }

    private void showDialog() {
        Holder holder = new ViewHolder(R.layout.me_dialog_content);
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

        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
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
            UserLevelBean bean = GsonUtils.getInstance().transitionToBean(result, UserLevelBean
                    .class);
            if (bean == null)
                return;
            nickName = bean.getNickname();
            mData.add(bean.getFlow());
            mData.add("shop");
            mData.add("chart");
            mData.add("history");
            mData.add("friends");
            mData.add("code");
            mData.add(bean.getLevel());
            mData.add("privilege");
            mData.add("management");
            mAdapter.add(0, mData.size(), mData);
            tvGreeting.setText(nickName + "，你好");
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
