package com.anyihao.ayb.frame.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class RadarActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.btn_call)
    AppCompatButton btnCall;
    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;

    /**
     * 获取布局文件Id
     *
     * @return layoutId
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_radar;
    }

    /**
     * 获取从上一页面传递的参数
     */
    @Override
    protected void getExtraParams() {

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        toolbarTitleMid.setText(getString(R.string.radar));
        tvLocation.setText("滨江区滨安路652号滨江慧港科技园");
        tvShopName.setText("一号店");
        tvPhoneNo.setText("188****8078");
    }

    /**
     * 初始化事件
     */
    @Override
    protected void initEvent() {

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
