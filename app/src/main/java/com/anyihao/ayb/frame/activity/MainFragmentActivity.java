package com.anyihao.ayb.frame.activity;

import android.app.DownloadManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.anyihao.androidbase.manager.ActivityManager;
import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.AppUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UFragmentPagerAdapter;
import com.anyihao.ayb.bean.VersionInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.fragment.DiscoverFragment;
import com.anyihao.ayb.frame.fragment.HomeFragment;
import com.anyihao.ayb.frame.fragment.MeFragment;
import com.anyihao.ayb.frame.fragment.TaskFragment;
import com.anyihao.ayb.ui.CustomViewPager;
import com.anyihao.ayb.utils.UpdateConfig;
import com.anyihao.ayb.utils.Updater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 *
 */
public class MainFragmentActivity extends ABaseActivity {

    @BindView(R.id.radio_button_device)
    RadioButton mRadioButtonDevice;
    @BindView(R.id.radio_button_me)
    RadioButton mRadioButtonMe;
    private static boolean isExit = false;
    @BindView(R.id.radio_button_discovery)
    RadioButton mRadioButtonDiscovery;
    @BindView(R.id.radio_button_task)
    RadioButton mRadioButtonTask;
    @BindView(R.id.viewpager)
    CustomViewPager mViewPager;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private UFragmentPagerAdapter uFragmentPagerAdapter;
    private RadioButton mCurrent;
//    private int count;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_fragment;
    }

    @Override
    protected void setStatusBarTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.setStatusBarTheme();
            StatusBarUtil.setTranslucentForImageView(this, 0, mViewPager);
        } else {
            StatusBarUtil.setTranslucentForImageViewInFragment(MainFragmentActivity.this, null);
        }
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        getUpdateVersion();
    }

    private void getUpdateVersion() {
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

    private void initViewPager() {
        HomeFragment homeFragment = new HomeFragment();
        DiscoverFragment discoverFragment = new DiscoverFragment();
        TaskFragment taskFragment = new TaskFragment();
        MeFragment meFragment = new MeFragment();
        mFragmentList.add(homeFragment);
        mFragmentList.add(discoverFragment);
        mFragmentList.add(taskFragment);
        mFragmentList.add(meFragment);
        uFragmentPagerAdapter = new UFragmentPagerAdapter(getSupportFragmentManager(),
                mFragmentList);
        mViewPager.setAdapter(uFragmentPagerAdapter);
        mViewPager.setCurrentItem(0, true);
        mCurrent = mRadioButtonDevice;
        changeIcon(mRadioButtonDevice, R.drawable.device_focused);
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
        if (TextUtils.isEmpty(versionName) || TextUtils.isEmpty(downloadUrl))
            return;
        if (checkUpdate(versionName)) {
            downloadApkFile(downloadUrl);
        }
    }

    private boolean checkUpdate(String versionName) {
        String latestVersion = versionName.replace(".", "");
        String currentVersion = AppUtils.getAppVersionName(this).replace(".", "");
        return latestVersion.compareTo(currentVersion) > 0;
    }

    @Override
    protected void initEvent() {

//        mRadioButtonDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetIcon();
//                changeIcon(mRadioButtonDevice, R.drawable.device_focused);
//                mCurrent = mRadioButtonDevice;
//                mViewPager.setCurrentItem(0, true);
//            }
//        });

        mRadioButtonDevice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mViewPager.getCurrentItem() == 0)
                    return true;
                resetIcon();
                changeIcon(mRadioButtonDevice, R.drawable.device_focused);
                mCurrent = mRadioButtonDevice;
                mViewPager.setCurrentItem(0, true);
                return false;
            }
        });

//        mRadioButtonDiscovery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetIcon();
//                changeIcon(mRadioButtonDiscovery, R.drawable.discovery_focused);
//                mCurrent = mRadioButtonDiscovery;
//                mViewPager.setCurrentItem(1);
//            }
//        });

        mRadioButtonDiscovery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mViewPager.getCurrentItem() == 1)
                    return true;
                resetIcon();
                changeIcon(mRadioButtonDiscovery, R.drawable.discovery_focused);
                mCurrent = mRadioButtonDiscovery;
                mViewPager.setCurrentItem(1, true);
                return false;
            }
        });

//        mRadioButtonTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetIcon();
//                changeIcon(mRadioButtonTask, R.drawable.task_focused);
//                mCurrent = mRadioButtonTask;
//                mViewPager.setCurrentItem(2);
//            }
//        });

        mRadioButtonTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mViewPager.getCurrentItem() == 2)
                    return true;
                resetIcon();
                changeIcon(mRadioButtonTask, R.drawable.task_focused);
                mCurrent = mRadioButtonTask;
                mViewPager.setCurrentItem(2, true);
                return false;
            }
        });

//        mRadioButtonMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetIcon();
//                changeIcon(mRadioButtonMe, R.drawable.me_focused);
//                mCurrent = mRadioButtonMe;
//                mViewPager.setCurrentItem(3, true);
//            }
//        });

        mRadioButtonMe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mViewPager.getCurrentItem() == 3)
                    return true;
                resetIcon();
                changeIcon(mRadioButtonMe, R.drawable.me_focused);
                mCurrent = mRadioButtonMe;
                mViewPager.setCurrentItem(3, true);
                return false;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetIcon();
                switch (position) {
                    case 0:
                        changeIcon(mRadioButtonDevice, R.drawable.device_focused);
                        mCurrent = mRadioButtonDevice;
                        break;
                    case 1:
                        changeIcon(mRadioButtonDiscovery, R.drawable.discovery_focused);
                        mCurrent = mRadioButtonDiscovery;
                        break;
                    case 2:
                        changeIcon(mRadioButtonTask, R.drawable.task_focused);
                        mCurrent = mRadioButtonTask;
                        break;
                    case 3:
                        changeIcon(mRadioButtonMe, R.drawable.me_focused);
                        mCurrent = mRadioButtonMe;
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeIcon(RadioButton v, int resId) {
        Drawable iconTop = getResources().getDrawable(resId);
        iconTop.setBounds(0, 0, iconTop.getMinimumWidth(), iconTop.getMinimumHeight());
        v.setCompoundDrawables(null, iconTop, null, null);
    }

    private void resetIcon() {
        if (mCurrent == null)
            return;
        int id = mCurrent.getId();
        switch (id) {
            case R.id.radio_button_device:
                changeIcon(mCurrent, R.drawable.device_normal);
                break;
            case R.id.radio_button_discovery:
                changeIcon(mCurrent, R.drawable.discovery_normal);
                break;
            case R.id.radio_button_me:
                changeIcon(mCurrent, R.drawable.me_normal);
                break;
            case R.id.radio_button_task:
                changeIcon(mCurrent, R.drawable.task_normal);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        onAppExit();
    }

    private void onAppExit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.showToast(getApplicationContext(), "再按一次退出程序");
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ActivityManager.getInstance().finishAllActivity();
            System.exit(0);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            VersionInfoBean bean = GsonUtils.getInstance().transitionToBean(result,
                    VersionInfoBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                versionUpdate(bean.getVersion(), bean.getDownloadLink());
            }
        }
    }
}
