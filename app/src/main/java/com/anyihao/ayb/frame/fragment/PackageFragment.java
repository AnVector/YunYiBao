package com.anyihao.ayb.frame.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.DataFlowAdapter;
import com.anyihao.ayb.bean.PackageListBean;
import com.anyihao.ayb.bean.PackageListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class PackageFragment extends ABaseFragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_package_desc)
    TextView tvPackageDesc;
    private DataFlowAdapter mAdapter;
    private List<DataBean> mData = new LinkedList<>();
    private String flowType;
    private String money;
    private String amount;
    private String expires;
    private String packageID;

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            flowType = bundle.getString("flowType");
            getPackageInfo();
        }
        mAdapter = new DataFlowAdapter(getActivity(), R.layout.item_data_flow);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
    }

    private void getPackageInfo() {
        if (StringUtils.isEmpty(flowType))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PAYINFO");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("flowType", flowType);
        params.put("userType", PreferencesUtils.getString(mContext.getApplicationContext(),
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

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof DataBean) {
                    expires = ((DataBean) o).getPkgDesc();
                    amount = ((DataBean) o).getFlow();
                    money = ((DataBean) o).getPrice();
                    packageID = ((DataBean) o).getPackageID();
                    tvPackageDesc.setText(expires);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    public String getMoney() {
        return money;
    }

    public String getAmount() {
        return amount;
    }

    public String getExpires() {
        return expires;
    }

    public String getPackageID() {
        return packageID;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_package;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            PackageListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    PackageListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                mAdapter.remove(0, mData.size());
                mData.clear();
                List<DataBean> beans = bean.getData();
                if (beans != null && !beans.isEmpty()) {
                    money = beans.get(0).getPrice();
                    amount = beans.get(0).getFlow();
                    expires = beans.get(0).getPkgDesc();
                    packageID = beans.get(0).getPackageID();
                    tvPackageDesc.setText(expires);
                    mData.addAll(beans);
                    mAdapter.add(0, mData.size(), mData);
                }

            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(mContext.getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(mContext.getApplicationContext(), error);
        }
    }
}
