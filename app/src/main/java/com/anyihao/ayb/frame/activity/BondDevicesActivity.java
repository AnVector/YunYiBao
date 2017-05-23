package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.BondDeviceAdapter;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class BondDevicesActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    private BondDeviceAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    protected LinearLayoutManager layoutManager;
    //    private ItemTouchHelper mItemTouchHelper;
    private String[] array = new String[]{"AYM-100201", "AYM-100201", "AYM-100201", "AYM-100201", "AYM-100201", "AYM-100201", "AYM-100201",
            "AYM-100201"};
    private List<String> mData = Arrays.asList(array);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bond_devices;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleMid.setText(getString(R.string.device_list));
        recyclerView.setHasFixedSize(false);
        mAdapter = new BondDeviceAdapter(mData);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setEmptyViewPolicy(UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration
//                (informationAdapter);
//        ultimateRecyclerView.addItemDecoration(headersDecor);
        //bug 设置加载更多动画会使添加的数据延迟显示
//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setEmptyView(R.layout.empty_view_no_message, UltimateRecyclerView
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
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
            }
        });
//        mAdapter.setOnDragStartListener(new UltimateViewAdapter.OnStartDragListener() {
//
//            @Override
//            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//                mItemTouchHelper.startDrag(viewHolder);
//            }
//        });
//        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener
//                () {
//            @Override
//            public void onRefresh() {
//
//            }
//        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
//                Intent intent = new Intent(RechargeRecordActivity.this, RechargeRecordDetailsActivity.class);
//                startActivity(intent);
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
