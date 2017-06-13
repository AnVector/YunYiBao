package com.anyihao.ayb.frame.fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.DataFlowAdapter;
import com.anyihao.ayb.listener.OnItemClickListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class DaysFragment extends ABaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_package_desc)
    TextView tvPackageDesc;
    private DataFlowAdapter mAdapter;
    private String[] array = new String[]{"1G", "2G", "3G", "5G"};
    private String[] price = new String[]{"5元","9元","12元","20元"};
    private List<String> mData = Arrays.asList(array);
    private View mCurrent;

    @Override
    protected void initData() {
        tvPackageDesc.setText(mContext.getString(R.string.days_package_desc));
        mAdapter = new DataFlowAdapter(mContext, R.layout.item_data_flow);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerview.setHasFixedSize(true);
        mAdapter.setPrice(price);
        mAdapter.add(0, mData.size(), mData);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if(mCurrent!=null){
                    mCurrent.setBackgroundResource(R.drawable.data_flow_item_normal_bg);
                    ((TextView)mCurrent.findViewById(R.id.tv_price)).setTextColor(getResources().getColor(R.color.data_flow_item_price_text_color));
                    ((TextView)mCurrent.findViewById(R.id.tv_data_amount)).setTextColor(getResources().getColor(R.color.toolbar_title_color));
                }
                view.setBackgroundResource(R.drawable.data_flow_item_focus_bg);
                ((TextView)view.findViewById(R.id.tv_price)).setTextColor(getResources().getColor(R.color.white));
                ((TextView)view.findViewById(R.id.tv_data_amount)).setTextColor(getResources().getColor(R.color.white));
                mCurrent = view;
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_year;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
