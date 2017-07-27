package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.RentedAdapter;
import com.anyihao.ayb.bean.RentedBean;
import com.anyihao.ayb.bean.RentedBean.DataBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.RentedDevicesActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.emptyview.emptyViewOnShownListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class RentedFragment extends ABaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    private RentedAdapter mAdapter;
    protected LinearLayoutManager layoutManager;
    private List<DataBean> mData = new ArrayList<>();
    private String status;
    private boolean isRefresh;

    @Override
    protected void initData() {
        initUltimateRV();
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString("status");
        }
        getBelongs();
    }

    private void initUltimateRV() {
        recyclerView.setHasFixedSize(false);
        mAdapter = new RentedAdapter(mData, R.layout.item_rented_device);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL, new emptyViewOnShownListener() {
            @Override
            public void onEmptyViewShow(View mView) {
                if (mView == null)
                    return;
                ImageView imvError = (ImageView) mView.findViewById(R.id.ic_error);
                TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
                if (imvError != null) {
                    imvError.setImageDrawable(getResources().getDrawable(R.drawable
                            .ic_no_binding_devices));
                }
                if (tvHint != null) {
                    tvHint.setText("暂无设备");
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getBelongs() {
        if (TextUtils.isEmpty(status))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MERCHANTDEVLIST");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("status", status);
        postForm(params, 1, 0);
    }

    private void returnBelongs(String vid) {
        if (TextUtils.isEmpty(vid))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "RETURN");
        params.put("merchantId", PreferencesUtils.getString(mContext.getApplicationContext(),
                "uid", ""));
        params.put("vid", vid);
        postForm(params, 1, 1);
    }

    private void postForm(Map<String, String> params, int page, int actionType) {
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(page)
                .setActionType(actionType)
                .createTask());
    }

    private void onLoadMore(List<DataBean> beans) {
        mAdapter.removeAllInternal(mData);
        mAdapter.insert(beans);
        if (isRefresh) {
            recyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
            recyclerView.reenableLoadmore();
        }
    }

    private void onLoadNoData() {
        if (recyclerView != null) {
            recyclerView.showEmptyView();
        }
    }


    @Override
    protected void initEvent() {

        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
                () {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getBelongs();
            }
        });


        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, RentedDevicesActivity.class);
                if (o instanceof DataBean) {
                    intent.putExtra("keyId", ((DataBean) o).getKeyId());
                }
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        mAdapter.setmOnItemButtonClickListener(new RentedAdapter.OnItemButtonClickListener() {
            @Override
            public void onItemButtonClick(ViewGroup parent, View view, DataBean bean, int
                    position) {
                if (bean != null) {
                    showDialog(bean.getPrintId());
                }
            }
        });

    }

    private void showDialog(final String vid) {
        Holder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout
                        .confirm_dialog,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.dia_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(getString(R.string.device_return_dialog_hint));
        btnLeft.setText(getString(R.string.cancel));
        btnRight.setText(getString(R.string.ok));
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
                        returnBelongs(vid);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setInAnimation(R.anim.fade_in_center)
                .setOutAnimation(R.anim.fade_out_center)
                .setOnClickListener(clickListener)
                .setContentWidth(DensityUtils.dp2px(mContext, 298f))
                .setContentHeight(DensityUtils.dp2px(mContext, 195f))
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            RentedBean bean = GsonUtils.getInstance().transitionToBean(result, RentedBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans.size() > 0) {
                    onLoadMore(beans);
                } else {
                    onLoadNoData();
                }
            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
                onLoadNoData();
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(mContext.getApplicationContext(), bean.getMsg());
            if (bean.getCode() == 200) {
                isRefresh = true;
                getBelongs();
            }
        }
    }

}
