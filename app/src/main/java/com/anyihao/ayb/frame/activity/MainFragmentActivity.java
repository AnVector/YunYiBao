package com.anyihao.ayb.frame.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.anyihao.androidbase.manager.ActivityManager;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UFragmentPagerAdapter;
import com.anyihao.ayb.frame.fragment.DiscoverFragment;
import com.anyihao.ayb.frame.fragment.HomeFragment;
import com.anyihao.ayb.frame.fragment.MeFragment;
import com.anyihao.ayb.frame.fragment.TaskFragment;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class MainFragmentActivity extends ABaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.radio_button_device)
    RadioButton mRadioButtonDevice;
    @BindView(R.id.radio_button_me)
    RadioButton mRadioButtonMe;
    private static boolean isExit = false;
    @BindView(R.id.radio_button_discovery)
    RadioButton mRadioButtonDiscovery;
    @BindView(R.id.radio_button_task)
    RadioButton mRadioButtonTask;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private UFragmentPagerAdapter uFragmentPagerAdapter;
    private DownloadManager mDownloadManager;
    private RadioButton mCurrent;

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
        StatusBarUtil.setTranslucentForImageViewInFragment(MainFragmentActivity.this, null);
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initViewPager();
        if (checkUpdate()) {
            String downloadUrl = "http://gdown.baidu" +
                    ".com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk";
            downloadApkFile(downloadUrl);
        }

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
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        String apkFileName = renameApkFile(downloadUrl);
        String apkFilePath = Environment
                .getExternalStorageDirectory() + "/Android/data/" +
                getPackageName() + "/files/" + Environment
                .DIRECTORY_DOWNLOADS + "/" + apkFileName;
        deleteOldApkFile(apkFilePath);
        DownloadManager.Request request =
                new DownloadManager.Request(Uri.parse(downloadUrl));
        //设置WIFI网络下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("今日头条");
        request.setDescription("今日头条正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,
                apkFileName);
        mDownloadManager.enqueue(request);
    }

    private String renameApkFile(String downloadUrl) {
        String[] downloadArray = downloadUrl.split("/");
        int index = downloadArray.length - 1;
        return downloadArray[index];
    }

    private boolean checkUpdate() {
        return false;
    }

    private void deleteOldApkFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    protected void initEvent() {

        mRadioButtonDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIcon();
                changeIcon(mRadioButtonDevice, R.drawable.device_focused);
                mCurrent = mRadioButtonDevice;
                mViewPager.setCurrentItem(0, true);
            }
        });

        mRadioButtonDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIcon();
                changeIcon(mRadioButtonDiscovery, R.drawable.discovery_focused);
                mCurrent = mRadioButtonDiscovery;
                mViewPager.setCurrentItem(1);
            }
        });

        mRadioButtonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIcon();
                changeIcon(mRadioButtonTask, R.drawable.task_focused);
                mCurrent = mRadioButtonTask;
                mViewPager.setCurrentItem(2);
            }
        });

        mRadioButtonMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIcon();
                changeIcon(mRadioButtonMe, R.drawable.me_focused);
                mCurrent = mRadioButtonMe;
                mViewPager.setCurrentItem(3, true);
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
            ToastUtils.showToast(getApplicationContext(), "再按一次退出程序", R.layout.toast, R.id
                    .tv_message);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ActivityManager.getInstance().finishAllActivity();
            System.exit(0);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
