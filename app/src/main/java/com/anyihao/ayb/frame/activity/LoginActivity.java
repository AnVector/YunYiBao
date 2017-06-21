package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.anyihao.androidbase.utils.MD5;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.StringUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.LoginBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.jaeger.library.StatusBarUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends ABaseActivity {

    private static final int REQUEST_SIGNUP = 1000;
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void setStatusBarTheme() {
        StatusBarUtil.setColor(LoginActivity.this, getResources().getColor(R.color
                .app_background_color), 0);
    }

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
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
                login();
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
                Intent intent = new Intent(LoginActivity.this, RetrievePwdActivity.class);
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

    private void login() {
        if (!validate()) {
            onLoginFailed("请输入用户名或密码");
            return;
        }
        btnLogin.setEnabled(false);
        JSONObject json = new JSONObject();
        try {
            json.put("cmd", "IN");
            json.put("loginId", userName);
            json.put("pwd", MD5.string2MD5(password));
            json.put("appType", "ANDROID");
            json.put("userType", "SJ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressbarCircular.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).start();
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL + "?cmd=IN" + "&" + "pwd=" +
                                MD5.string2MD5(password) + "&" + "appType=" + "ANDROID" + "&" +
                                "userType=" + "SJ" + "&" + "loginId=" + userName)
                        .setContent(json.toString())
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
    }

    public boolean validate() {
        boolean valid = true;
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
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
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    private void onLoginFailed(String message) {
        ToastUtils.showToast(getApplicationContext(), message, R.layout.toast, R.id.tv_message);
        btnLogin.setEnabled(true);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        LoginBean bean = GsonUtils.getInstance().transitionToBean(result, LoginBean
                .class);
        if (bean == null)
            return;
        if (bean.getCode() == 200) {
            btnLogin.setEnabled(true);
            ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                    .tv_message);
            PreferencesUtils.putString(getApplicationContext(), "uid", bean.getUid());
            PreferencesUtils.putString(getApplicationContext(), "userType", bean.getUserType());
            Intent intent = new Intent(LoginActivity.this, MainFragmentActivity.class);
            startActivity(intent);
            finish();
        } else {
            onLoginFailed(bean.getMsg());
        }
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
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
//            SocializeUtils.safeShowDialog(dialog);
//            UmengTool.getSignature(LoginActivity.this);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "成功了", Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "platform=" + platform);
            LogUtils.d(TAG, "action = " + action);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                LogUtils.d(TAG, "Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "platform=" + platform);
            LogUtils.d(TAG, "action = " + action);
            LogUtils.d(TAG, "error = " + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "platform=" + platform);
            LogUtils.d(TAG, "action = " + action);
        }
    };
}

