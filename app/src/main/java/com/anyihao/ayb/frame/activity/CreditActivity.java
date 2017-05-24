package com.anyihao.ayb.frame.activity;

import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.CreditAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
public class CreditActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_points)
    TextView tvPoints;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private CreditAdapter mAdapter;
    String[] array = new String[]{"购买流量", "兑换流量", "购买会员流量", "下载王者荣耀"};
    private List<String> mData = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_credit;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/方正兰亭特黑简体.TTF");
        tvPoints.setTypeface(fontFace);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setBackground(null);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        toolbarTitle.setText(getString(R.string.my_points));
        mAdapter = new CreditAdapter(this, R.layout.item_credit);
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

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}