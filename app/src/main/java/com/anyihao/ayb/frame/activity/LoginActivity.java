package com.anyihao.ayb.frame.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.LoginResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import butterknife.BindView;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends ABaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 1000;
    private static final int LOGIN_AUTHENTICATION_SUCCESS = 1001;
    private static final int LOGIN_AUTHENTICATION_FAILURE = 1002;

    private static final int LOGIN_BY_USERNAME = 0;
    private static final int LONGIN_BY_MOBILE_PHONE = 1;
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
    //    @BindView(R.id.verification_login)
//    TextView verificationLogin;
    @BindView(R.id.retrieve_password)
    TextView retrievePassword;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar_title_right)
    TextView titleRight;
    private ProgressDialog dialog;
    private String userName;
    private String password;
    private int accountType = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_AUTHENTICATION_SUCCESS:
                    break;
                case LOGIN_AUTHENTICATION_FAILURE:
                    break;
                case REQUEST_SIGNUP:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        titleRight.setText(getString(R.string.register_hint));
        dialog = new ProgressDialog(LoginActivity.this,
                ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                login();
                Intent intent = new Intent(LoginActivity.this, MainFragmentActivity.class);
                startActivity(intent);
            }
        });

//        verificationLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleLoginMode();
//            }
//        });
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
                Intent intent = new Intent(LoginActivity.this, RetrievePwdActivity.class);
                startActivity(intent);
//                finish();
//                if (accountType == 0) {
//                    ToastUtils.showLongToast(LoginActivity.this, "找回密码");
//                } else {
//                    ToastUtils.showLongToast(LoginActivity.this, "获取验证码");
//                }
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

//    private void toggleLoginMode() {
//        accountType = (accountType + 1) % 2;
//        changeTitle();
//        changeAccountIcon();
//    }
//
//    private void changeTitle() {
//        if (accountType == 1) {
//            verificationLogin.setText("账号密码登录");
//            retrievePassword.setText("获取验证码");
//            etUserName.setHint("请输入手机号");
//            etPassword.setHint("请输入验证码");
//        } else {
//            verificationLogin.setText("手机动态码登录");
//            retrievePassword.setText("忘记密码？");
//            etUserName.setHint("请输入用户名或手机号");
//            etPassword.setHint("请输入密码");
//        }
//    }

//    private void changeAccountIcon() {
//        Drawable icUsn, icPwd;
//        Resources res = getResources();
//        if (accountType == 1) {
//            icUsn = res.getDrawable(R.drawable.ic_mobile);
//            icPwd = res.getDrawable(R.drawable.ic_message);
//        } else {
//            icUsn = res.getDrawable(R.drawable.ic_profile);
//            icPwd = res.getDrawable(R.drawable.ic_lock);
//        }
//
//        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
//        icUsn.setBounds(0, 0, icUsn.getMinimumWidth(), icUsn.getMinimumHeight());
//        icPwd.setBounds(0, 0, icPwd.getMinimumWidth(), icPwd.getMinimumHeight());
//        etUserName.setCompoundDrawables(icUsn, null, null, null); //设置左图标
//        etPassword.setCompoundDrawables(icPwd, null, null, null); //设置左图标
//    }

    private void login() {
        if (!validate()) {
            onLoginFailed("用户名或密码不正确");
            return;
        }
        btnLogin.setEnabled(false);
        dialog.setIndeterminate(true);
        dialog.setMessage("登录中...");
        dialog.show();
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.GET)
                .setUrl(GlobalConsts.USER_LOGIN + "uid=" + userName + "&pwd=" + password)
                .setPage(1).createTask());
    }

    public boolean validate() {
        boolean valid = true;
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (StringUtils.isEmpty(userName)) {
            etUserName.setError("用户名不能为空");
            valid = false;
        } else {
            etUserName.setError(null);
        }

        if (StringUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("密码长度不能小于6位");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainFragmentActivity.class);
        startActivity(intent);
        dialog.dismiss();
        dialog = null;
        finish();
    }

    public void onLoginFailed(String reason) {
        ToastUtils.showShortToast(LoginActivity.this, reason);
        dialog.dismiss();
        btnLogin.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }
//
//    @Override
//    public void onBackPressed() {
//        // Disable going back to the MainActivity
//        moveTaskToBack(true);
//    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        LoginResultBean bean = GsonUtils.getInstance().transitionToBean(result, LoginResultBean
                .class);
        Logger.e(result);
        if ("200".equals(bean.getCode())) {
            onLoginSuccess();
        } else {
            onLoginFailed(bean.getReason());
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        onLoginFailed(error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
//            UmengTool.getSignature(LoginActivity.this);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "成功了", Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "platform=" + platform);
            LogUtils.d(TAG, "action = " + action);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                LogUtils.d(TAG, "Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "platform=" + platform);
            LogUtils.d(TAG, "action = " + action);
            LogUtils.d(TAG, "error = " + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "platform=" + platform);
            LogUtils.d(TAG, "action = " + action);
        }
    };
}

