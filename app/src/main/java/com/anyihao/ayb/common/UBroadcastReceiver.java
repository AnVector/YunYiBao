package com.anyihao.ayb.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.anyihao.androidbase.utils.PreferencesUtils;

import java.io.File;

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
                PreferencesUtils.putBoolean(UApplication.getInstance(), "isDownloading",
                        true);
                installApk(context);
                break;
            case DownloadManager.ACTION_NOTIFICATION_CLICKED:
                //Toast.makeText(context, "你点击了下载通知栏！！！", Toast.LENGTH_SHORT).show();
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
                        .DIRECTORY_DOWNLOADS + "/yyb.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
