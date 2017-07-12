package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UTabAdapter;
import com.anyihao.ayb.bean.PackageListBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.fragment.PackageFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RechargeActivity extends ABaseActivity {


    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.btn_confirm_to_recharge)
    AppCompatButton btnConfirmToRecharge;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_flow)
    TextView tvFlow;
    private UTabAdapter mTabAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitleArray = new String[]{"超值包", "月包", "3天包", "日包", "季包"};
    private String[] mFlowTypes = new String[]{"VIP", "MONTH", "THREE", "DAY", "SEASON"};
    private List<String> mTitles = Arrays.asList(mTitleArray);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.data_flow_charge));
        tabLayout.setupWithViewPager(mViewpager);
        getUserInfo();
    }

    private void initViewPager() {
        PackageFragment fragment;
        for (int i = 0; i < 5; ++i) {
            fragment = new PackageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("flowType", mFlowTypes[i]);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        mTabAdapter = new UTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewpager.setAdapter(mTabAdapter);
        mViewpager.setCurrentItem(0, true);
    }

    private void getUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAYINFO");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("flowType", "VIP");
        params.put("userType", PreferencesUtils.getString(getApplicationContext(),
                "userType", ""));
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnConfirmToRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageFragment fragment = (PackageFragment) mFragments.get(mViewpager
                        .getCurrentItem());
                if (fragment == null)
                    return;
                if (fragment.isVisible() && !fragment.isDetached()) {
                    Intent intent = new Intent(RechargeActivity.this, PayActivity.class);
                    intent.putExtra("money", fragment.getMoney());
                    intent.putExtra("amount", fragment.getAmount());
                    intent.putExtra("expires", fragment.getExpires());
                    intent.putExtra("packageID", fragment.getPackageID());
                    startActivity(intent);
                }
            }
        });
    }

    private void setAmountTextStyle(double amount) {
        if (tvFlow == null)
            return;
        String txt = String.format(getResources().getString(R.string
                .data_available), transferAmount(amount));
        SpannableString spannableString = new SpannableString(txt);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2DA8F4"));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(colorSpan, 7, spannableString.length(), Spanned
                .SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, 7, spannableString.length(), Spanned
                .SPAN_INCLUSIVE_EXCLUSIVE);
        tvFlow.setText(spannableString);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            PackageListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    PackageListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                tvNickname.setText(String.format(getResources().getString(R.string
                        .recharge_nick_name), bean.getNickname()));
                setAmountTextStyle(bean.getTotalUseFlow());
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }

    private String transferAmount(double amount) {
        if (amount > 1024) {
            BigDecimal bg = new BigDecimal(amount / 1024);
            amount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return amount + "G";
        }
        return amount + "M";
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(getApplicationContext(), error);
        }
    }
}
