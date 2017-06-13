package com.anyihao.ayb.frame.activity;

import android.app.ProgressDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anyihao.ayb.R;

import butterknife.BindView;

public class SignUpActivity extends ABaseActivity {

    private static final String TAG = "SignUpActivity";

    @BindView(R.id.input_name)
    EditText etName;
    @BindView(R.id.input_address)
    EditText etAddress;
    @BindView(R.id.input_email)
    EditText etEmail;
    @BindView(R.id.input_mobile)
    EditText etMobile;
    @BindView(R.id.input_password)
    EditText etPassword;
    @BindView(R.id.input_reEnterPassword)
    EditText etReEnterPassword;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.link_login)
    TextView tvLogin;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();
        String reEnterPassword = etReEnterPassword.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();
        String reEnterPassword = etReEnterPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etName.setError("at least 3 characters");
            valid = false;
        } else {
            etName.setError(null);
        }

        if (address.isEmpty()) {
            etAddress.setError("Enter Valid Address");
            valid = false;
        } else {
            etAddress.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            etMobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            etMobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length()
                > 10 || !(reEnterPassword.equals(password))) {
            etReEnterPassword.setError("Password Do not match");
            valid = false;
        } else {
            etReEnterPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
