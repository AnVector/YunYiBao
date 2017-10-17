package com.anyihao.ayb.frame.presenter;

import android.text.TextUtils;

import com.anyihao.androidbase.mvp.IView;
import com.anyihao.androidbase.mvp.PresenterCompat;
import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.utils.LogUtils;
import com.library.http.okhttp.OkHttpUtils;
import com.library.http.okhttp.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 所有的类都必须添加创建者信息
 * Created by Administrator on 2016/10/28 0028.
 */

public class Presenter extends PresenterCompat {

    public Presenter(IView view) {
        super(view);
    }

    @Override
    protected void post(final Task task) {
        if (isViewDestroyed()) {
            return;
        }
        Map<String, String> params = task.getParams();
        if (params == null) {
            return;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || value == null) {
                return;
            }
        }
        Logger.d(params);
        OkHttpUtils
                .post()
                .url(task.getUrl())
                .addHeader("Content-Type", "text/plain")
                .params(task.getParams())
                .tag(getView())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        LogUtils.e(e.toString());
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onSuccess(response, task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }
                });
    }

    @Override
    protected void postFile(final Task task) {
        if (isViewDestroyed()) {
            return;
        }
        Map<String, String> params = task.getParams();
        Logger.d(params);
        File file = task.getFile();
        String url = task.getUrl();
        if (params == null || file == null || TextUtils.isEmpty(url)) {
            return;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || value == null) {
                return;
            }
        }

        // form 表单形式上传
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"picture\";" +
                        "filename=\"header.png\""), body)
                .addFormDataPart("uid", params.get("uid"))
                .addFormDataPart("userType", params.get("userType"))
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).tag(getView())
                .build();
        OkHttpUtils.getInstance().getOkHttpClient().newBuilder().readTimeout(3 * 1000, TimeUnit
                .MILLISECONDS).build().newCall(request)
                .enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String resp = response.body().string();
                        if (response.isSuccessful()) {
                            final IView view = getView();
                            if (mHandler != null && view != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.onSuccess(resp, task.getPage()
                                                , task
                                                        .getActionType());
                                    }
                                });
                            }
                        } else {
                            Logger.d(response.message() + " error : body " + resp);
                        }
                    }
                });

    }

    @Override
    protected void get(final Task task) {
        if (isViewDestroyed()) {
            return;
        }
        OkHttpUtils
                .get()
                .url(task.getUrl())
                .tag(getView())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onSuccess(response, task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }
                });
    }

    @Override
    protected void put(final Task task) {
        if (isViewDestroyed()) {
            return;
        }
        File file = task.getFile();
        if (file == null) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                file);
        OkHttpUtils
                .put()
                .requestBody(requestBody)
                .url(task.getUrl())
                .tag(getView())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        final IView view = getView();
                        if (mHandler != null && view != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.onSuccess(response, task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }
                });
    }

    @Override
    public void cancelByTag(IView view) {
        OkHttpUtils.getInstance().cancelTag(view);
    }

    @Override
    protected void executeInsert(Task task) {

    }

    @Override
    protected void executeSelect(Task task) {

    }

    @Override
    protected void executeDelete(Task task) {

    }

    @Override
    protected void executeUpdate(Task task) {

    }

    @Override
    protected void delete(Task task) {

    }

    @Override
    protected void head(Task task) {

    }

    @Override
    protected void patch(Task task) {

    }
}
