package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.AppUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.androidbase.utils.MD5;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StatusBarUtil;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.LoginBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends ABaseActivity {

    private static final int REQUEST_CODE = 0x0001;
    @BindView(R.id.input_user_name)
    EditText etUserName;
    @BindView(R.id.input_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.btn_wx)
    ImageButton btnWx;
    @BindView(R.id.btn_qq)
    ImageButton btnQq;
    @BindView(R.id.btn_weibo)
    ImageButton btnWeibo;
    @BindView(R.id.retrieve_password)
    TextView retrievePassword;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar_title_right)
    TextView titleRight;
    @BindView(R.id.progressbar_circular)
    CircularProgressBar progressbarCircular;
    private String userName;
    private String password;
    private String loginId;
    private String userType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
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
        toolbar.setBackgroundColor(getResources().getColor(R.color.app_background_color));
        titleRight.setText(getString(R.string.register_hint));
    }

    @Override
    protected void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userName = etUserName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                loginByMobile();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        retrievePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GetVerifyCodeActivity.class);
                intent.putExtra("title", "找回密码");
                intent.putExtra("action", "MODIFYPWD");
                intent.putExtra("phoneNum", "");
                startActivity(intent);
            }
        });

        btnWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,
                        SHARE_MEDIA.WEIXIN, authListener);
            }
        });

        btnQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,
                        SHARE_MEDIA.QQ, authListener);
            }
        });

        btnWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,
                        SHARE_MEDIA.SINA, authListener);
            }
        });
    }

    @Override
    protected void setStatusBarTheme() {
        super.setStatusBarTheme();
        StatusBarUtil.setColor(this, Color.parseColor("#F6F6F6"), 0);
    }

    private void loginByMobile() {
        if (!validate()) {
           ToastUtils.showToast(getApplicationContext(), "请输入用户名或密码");
            return;
        }
        btnLogin.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "IN");
        params.put("loginId", userName);
        params.put("pwd", MD5.string2MD5(password));
        params.put("appType", "ANDROID");
        params.put("userType", "SJ");
        params.put("ver", AppUtils.getAppVersionName(this));
        progressbarCircular.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).start();
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    private void loginByThirdAccount(String loginId, String userType, String nickname, String
            avatar, String sex) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "IN");
        params.put("loginId", loginId);
        params.put("appType", "ANDROID");
        params.put("userType", userType);
        params.put("nickname", nickname);
        params.put("avatar", avatar);
        params.put("sex", sex);
        params.put("ver", AppUtils.getAppVersionName(this));
        progressbarCircular.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).start();
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    public boolean validate() {
        boolean valid = true;
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            valid = false;
        }
        return valid;
    }

//    private void updateValues() {
//        CircularProgressDrawable circularProgressDrawable;
//        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
//                .colors(getResources().getIntArray(R.array.yun_colors))
//                .sweepSpeed(1f)
//                .rotationSpeed(1f)
//                .strokeWidth(dpToPx(3))
//                .style(CircularProgressDrawable.Style.ROUNDED);
//        if (mCurrentInterpolator != null) {
//            b.sweepInterpolator(mCurrentInterpolator);
//        }
//        progressbarCircular.setIndeterminateDrawable(circularProgressDrawable = b.build());
//
//        // /!\ Terrible hack, do not do this at home!
//        circularProgressDrawable.setBounds(0,
//                0,
//                progressbarCircular.getWidth(),
//                progressbarCircular.getHeight());
//        progressbarCircular.setVisibility(View.INVISIBLE);
//        progressbarCircular.setVisibility(View.VISIBLE);
//    }

//    public int dpToPx(int dp) {
//        Resources r = getResources();
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                dp, r.getDisplayMetrics());
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == GetVerifyCodeActivity.RESULT_BIND_NEW_CODE) {
                finish();
            }
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
        LoginBean bean = GsonUtils.getInstance().transitionToBean(result, LoginBean
                .class);
        if (bean == null)
            return;
        if (bean.getCode() == 200) {
            if (bean.getBindStatus() == 1) {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
                PreferencesUtils.putString(getApplicationContext(), "uid", bean.getUid());
                PreferencesUtils.putString(getApplicationContext(), "userType", bean.getUserType());
                PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", true);
                JPushInterface.setAlias(this, -1, bean.getUid());
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, GetVerifyCodeActivity.class);
                intent.putExtra("action", "BIND");
                intent.putExtra("title", "绑定手机");
                intent.putExtra("appId", loginId);
                intent.putExtra("userType", userType);
                startActivityForResult(intent, REQUEST_CODE);
            }

        } else {
            ToastUtils.showToast(getApplicationContext(), bean.getMsg());
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
        super.onFailure(error, page, actionType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        if (progressbarCircular != null) {
            progressbarCircular.progressiveStop();
            progressbarCircular = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String nickname = "";
            String avatar = "";
            String sex = "";
            LogUtils.e(TAG, "platform=" + platform);
            LogUtils.e(TAG, "action = " + action);
            switch (platform) {
                case QQ:
                case WEIXIN:
                    userType = platform.toString();
                    loginId = data.get("uid");
                    nickname = data.get("screen_name");
                    avatar = data.get("iconurl");
                    sex = data.get("gender");
                    if (platform == SHARE_MEDIA.WEIXIN)
                        userType = "WX";
                    Logger.d(data);
                    break;
                case SINA:
                    userType = "WB";
                    loginId = data.get("uid");
                    avatar = data.get("iconurl");
                    nickname = data.get("name");
                    sex = data.get("gender");
                    Logger.d(data);
                    break;
                default:
                    break;
            }
            loginByThirdAccount(loginId, userType, nickname, avatar, sex);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Logger.e(TAG, "platform=" + platform);
            Logger.e(TAG, "action = " + action);
            Logger.e(TAG, "error = " + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Logger.e(TAG, "platform=" + platform);
            Logger.e(TAG, "action = " + action);
        }
    };
}

