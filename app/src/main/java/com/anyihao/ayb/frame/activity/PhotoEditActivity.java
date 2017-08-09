package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static android.os.Environment.getExternalStorageDirectory;

public class PhotoEditActivity extends ABaseActivity {

    @BindView(R.id.cropView)
    CropView cropView;
    @BindView(R.id.toolbar_title_mid)
    TextView toolbarTitleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    private ArrayList<Uri> mUri;
    private Bitmap croppedBitmap;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo_edit;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mUri = intent.getParcelableArrayListExtra("uri");
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitleMid.setText("编辑");
        if (mUri != null && !mUri.isEmpty()) {
            cropView.of(mUri.get(0)).asSquare().initialize(this);
        }
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        croppedBitmap = cropView.getOutput();
                        if (croppedBitmap != null) {
                            File file = new File(getExternalStorageDirectory(),
                                    "cropped");
                            Uri destination = Uri.fromFile(file);
                            boolean save = CropUtil.saveOutput(PhotoEditActivity.this, destination,
                                    croppedBitmap, 90);
                            if (save) {
                                uploadPhoto(file);
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(getApplicationContext(), "请选择尺寸合适的图片");
                                }
                            });
                        }
                    }
                }.start();
            }
        });
    }

    private void uploadPhoto(File file) {
        if (file == null)
            return;
        Map<String, String> params = new HashMap<>();
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType", ""));
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST_FILE)
                .setUrl(GlobalConsts.FILE_PREFIX_URL)
                .setFile(file)
                .setParams(params)
                .setPage(1)
                .setActionType(0)
                .createTask());
    }


    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                ToastUtils.showToast(getApplicationContext(), "头像修改成功");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        }
    }
}
