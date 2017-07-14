package com.anyihao.ayb.common;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ProcessUtils;
import com.anyihao.ayb.BuildConfig;
import com.anyihao.ayb.constant.GlobalConsts;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.library.http.okhttp.OkHttpUtils;
import com.library.http.okhttp.callback.StringCallback;
import com.library.http.okhttp.https.HttpsUtils;
import com.library.http.okhttp.log.LoggerInterceptor;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/10/29 0029.
 * 启用 Dalvik 可执行文件分包输出
 */

public class UApplication extends MultiDexApplication {

    private final String TAG = UApplication.this.getClass().getSimpleName();
    private UBroadcastReceiver mUBroadcastReceiver;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    static {
        PlatformConfig.setQQZone("1105774848", "WmRdHTA5MG4bCO2F");//QQ
        PlatformConfig.setWeixin("wxc6a6d430c1f2c793", "a63a460189b5473828f7a2f2328a1dda");//微信
        PlatformConfig.setSinaWeibo("269247255", "6fb93892a757831710ed71a0c0fd80cf",
                "http://sns.whalecloud.com/sina2/callback");//微博
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initUShare();//初始化友盟社会化分享
        if (isAppProcess()) {
            initUBroadcastReceiver();//在当前进程注册广播
            initOkHttpUtils();//初始化OkHttp配置
            initUAnalysis();//初始化友盟统计
            GreenDaoManager.getInstance().init(this);//初始化GreenDao
        }
        initBugly();//初始化Bugly
        initLeakCanary();//初始化LeakCanary
        initButterKnife();//初始化ButterKnife
        initLogger();//初始化Logger
        initAMapLocation();//初始化高德定位
    }

    private void initAMapLocation() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setInterval(600 * 1000)//设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
                .setNeedAddress(true);//设置是否返回地址信息（默认返回地址信息）
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    if (PreferencesUtils.getBoolean(getApplicationContext(), "isLogin", false)) {
                        sendLocationInfo(aMapLocation.getLatitude(), aMapLocation.getLongitude(),
                                aMapLocation.getCity(), aMapLocation.getCityCode(), aMapLocation
                                        .getProvince(), aMapLocation.getDistrict(), aMapLocation
                                        .getAdCode(), aMapLocation.getStreet(), aMapLocation
                                        .getStreet(), aMapLocation.getAddress());
                    }

                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Logger.d("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    private void sendLocationInfo(double latitude, double longtitude, String city, String
            cityCode, String province, String district, String adcode, String street, String
                                          number, String address) {

        Map<String, String> params = new HashMap<>();
        params.put("uid", PreferencesUtils.getString(this, "uid", ""));
        params.put("cmd", "LOCATION");
        params.put("latitude", latitude + "");
        params.put("longtitude", longtitude + "");
        params.put("city", city);
        params.put("citycode", cityCode);
        params.put("province", province);
        params.put("district", district);
        params.put("adcode", adcode);
        params.put("street", street);
        params.put("number", number);
        params.put("address", address);
        OkHttpUtils
                .post()
                .url(GlobalConsts.PREFIX_URL)
                .addHeader("Content-Type", "text/plain")
                .params(params)
                .tag("")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
                    }
                });

    }

    private void initUShare() {
        Config.DEBUG = true;
        UMShareAPI.get(this);
    }

    private void initOkHttpUtils() {
        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new
                SharedPrefsCookiePersistor(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        Proxy proxy = new Proxy()
//        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore
// (getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG", true))
                .cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initUAnalysis() {
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    private boolean isAppProcess() {
        // 获取当前包名
        String packageName = getApplicationContext().getPackageName();
        // 获取当前进程名
        String processName = ProcessUtils.getProcessNameByPid(android.os.Process.myPid());
        Logger.d(processName);
        return (null != processName && processName.equals(packageName));
    }

    private void initBugly() {
        // 获取当前包名
        String packageName = getApplicationContext().getPackageName();
        // 获取当前进程名
        String processName = ProcessUtils.getProcessNameByPid(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        CrashReport.initCrashReport(getApplicationContext(), strategy);
    }

    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    private void initButterKnife() {
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }

    private void initUBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        mUBroadcastReceiver = new UBroadcastReceiver();
        registerReceiver(mUBroadcastReceiver, filter);
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            Logger.init(TAG)
                    .logLevel(LogLevel.FULL)//Note:Use LogLevel.NONE for the release version.
                    .methodCount(2)
                    .methodOffset(5);
        } else {
            Logger.init(TAG)
                    .logLevel(LogLevel.NONE)//Note:Use LogLevel.NONE for the release version.
                    .methodCount(2)
                    .methodOffset(5);
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mUBroadcastReceiver != null) {
            unregisterReceiver(mUBroadcastReceiver);
        }
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
    }
}
