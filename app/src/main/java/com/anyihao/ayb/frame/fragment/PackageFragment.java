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

public class PackageFragment extends ABaseFragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_package_desc)
    TextView tvPackageDesc;
    private DataFlowAdapter mAdapter;
    private String[] array = new String[]{"3G", "12G", "24G", "36G", "60G"};
    private String[] price = new String[]{"6元","39元","72元","108元","168元"};
    private String[] desArray = new String[]{"1G","1G","2G","3G","5G"};
    private List<String> mData = Arrays.asList(array);
    private View mCurrent;

    @Override
    protected void initData() {
        tvPackageDesc.setText("全国可用，即时生效，每月1G，共3个月");
        mAdapter = new DataFlowAdapter(getActivity(), R.layout.item_data_flow);
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
                if(position == 0){
                    tvPackageDesc.setText("全国流量，即时生效，每月"+desArray[position]+"，共3个月");
                }else {
                    tvPackageDesc.setText("全国流量，即时生效，每月"+desArray[position]+"，共12个月");
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_package;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
