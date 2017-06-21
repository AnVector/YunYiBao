package com.anyihao.androidbase.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anyihao.androidbase.utils.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * author: Administrator
 * date: 2017/3/13 001313:48.
 * email:looper@126.com
 */

public class Task implements Serializable {

    @NonNull
    private String taskType;//任务类型：网络请求或本地数据库操作
    private String url;//网络请求用到的url
    private String content;//post请求用到的参数
    @Nullable
    private Map<String, String> params;//表单参数提交
    private int page;//分页请求，用于回调判断
    @NonNull
    private Object actionType;//操作类型，用于区分网络请求的哪个接口或本地操作的哪次操作

    private File file;//put请求的文件参数

    private Task(@NonNull String taskType, String url, String content, int page, File file,
                 @NonNull Object actionType) {
        this.taskType = taskType;
        this.url = url;
        this.content = content;
        this.page = page;
        this.file = file;
        this.actionType = actionType;
    }

    private Task(@NonNull String taskType, String url, String content, int page, File file,
                 @NonNull Object actionType, @Nullable Map<String, String> params) {
        this.taskType = taskType;
        this.url = url;
        this.content = content;
        this.page = page;
        this.file = file;
        this.actionType = actionType;
        this.params = params;
    }

    private Task(@NonNull String taskType, String url, int page, @NonNull Object actionType) {
        if (StringUtils.isEmpty(url)) {
            throw new UnsupportedOperationException("u must transmit a valid url");
        }
        this.taskType = taskType;
        this.url = url;
        this.page = page;
        this.actionType = actionType;
    }

    private Task(@NonNull String taskType, String url, String content, int page, @NonNull Object
            actionType) {
        if (StringUtils.isEmpty(url)) {
            throw new UnsupportedOperationException("u must transmit a valid url");
        }
        this.taskType = taskType;
        this.url = url;
        this.content = content;
        this.page = page;
        this.actionType = actionType;
    }

    private Task(@NonNull String taskType, String url, String content, int page, @NonNull Object
            actionType, @Nullable Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            throw new UnsupportedOperationException("u must transmit a valid url");
        }
        this.taskType = taskType;
        this.url = url;
        this.content = content;
        this.page = page;
        this.actionType = actionType;
        this.params = params;
    }

    private Task(@NonNull String taskType, String url, File file, int page, @NonNull Object
            actionType) {
        if (StringUtils.isEmpty(url) || file == null) {
            throw new UnsupportedOperationException("u must transmit a valid url and a valid file");
        }
        this.taskType = taskType;
        this.url = url;
        this.file = file;
        this.page = page;
        this.actionType = actionType;
    }

    @NonNull
    public String getTaskType() {
        return taskType;
    }

    public String getUrl() {
        return url;
    }

    public int getPage() {
        return page;
    }

    public String getContent() {
        return content;
    }

    public File getFile() {
        return file;
    }

    @NonNull
    public Object getActionType() {
        return actionType;
    }

    @Nullable
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "taskType=" + taskType + ";url=" + url + ";content=" + content + ";page=" + page +
                ";actionType=" + actionType.toString() + ";file=" + actionType.toString();
    }

    public static class TaskBuilder {
        @NonNull
        private String taskType;
        @Nullable
        private String url = null;
        @Nullable
        private String content = null;
        private int page;
        @Nullable
        private File file = null;
        @NonNull
        private Object actionType;
        @Nullable
        private Map<String, String> params;

        public TaskBuilder setUrl(@Nullable String url) {
            this.url = url;
            return this;
        }

        public TaskBuilder setContent(@Nullable String content) {
            this.content = content;
            return this;
        }

        public TaskBuilder setFile(@Nullable File file) {
            this.file = file;
            return this;
        }

        public TaskBuilder setTaskType(@NonNull String taskType) {
            this.taskType = taskType;
            return this;
        }

        public TaskBuilder setPage(int page) {
            this.page = page;
            return this;
        }

        public TaskBuilder setActionType(@NonNull Object actionType) {
            this.actionType = actionType;
            return this;
        }

        public TaskBuilder setParams(@Nullable Map<String, String> params) {
            this.params = params;
            return this;
        }


        public Task createTask() {
            switch (taskType) {
                case TaskType.Method.GET:
                    return new Task(taskType, url, page, actionType);
                case TaskType.Method.POST:
                    return new Task(taskType, url, content, page, actionType, params);
                case TaskType.Method.HEAD:
                    break;
                case TaskType.Method.DELETE:
                    break;
                case TaskType.Method.PATCH:
                    break;
                case TaskType.Method.PUT:
                    return new Task(taskType, url, file, page, actionType);
                default:
                    break;
            }
            return new Task(taskType, url, content, page, file, actionType);
        }
    }
}
