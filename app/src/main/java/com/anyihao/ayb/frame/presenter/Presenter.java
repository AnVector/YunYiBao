package com.anyihao.ayb.frame.presenter;

import com.anyihao.androidbase.utils.LogUtils;
import com.library.http.okhttp.OkHttpUtils;
import com.library.http.okhttp.callback.StringCallback;
import com.anyihao.androidbase.mvp.IView;
import com.anyihao.androidbase.mvp.PresenterCompat;
import com.anyihao.androidbase.mvp.Task;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
        if (isViewDestroyed())
            return;
        OkHttpUtils
                .postString()
                .url(task.getUrl())
                .content(task.getContent())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .tag(getView())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        LogUtils.d(e.toString());
                        if (mHandler != null && getView() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        if (mHandler != null && getView() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().onSuccess(response, task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }
                });
    }

    @Override
    protected void get(final Task task) {
        if (isViewDestroyed())
            return;
        OkHttpUtils
                .get()
                .url(task.getUrl())
                .tag(getView())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {

                        if (mHandler != null && getView() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        if (mHandler != null && getView() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().onSuccess(response, task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }
                });
    }

    @Override
    protected void put(final Task task) {
        if (isViewDestroyed())
            return;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                task.getFile());
        OkHttpUtils
                .put()
                .requestBody(requestBody)
                .url(task.getUrl())
                .tag(getView())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        if (mHandler != null && getView() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().onFailure(e.toString(), task.getPage(), task
                                            .getActionType());
                                }
                            });
                        }

                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        if (mHandler != null && getView() != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getView().onSuccess(response, task.getPage(), task
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
