package com.anyihao.ayb.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * Singleton
 * Created by chiclaim on 2016/05/18
 */
public class DownloadManager {

    private volatile static DownloadManager instance;

    private android.app.DownloadManager mDownloadManager;


    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public android.app.DownloadManager getDM(Context context) {
        if (mDownloadManager == null) {
            mDownloadManager = (android.app.DownloadManager) context
                    .getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return mDownloadManager;
    }


    public long startDownload(UpdateConfig updaterConfig) {
        android.app.DownloadManager.Request req = new android.app.DownloadManager.Request(Uri
                .parse(updaterConfig.getFileUrl()));
        req.setAllowedNetworkTypes(updaterConfig.getAllowedNetworkTypes());
        //req.setAllowedOverMetered()
        //移动网络是否允许下载
        req.setAllowedOverRoaming(updaterConfig.isAllowedOverRoaming());

        if (updaterConfig.isCanMediaScanner()) {
            //能够被MediaScanner扫描
            req.allowScanningByMediaScanner();
        }

        //是否显示状态栏下载UI
        req.setNotificationVisibility(updaterConfig.getNotificationVisibility());
        //点击正在下载的Notification进入下载详情界面，如果设为true则可以看到下载任务的进度，如果设为false，则看不到我们下载的任务
        req.setVisibleInDownloadsUi(updaterConfig.isShowDownloadUI());

        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
        req.setDestinationInExternalFilesDir(updaterConfig.getContext(), Environment
                .DIRECTORY_DOWNLOADS, updaterConfig.getFilename());
        //第二种
        //file:///storage/emulated/0/Download/update.apk
        //req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
        //第三种 自定义文件路径
        //req.setDestinationUri()


        // 设置一些基本显示信息
        req.setTitle(updaterConfig.getTitle());
        req.setDescription(updaterConfig.getDescription());


        //req.setMimeType("application/vnd.android.package-archive");

        long id = getDM(updaterConfig.getContext()).enqueue(req);
        //把DownloadId保存到本地
        UpdaterUtils.saveDownloadId(updaterConfig.getContext(), id);
        return id;
        //long downloadId = mDownloadManager.enqueue(req);
        //Log.d("DownloadManager", downloadId + "");
        //mDownloadManager.openDownloadedFile()
    }


    /**
     * 获取文件保存的路径
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see DownloadManager#getDownloadUri(Context, long)
     */
    private String getDownloadPath(Context context, long downloadId) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor c = getDM(context).query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(android.app.DownloadManager
                            .COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }


    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see DownloadManager#getDownloadPath(Context, long)
     */
    public Uri getDownloadUri(Context context, long downloadId) {
        return getDM(context).getUriForDownloadedFile(downloadId);
    }

    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see android.app.DownloadManager#STATUS_PENDING
     * @see android.app.DownloadManager#STATUS_PAUSED
     * @see android.app.DownloadManager#STATUS_RUNNING
     * @see android.app.DownloadManager#STATUS_SUCCESSFUL
     * @see android.app.DownloadManager#STATUS_FAILED
     */
    public int getDownloadStatus(Context context, long downloadId) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor c = getDM(context).query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(android.app.DownloadManager
                            .COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }
}
