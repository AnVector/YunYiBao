package com.library.http.okhttp.utils;

import android.util.Log;

/**
 * Created by zhy on 15/11/6.
 */
public class L {
    private static boolean debug = true;

    public static void d(String msg) {
        if (debug) {
            Log.d("OkHttp", msg);
        }
    }

}

