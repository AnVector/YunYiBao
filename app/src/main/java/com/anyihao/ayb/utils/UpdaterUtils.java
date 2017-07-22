package com.anyihao.ayb.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by chiclaim on 2016/05/18
 */
public class UpdaterUtils {

    private static final String KEY_DOWNLOAD_ID = "downloadId";

    public static void startInstall(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        context.startActivity(install);
    }

    /**
     * 下载的apk和当前程序版本比较
     *
     * @param context Context 当前运行程序的Context
     * @param path    apk file's location
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    public static boolean compare(Context context, String path) {

        PackageInfo apkInfo = getApkInfo(context, path);
        if (apkInfo == null) {
            Logger.d("apkInfo is null , return false");
            return false;
        }

        String localPackage = context.getPackageName();

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
            Logger.e("apk file packageName=" + apkInfo.packageName +
                    ",versionName=" + apkInfo.versionName);
            Logger.e("current app packageName=" + packageInfo.packageName +
                    ",versionName=" + packageInfo.versionName);
            //String appName = pm.getApplicationLabel(appInfo).toString();
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            if (apkInfo.packageName.equals(localPackage)) {
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取apk程序信息[packageName,versionName...]
     * read from Manifest.xml file
     * if not defined PackageInfo return null
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }


    /**
     * 要启动的intent是否可用
     *
     * @return boolean
     */
    public static boolean intentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 系统的下载组件是否可用
     *
     * @return boolean
     */
    public static boolean checkDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android" +
                    ".providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void showDownloadSetting(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (UpdaterUtils.intentAvailable(context, intent)) {
            context.startActivity(intent);
        }
    }

    public static long getLocalDownloadId(Context context) {
        return SpUtils.getInstance(context).getLong(KEY_DOWNLOAD_ID, -1L);
    }

    public static void saveDownloadId(Context context, long id) {
        SpUtils.getInstance(context).putLong(KEY_DOWNLOAD_ID, id);
    }
}


