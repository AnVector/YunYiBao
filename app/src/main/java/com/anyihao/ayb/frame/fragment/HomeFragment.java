package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.MainAdapter;
import com.anyihao.ayb.frame.activity.ConnectedDevicesActivity;
import com.anyihao.ayb.frame.activity.HelpActivity;
import com.anyihao.ayb.frame.activity.MessageActivity;
import com.anyihao.ayb.frame.activity.QuestionsActivity;
import com.anyihao.ayb.frame.activity.RechargeActivity;
import com.anyihao.ayb.frame.activity.ScanActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;
import java.util.List;

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
    private MainAdapter mAdapter;
    String[] advertisement = new String[]{"流量大减价，一律两元！一律两元！", "流量大减价，一律两元！一律两元！"};
    String[] array = new String[]{"AYB-10086", "AYB-10010"};
    private List<String> mData = Arrays.asList(array);
    private AnimationDrawable animationDrawable;

    @Override
    protected void initData() {
        animationDrawable = (AnimationDrawable) ivBell.getDrawable();
        animationDrawable.start();
        toolbar.setBackground(null);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        toolbarTitle.setText(getString(R.string.yun_bao));
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
    }


    @Override
    protected void initEvent() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                return true;
            }
        });

        toolbarHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });

        networkLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConnectedDevicesActivity.class);
                startActivity(intent);
            }
        });

        tvLeasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = IntentIntegrator
                        .forSupportFragment(HomeFragment.this).setCaptureActivity
                                (ScanActivity.class);
                intentIntegrator
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("对准二维码，将会自动扫描")//写那句提示的话
                        .setOrientationLocked(false)//扫描方向固定
                        .setCaptureActivity(ScanActivity.class) //
                        // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
            }
        });

        tvMyDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RechargeActivity.class);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
