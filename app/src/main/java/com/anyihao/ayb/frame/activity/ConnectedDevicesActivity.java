package com.anyihao.ayb.frame.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.ConnectedDeviceAdapter;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class ConnectedDevicesActivity extends ABaseActivity {


    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private ConnectedDeviceAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    protected LinearLayoutManager layoutManager;
    //    private ItemTouchHelper mItemTouchHelper;
    private String[] array = new String[]{"本机", "Jack", "Rose", "Frank", "Bruce",
            "Tonny", "Emmy", "Jimy",
            "Bill"};
    private Dialog bottomDialog;
    private TextView tvSayHi;
    private TextView tvBriberyMoney;
    private TextView tvCancel;
    private List<String> mData = Arrays.asList(array);
    private String phoneNum;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_connected_devices;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initBottomDialog();
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setBackground(null);
        toolbarTitle.setText(getString(R.string.connected_devices));
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));

        recyclerView.setHasFixedSize(false);
        mAdapter = new ConnectedDeviceAdapter(mData, R.layout.item_connected_device_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
//                (informationAdapter);
//        ultimateRecyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView
                .EMPTY_CLEAR_ALL);
//        recyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout
//                .parallax_recyclerview_header, recyclerView.mRecyclerView, false));
//        recyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback
                (mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView.mRecyclerView);
//        recyclerView.reenableLoadmore();
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setTranslucentForImageView(this, 0, llContainer);
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
                phoneNum = o.toString();
                if (bottomDialog != null) {
                    bottomDialog.show();
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        tvSayHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectedDevicesActivity.this, SayHiActivity.class);
                intent.putExtra("uid", phoneNum);
                startActivity(intent);
            }
        });

        tvBriberyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectedDevicesActivity.this, BriberyMoneyActivity
                        .class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomDialog != null) {
                    bottomDialog.dismiss();
                }
            }
        });

    }

    private void initBottomDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View dialogContentView = LayoutInflater.from(this).inflate(R.layout
                .dialog_content_circle, null);
        tvSayHi = (TextView) dialogContentView.findViewById(R.id.tv_say_hi);
        tvBriberyMoney = (TextView) dialogContentView.findViewById(R.id.tv_bribery_money);
        tvCancel = (TextView) dialogContentView.findViewById(R.id.tv_cancle);
        bottomDialog.setContentView(dialogContentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dialogContentView
                .getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtils.dp2px(this,
                16f);
        params.bottomMargin = DensityUtils.dp2px(this, 8f);
        dialogContentView.setLayoutParams(params);
        if (bottomDialog.getWindow() != null) {
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
