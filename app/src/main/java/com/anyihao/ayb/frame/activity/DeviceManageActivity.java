package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.AuthDeviceAdapter;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean;
import com.anyihao.ayb.bean.AuthorizedDeviceListBean.DataBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DeviceManageActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_add_auth_device)
    AppCompatButton btnAddAuthDevice;
    @BindView(R.id.ic_error)
    ImageView icError;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    private AuthDeviceAdapter mAdapter;
    private List<DataBean> mData = new ArrayList<>();
    public static final int REQUEST_ADD_AUTH_DEVICE_CODE = 0X00006;
    private int keyId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_manage;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.authorization_device));
        mAdapter = new AuthDeviceAdapter(this, R.layout.item_auth_device);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        rlEmpty.setVisibility(View.GONE);
        getAuthorizedDevices();
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnAddAuthDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceManageActivity.this, AddAuthDeviceActivity.class);
                startActivityForResult(intent, REQUEST_ADD_AUTH_DEVICE_CODE);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof DataBean) {
                    keyId = ((DataBean) o).getKeyId();
                    showDialog();
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    private void getAuthorizedDevices() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZELIST");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
    }

    private void releaseAuthority() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZE");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("keyId", keyId + "");

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(1)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            AuthorizedDeviceListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    AuthorizedDeviceListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                mAdapter.remove(0, mData.size());
                mData.clear();
                if (beans.size() > 0) {
                    mData.addAll(beans);
                    mAdapter.add(0, mData.size(), mData);
                } else {
                    icError.setImageDrawable(getResources().getDrawable(R.drawable.no_auth_device));
                    tvHint.setText("暂无授权设备");
                    rlEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 1) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), "解除授权成功");
                getAuthorizedDevices();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_AUTH_DEVICE_CODE && resultCode == AddAuthDeviceActivity
                .RESULT_ADD_AUTH_DEVICE_CODE) {
            getAuthorizedDevices();
        }
    }

    private void showDialog() {
        Holder holder = new ViewHolder(LayoutInflater.from(this).inflate(R.layout.confirm_dialog,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.dia_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(getString(R.string.mac_address_release_hint));
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
                        releaseAuthority();
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "dismiss");
            }
        };

        OnCancelListener cancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "cancel");
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentHeight(DensityUtils.dp2px(this, 195))
                .setContentWidth(DensityUtils.dp2px(this, 298))
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }
}
