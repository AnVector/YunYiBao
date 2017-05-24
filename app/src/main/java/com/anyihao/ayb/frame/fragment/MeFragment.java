package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MeAdapter;
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

import java.util.Arrays;
import java.util.List;

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
    private MeAdapter mAdapter;
    String[] array = new String[]{"我的流量", "流量商城", "流量报表", "充值记录", "邀请好友", "输入邀请码", "系统赠送记录",
            "我的积分", "商家特权", "授权设备管理"};
    private List<String> mData = Arrays.asList(array);

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
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbarTitle.setText(getString(R.string.me));
        icProfile.setImageResource(R.drawable.user_profile);
        mAdapter = new MeAdapter(getContext(), R.layout.item_me);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        mRecyclerview.setHasFixedSize(true);
        mAdapter.add(0, mData.size(), mData);
        tvGreeting.setText("小明，你好");
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
                startActivity(intent);
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