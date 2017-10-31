package com.anyihao.ayb.constant;

import com.anyihao.ayb.BuildConfig;

/**
 * @author Administrator
 *         date: 2017/2/16 001613:54.
 *         email:looper@126.com
 */

public class GlobalConsts {

    /**
     * 上卡超时时间 单位：秒
     */
    public static final int CONNECT_TIME_OUT_MINUTES = 60;
    /**
     * 轮询时间周期 单位：秒
     */
    public static final int RETRY_CYCLE_TIME_MINUTES = 5;
    /**
     * 微信开发平台APPID
     */
    public static final String WX_APP_ID = "wxc6a6d430c1f2c793";
    /**
     * API address
     */
    /**
     * public static final String PREFIX_URL = "http://192.168.0.118:8888/vrsws/vappws.json";
     * public static final String PREFIX_URL = "http://122.224.91.149:8888/vrsws/vappws.json";
     * <p>
     * public static final String PREFIX_URL = "http://180.153.59.29:8888/vrsws/vappws.json";
     * public static final String PREFIX_URL = "http://122.224.91.148:8888/vrsws/vappws.json";
     */
    public static final String PREFIX_URL = BuildConfig.API_URL + "/vrsws/vappws.json";
    public static final String FILE_PREFIX_URL = BuildConfig.API_URL + "/vrsws/upload/avatar";
}
