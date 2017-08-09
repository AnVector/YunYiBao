package com.anyihao.ayb.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.appcompat.BuildConfig;

import com.anyihao.androidbase.utils.TextUtils;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

/**
 * Created by hkq325800 on 2017/4/14.
 */

public class PermissionSettingUtils {
    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "Huawei";//华为
    private static final String MANUFACTURER_MEIZU = "Meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";//小米
    private static final String MANUFACTURER_SONY = "Sony";//索尼
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "Letv";//乐视
    private static final String MANUFACTURER_ZTE = "ZTE";//中兴
    private static final String MANUFACTURER_YULONG = "YuLong";//酷派
    private static final String MANUFACTURER_LENOVO = "LENOVO";//联想

    /**
     * 此函数可以自己定义
     *
     * @param context
     */
    public static void goToSettings(Context context) {
        if (context == null)
            return;
        switch (Build.MANUFACTURER) {
            case MANUFACTURER_HUAWEI:
                Huawei(context);
                break;
            case MANUFACTURER_MEIZU:
                Meizu(context);
                break;
            case MANUFACTURER_XIAOMI:
                Xiaomi(context);
                break;
            case MANUFACTURER_SONY:
                Sony(context);
                break;
            case MANUFACTURER_OPPO:
                OPPO(context);
                break;
            case MANUFACTURER_LG:
                LG(context);
                break;
            case MANUFACTURER_LETV:
                Letv(context);
                break;
            default:
                applicationInfo(context);
                Logger.e("goToSetting", "目前暂不支持此系统");
                break;
        }
    }

    private static void Huawei(Context context) {

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei" +
                ".permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }

    }

    private static void Meizu(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }

    }

    private static void Xiaomi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        if (getMiUiVersion() == 6 || getMiUiVersion() == 7) {
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions" +
                    ".AppPermissionsEditorActivity");
        } else {
            ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui" +
                    ".permcenter.permissions.PermissionsEditorActivity");
            intent.setComponent(componentName);
        }
        intent.putExtra("extra_pkgname", context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }
    }

    private static int getMiUiVersion() {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version" +
                    ".name");
            if (!TextUtils.isEmpty(version)) {
                if (version.contains("V") && version.length() == 2) {
                    return Integer.parseInt(version.substring(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void Sony(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta" +
                ".SomcCTAMainActivity");
        intent.setComponent(comp);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }
    }

    private static void OPPO(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter" +
                ".permission.PermissionManagerActivity");
        intent.setComponent(comp);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }

    }

    private static void LG(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings" +
                ".Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }

    }

    private static void Letv(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android" +
                ".letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }

    }

    /**
     * 只能打开到自带安全软件
     *
     * @param context
     */
    private static void _360(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360" +
                ".mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d(e.toString());
            applicationInfo(context);
        }

    }

    /**
     * 应用信息界面
     *
     * @param context
     */
    private static void applicationInfo(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings" +
                    ".InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context
                    .getPackageName());
        }
        context.startActivity(localIntent);
    }

    /**
     * 系统设置界面
     *
     * @param context
     */
    private static void systemConfig(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }
}
