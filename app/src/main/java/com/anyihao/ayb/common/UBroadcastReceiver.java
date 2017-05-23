package com.anyihao.ayb.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.anyihao.androidbase.utils.ProcessUtils;

import java.io.File;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/5 0005.
 */

public class UBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                // long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                installApk(context);
                break;
            case DownloadManager.ACTION_NOTIFICATION_CLICKED:
                //Toast.makeText(context, "你点击了下载通知栏！！！", Toast.LENGTH_SHORT).show();
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                onNetworkChanged(context);
                break;
            default:
                break;

        }
    }

    private void installApk(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory() + "/Android/data/" +
                        context.getPackageName() + "/files/" + Environment
                        .DIRECTORY_DOWNLOADS + "/jinritoutiao_448.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * @param context 上下文
     *                开启无线网络：广播发送次数 1次
     *                关闭无线网络：广播发送次数 2次
     *                切换无线网络：广播发送次数 3次
     */
    private void onNetworkChanged(Context context) {
        Log.e("uuu","收到网络状态改变的系统广播");
        boolean isBgProcRunning = false;
        Set<String> runningProc = ProcessUtils.getAllBackgroundProcesses(context);
        if(!runningProc.isEmpty()){
            for (String value : runningProc) {
                if(value.equals("com.googleplay.easylife:remote")){
                    isBgProcRunning = true;
                    break;
                }
            }
        }
        if(!isBgProcRunning){
//            Intent intent = new Intent(context,PollingService.class);
//            context.startService(intent);
//            LogUtils.e("进程未在后台运行");
        }else {
//            LogUtils.e("进程后台运行中");
        }

//        if (NetworkUtils.getNetworkType(context) == NetworkUtils.NETWORK_NO) {
//            Toast.makeText(context, "networkType = " + NetworkUtils.getNetworkType
//                    (context), Toast.LENGTH_SHORT).show();
//        } else {
//
//        }
    }
}
