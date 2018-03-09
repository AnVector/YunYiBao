package com.anyihao.ayb.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.NetworkUtils;
import com.anyihao.androidbase.utils.TextUtils;
import com.anyihao.ayb.ITaskCallback;
import com.anyihao.ayb.IWifiInfoManager;
import com.anyihao.ayb.R;
import com.anyihao.ayb.WifiInfo;
import com.anyihao.ayb.frame.activity.MainActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Administrator
 * @date 2016/11/15 0015
 */

public class PollingService extends Service {

    private final String TAG = this.getClass().getSimpleName();
    private static final int ONGOING_NOTIFICATION_ID = 1001;
    private static final int POWER_NOTIFICATION_ID = 1002;
    private static final int NOTIFICATION_CONNECT = 1003;
    private static final int NOTIFICATION_DISCONNECT = 1004;
    private static final int NOTIFICATION_CLOSE = 1005;
    private NotificationManager mNotificationManager;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private volatile boolean isConnected = false;
    private String deviceName;//设备名称
    private int batteryQuantity;//电池电量
    private String networkRate;//网速
    private int connectCount;//连接人数
    private int sigIntensity;//信号强度
    private String signalType;//信号类型

    private int count = 0;
    private byte failTimes = 0;
    private final RemoteCallbackList<ITaskCallback> mRemoteCallbackList = new
            RemoteCallbackList<>();

    private final IWifiInfoManager.Stub mWifiInfoManager = new IWifiInfoManager.Stub() {
        @Override
        public List<WifiInfo> getWifiInfoList() throws RemoteException {
            synchronized (this) {
                List<WifiInfo> mWifiLst = new ArrayList<>();
                WifiInfo wifiInfo = new WifiInfo();
                wifiInfo.setCode(200);
                wifiInfo.setDesc("success");
                wifiInfo.setMsg("IPC");
                mWifiLst.add(wifiInfo);
                return mWifiLst;
            }
        }

        @Override
        public void registerCallback(ITaskCallback cb) throws RemoteException {
            if (cb != null) {
                boolean isSuccess = mRemoteCallbackList.register(cb);
                if (isSuccess) {
                    Logger.e("注册跨进程广播成功");
                } else {
                    Logger.e("注册跨进程广播失败");
                }
            } else {
                Logger.e("注册跨进程广播失败");
            }
        }

        @Override
        public void unregisterCallback(ITaskCallback cb) throws RemoteException {
            if (cb != null) {
                mRemoteCallbackList.unregister(cb);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e("后台主线程Tid=" + Thread.currentThread()
                .getId());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initUUWifiInfo();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.looper.easylife.permission" +
                ".ACCESS_POLLING_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            Logger.e("连接权限验证失败，拒绝连接");
            return null;
        }
        Logger.e("连接权限验证成功，允许连接");
        return mWifiInfoManager;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTimerTask();
        if (intent != null) {
            dispatchClickEvent(intent.getIntExtra("command", 0));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * try catch
     */
    private void initTimerTask() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
//                Logger.e("后台子线程Tid=" + Thread
//                        .currentThread().getId());
//                try {
//                    String msg = EuUdpClient.getInstance().getWifiInfo();
//                    Logger.e("第" + (++count) + "次发送UDP请求功");
//                    convertUUWifiInfo(msg);
//                } catch (IOException e) {
//                    Logger.e("第" + (++count) + "次发送UDP请求失败");
//                    e.printStackTrace();
//                    setDiscStatus();
//                }
            }
        };
//        if(NetworkUtils.isWifiConnected(this)){
        mTimer.schedule(mTimerTask, 0L, 10000L);
//        }
    }

