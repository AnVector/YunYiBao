package com.anyihao.ayb.frame.activity;

import android.app.DownloadManager;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.AppUtils;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.AboutUsAdapter;
import com.anyihao.ayb.bean.KeyValueBean;
import com.anyihao.ayb.bean.VersionInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.anyihao.ayb.utils.UpdateConfig;
import com.anyihao.ayb.utils.Updater;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class AboutUsActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.activity_about_us)
    LinearLayout activityAboutUs;
    private AboutUsAdapter mAdapter;
    private List<KeyValueBean> mData = new LinkedList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText(getString(R.string.about_us));
        mAdapter = new AboutUsAdapter(this, R.layout.item_about_us);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mData.clear();
        mData.addAll(generateKeyVauleBean());
        mAdapter.add(0, mData.size(), mData);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fakeStatusBar.setVisibility(View.VISIBLE);
//        }
    }

//    @Override
//    protected void setStatusBarTheme() {
//        super.setStatusBarTheme();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            StatusBarUtil.setTranslucentForImageView(AboutUsActivity.this, 0, activityAboutUs);
//        }
//    }

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
                if (o instanceof KeyValueBean) {
                    String title = ((KeyValueBean) o).getTitle();
                    if ("当前版本".equals(title)) {
                        checkUpdate();
                    }
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

    }

    private void checkUpdate() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "VERCTRL");
        params.put("ver", AppUtils.getAppVersionName(this));
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .createTask());
    }

    private List<KeyValueBean> generateKeyVauleBean() {
        List<KeyValueBean> beans = new LinkedList<>();
        beans.add(0, new KeyValueBean().setTitle("当前版本").setValue(AppUtils.getAppVersionName
                (this)));
        beans.add(1, new KeyValueBean().setTitle("客服电话").setValue("0571-7598279"));
        beans.add(2, new KeyValueBean().setTitle("微信公众号").setValue("云逸宝WIFI"));
        beans.add(3, new KeyValueBean().setTitle("官方网站").setValue("www.aybwifi.com"));
        return beans;
    }

    private void downloadApkFile(String downloadUrl) {
        UpdateConfig config = new UpdateConfig.Builder(getApplicationContext())
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.download_hint))
                .setFileUrl(downloadUrl)
                .setCanMediaScanner(true)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false)
                .setFilename(renameApkFile(downloadUrl))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .build();
        Updater.getInstance().download(config);
    }

    private String renameApkFile(String downloadUrl) {
        String[] downloadArray = downloadUrl.split("/");
        int index = downloadArray.length - 1;
        return downloadArray[index];
    }

    private void versionUpdate(String versionName, String downloadUrl) {
        if (TextUtils.isEmpty(versionName) || TextUtils.isEmpty(downloadUrl)) {
            ToastUtils.showToast(getApplicationContext(), "已是最新版本");
            return;
        }
        if (compareVersion(versionName)) {
            showDialog(downloadUrl);
        } else {
            ToastUtils.showToast(getApplicationContext(), "已是最新版本");
        }
    }

    private boolean compareVersion(String versionName) {
        String latestVersion = versionName.replace(".", "");
        String currentVersion = AppUtils.getAppVersionName(this).replace(".", "");
        return latestVersion.compareTo(currentVersion) > 0;
    }

    private void showDialog(final String downloadUrl) {
        Holder holder = new ViewHolder(LayoutInflater.from(this).inflate(R.layout.dialog_confirm,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.tv_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(getString(R.string.new_version_update_hint));
        btnLeft.setText(getString(R.string.refuse_to_update));
        btnRight.setText(getString(R.string.agree_to_update));
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
                        ToastUtils.showToast(getApplicationContext(), "正在下载云逸宝...");
                        downloadApkFile(downloadUrl);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentHeight(DensityUtils.dp2px(this, 195))
                .setContentWidth(DensityUtils.dp2px(this, 298))
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            VersionInfoBean bean = GsonUtils.getInstance().transitionToBean(result,
                    VersionInfoBean.class);
            if (bean == null) {
                return;
            }
            if (bean.getCode() == 200) {
                versionUpdate(bean.getVersion(), bean.getDownloadLink());
            }
        }
    }
}
