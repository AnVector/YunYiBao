package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.chaychan.viewlib.PowerfulEditText;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class AddAuthDeviceActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar_title_right)
    TextView toolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_help)
    TextView tvHelp;
    @BindView(R.id.btn_add_auth_device)
    AppCompatButton btnAddAuthDevice;
    @BindView(R.id.et_mac_address)
    PowerfulEditText etMacAddress;
    @BindView(R.id.rg_device_type)
    RadioGroup rgDeviceType;
    private String macAddress;
    public static final int RESULT_ADD_AUTH_DEVICE_CODE = 0X0007;
    private int mStatus = 0;
    private String mRemark = "";
    private String mMessage = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_auth_device;
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
        toolbarTitleMid.setText(getString(R.string.add_auth_device));
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
                macAddress = etMacAddress.getText().toString().trim();
                if (StringUtils.isEmpty(macAddress)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入设备Mac地址", R.layout.toast, R
                            .id.tv_message);
                    return;
                }

                int id = rgDeviceType.getCheckedRadioButtonId();
                if (id == -1) {
                    ToastUtils.showToast(getApplicationContext(), "请选择要添加的设备类型");
                    return;
                }
                switch (id) {
                    case R.id.rbt_phone:
                        mRemark = "POHONE";
                        break;
                    case R.id.rbt_pad:
                        mRemark = "PAD";
                        break;
                    case R.id.rbt_pc:
                        mRemark = "PC";
                        break;
                    case R.id.rbt_other:
                        mRemark = "NONE";
                        break;
                    default:
                        break;
                }
                addAuthorizedDevice();
            }
        });

    }

    private void addAuthorizedDevice() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "AUTHORIZEADD");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("mac", macAddress);
        params.put("remarks", mRemark);
        params.put("addStatus", mStatus + "");

        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setParams(params)
                .setPage(1)
                .setActionType(mStatus)
                .setUrl(GlobalConsts.PREFIX_URL)
                .createTask());
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
        if (bean == null)
            return;

        if (actionType == 0) {
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                Intent intent = new Intent();
                setResult(RESULT_ADD_AUTH_DEVICE_CODE, intent);
                finish();
            } else if (bean.getCode() == 493) {
                mStatus = 1;
                mMessage = bean.getMsg();
                showDialog();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

        if (actionType == 1) {
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                Intent intent = new Intent();
                setResult(RESULT_ADD_AUTH_DEVICE_CODE, intent);
                finish();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(getApplicationContext(), "网络连接失败，请检查网络设置");
        }
    }

    private void showDialog() {
        Holder holder = new ViewHolder(LayoutInflater.from(this).inflate(R.layout.confirm_dialog,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.dia_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(mMessage + "?");
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
                        addAuthorizedDevice();
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
