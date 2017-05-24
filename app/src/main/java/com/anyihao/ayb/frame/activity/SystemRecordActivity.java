package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.SettingsAdapter;
import com.anyihao.ayb.adapter.SystemRecordAdapter;
import com.anyihao.ayb.listener.OnItemClickListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class SystemRecordActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private SystemRecordAdapter mAdapter;
    String[] array = new String[]{"首次注册", "首次购买流量", "首次租赁", "邀请好友入驻"};
    private List<String> mData = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_system_record;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbarTitleMid.setText(getString(R.string.system_gift_record));
        mAdapter = new SystemRecordAdapter(this, R.layout.item_system_record);
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

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(SystemRecordActivity.this, SysRecordDetailsActivity
                        .class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
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
