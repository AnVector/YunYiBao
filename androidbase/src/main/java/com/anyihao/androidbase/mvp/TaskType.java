package com.anyihao.androidbase.mvp;

/**
 * author: Administrator
 * date: 2017/3/9 000910:55.
 * email:looper@126.com
 */

public interface TaskType {

    interface Method {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PATCH = "PATCH";
    }

    interface Local {
        public static final String INSERT = "INSERT";
        public static final String SELECT = "SELECT";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "CRUD_DELETE";
    }
}