    private void beginBroadCast(WifiInfo uuWifiBean) {
        final int N = mRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            ITaskCallback cb = mRemoteCallbackList.getBroadcastItem(i);
            if (cb != null) {
                try {
                    cb.onNetworkChanged(uuWifiBean, count, isConnected);
//                    Logger.e("Binder回调线程Tid=" + Thread
//                            .currentThread().getId());
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                    e.printStackTrace();
                }
            }

        }
        mRemoteCallbackList.finishBroadcast();
    }

    private void setDiscStatus() {
        if (isConnected) {
            if (failTimes == 1) {
                initUUWifiInfo();
                return;
            }
            ++failTimes;
        }
    }

    private void dispatchClickEvent(int cmd) {
        switch (cmd) {
            case NOTIFICATION_CLOSE:
//                Logger.e("你点击了关闭按钮");
                break;
            case NOTIFICATION_CONNECT:
//                Logger.e("你点击了连接按钮");
//                    sendForegroundNotification(NOTIFICATION_CONNECT);
                break;
            case NOTIFICATION_DISCONNECT:
//                    sendForegroundNotification(NOTIFICATION_DISCONNECT);
//                Logger.e("你点击了断开按钮");
                break;
            default:
                break;
        }
    }

    private void convertUUWifiInfo(String resultMsg) {
        if (TextUtils.isEmpty(resultMsg)) {
            return;
        }
        isConnected = true;
        failTimes = 0;
        WifiInfo uuWifiBean = GsonUtils.getInstance().transitionToBean(resultMsg, WifiInfo.class);
        Logger.json(resultMsg);
        String devName = uuWifiBean.getData().getDevice();
        int btQuantity = uuWifiBean.getData().getBattery();
        String nwRate = uuWifiBean.getData().getRate();
        int cntCount = uuWifiBean.getData().getWificonnt();
        int sigInt = uuWifiBean.getData().getSigint();
        String sigType = uuWifiBean.getData().getSigtype();
        if (!isKeepUnchanged(devName, btQuantity, nwRate, cntCount, sigInt, sigType)) {
            deviceName = devName;
            batteryQuantity = btQuantity;
            networkRate = nwRate;
            connectCount = cntCount;
            sigIntensity = sigInt;
            signalType = sigType;
            if (batteryQuantity <= 15) {
                lowPowerNotification();
            }
            sendForegroundNotification();
            beginBroadCast(uuWifiBean);
//            Logger.e("安逸宝WIFI状态已改变,电量：" + batteryQuantity + "%;网速：" + networkRate + ";" +
//                    "连接数：" + connectCount);
        }
    }

    private void initUUWifiInfo() {
        isConnected = false;
        failTimes = 0;
        deviceName = "";
        batteryQuantity = 0;
        networkRate = "0kb/s";
        connectCount = 0;
        sigIntensity = 0;
        signalType = "";
        sendForegroundNotification();
        beginBroadCast(null);
//        Logger.e("安逸宝WIFI已断开;isConnected=" + isConnected);
    }

    private boolean isKeepUnchanged(String deName, int btQuantity, String nwRate, int cntCount,
                                    int sigInt, String sigType) {
        return (deName.equals(deviceName) && batteryQuantity == btQuantity
                && nwRate.equals(networkRate) && connectCount == cntCount
                && sigInt == sigIntensity && sigType.equals(signalType));
    }


    private void lowPowerNotification() {
        String contentTitle = "安逸宝wifi";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle演示示例");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app
                .NotificationCompat.InboxStyle();
        style.setBigContentTitle(contentTitle)
                .addLine("设备电量已低于15%，请及时充电")
                .setSummaryText("SummaryText");
        builder.setStyle(style);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification mNotification = builder.build();
        mNotificationManager.notify(POWER_NOTIFICATION_ID, mNotification);
    }

    private void sendForegroundNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Notification");
        builder.setContentText("自定义通知栏示例");
        builder.setWhen(System.currentTimeMillis());//立即显示
        builder.setSmallIcon(R.drawable.ic_launcher);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.push));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setShowWhen(false);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout
                .notification_wifi_state);
        if (isConnected) {
            mRemoteViews.setTextViewText(R.id.con_state, NetworkUtils.getConnectWifiSsid
                    (getApplicationContext()));
        } else {
            mRemoteViews.setTextViewText(R.id.con_state, "安逸宝Wifi未连接");
        }
        mRemoteViews.setTextViewText(R.id.bat_power, String.format(getString(R.string.bat_power),
                batteryQuantity) + "%");
        mRemoteViews.setTextViewText(R.id.net_speed, String.format(getString(R.string.net_speed),
                networkRate));
        mRemoteViews.setTextViewText(R.id.con_count, String.format(getString(R.string.con_count),
                connectCount));

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);

        Intent closeIntent = new Intent(this, PollingService.class);
        closeIntent.putExtra("command", NOTIFICATION_CLOSE);
        PendingIntent pIntent2 = PendingIntent.getService(this, 7, closeIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_close, pIntent2);
        builder.setContent(mRemoteViews);
        Notification notification = builder.build();
        // 注：当使用的通知ID一致时，只会更新当前Notification
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

//    private void updateForegroundNotification(int cmd){
//        if(cmd == NOTIFICATION_CONNECT){
//            mRemoteViews.setImageViewResource(R.id.notification_switch,R.drawable.icon_connect);
//            cmd = NOTIFICATION_DISCONNECT;
//        }else{
//            mRemoteViews.setImageViewResource(R.id.notification_switch,R.drawable
// .icon_disconnect);
//            cmd = NOTIFICATION_CONNECT;
//        }
//        Intent connectIntent = new Intent(this,PollingService.class);
//        connectIntent.putExtra("command",cmd);
//        PendingIntent pIntent1 =  PendingIntent.getService(this,6,connectIntent,0);
//        mRemoteViews.setOnClickPendingIntent(R.id.notification_switch,pIntent1);
//    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        //Disable this callback list.
        // All registered callbacks are unregistered, and the list is disabled so that future
        // calls to register(E) will fail.
        // This should be used when a Service is stopping, to prevent clients from registering
        // callbacks after it is stopped.
        mRemoteCallbackList.kill();
        super.onDestroy();
    }
}
