package com.anyihao.ayb.frame.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Window;

import com.anyihao.androidbase.acitivity.BKBaseActivity;
import com.anyihao.androidbase.mvp.IView;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.common.PresenterFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class ABaseActivity extends BKBaseActivity implements IView<Integer> {

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setColor(ABaseActivity.this, Color.parseColor("#FFFFFF"), 0);
        if (isMiui()) {
            setStatusBarDarkMode(ABaseActivity.this, true);
        }
    }

    private static boolean isMiui() {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version" +
                    ".name");
            if (!StringUtils.isEmpty(version)) {
                if (version.contains("V") && version.length() == 2) {
                    int verNum = Integer.parseInt(version.substring(1));
                    if (verNum >= 6) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void setStatusBarDarkMode(Activity activity, boolean darkMode) {
        Window window = activity.getWindow();
        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, darkMode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setBackground() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.app_background);
        this.getWindow().setBackgroundDrawable(drawable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PresenterFactory.getInstance().remove(this);
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        if (StringUtils.isEmpty(error))
            return;
        if (error.contains("ConnectException")) {
            ToastUtils.showToast(getApplicationContext(), "网络连接失败，请检查网络设置");
        } else if (error.contains("404")) {
            ToastUtils.showToast(getApplicationContext(), "未知异常");
        } else {
            ToastUtils.showToast(getApplicationContext(), error);
        }
    }
}
