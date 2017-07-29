package com.anyihao.ayb.utils;

import android.net.Uri;

import com.orhanobut.logger.Logger;


/**
 * Description：
 * <br/>
 * Created by chiclaim on 2017/5/16.
 */

public class Updater {

    /**
     * DownloadManager.getDownloadStatus如果没找到会返回-1
     */
    private static final int STATUS_UN_FIND = -1;

    private volatile static Updater instance;

    private Updater() {
        //
    }

    public static Updater getInstance() {
        if (instance == null) {
            synchronized (Updater.class) {
                if (null == instance) {
                    instance = new Updater();
                }
            }
        }
        return instance;
    }

    public void download(UpdateConfig updaterConfig) {

        if (!UpdaterUtils.checkDownloadState(updaterConfig.getContext())) {
            UpdaterUtils.showDownloadSetting(updaterConfig.getContext());
            return;
        }

        long downloadId = UpdaterUtils.getLocalDownloadId(updaterConfig.getContext());
        Logger.d("local download id is " + downloadId);
        if (downloadId != -1L) {
            DownloadManager dm = DownloadManager.getInstance();
            //获取下载状态
            int status = dm.getDownloadStatus(updaterConfig.getContext(), downloadId);
            switch (status) {
                //下载成功
                case android.app.DownloadManager.STATUS_SUCCESSFUL:
                    Logger.d("downloadId=" + downloadId + " ,status = STATUS_SUCCESSFUL");
                    Uri uri = dm.getDownloadUri(updaterConfig.getContext(), downloadId);
                    if (uri != null) {
                        //本地的版本大于当前程序的版本直接安装
                        if (UpdaterUtils.compare(updaterConfig.getContext(), uri.getPath())) {
                            Logger.d("start install UI");
                            UpdaterUtils.startInstall(updaterConfig.getContext(), uri);
                            return;
                        } else {
                            //从FileDownloadManager中移除这个任务
                            Logger.d("remove task" + " from downloadManager" + " where " +
                                    "downloadId is " + downloadId);
                            dm.getDM(updaterConfig.getContext()).remove(downloadId);
                        }
                    } else {
                        //重新下载
                        Logger.d("Uri= null,redownload apk file");
                        startDownload(updaterConfig);
                    }

                    break;
                //下载失败
                case android.app.DownloadManager.STATUS_FAILED:
                    Logger.d("download failed " + downloadId);
                    startDownload(updaterConfig);
                    break;
                case android.app.DownloadManager.STATUS_RUNNING:
                    Logger.d("downloadId=" + downloadId + " ,status = STATUS_RUNNING");
                    break;
                case android.app.DownloadManager.STATUS_PENDING:
                    Logger.d("downloadId=" + downloadId + " ,status = STATUS_PENDING");
                    break;
                case android.app.DownloadManager.STATUS_PAUSED:
                    Logger.d("downloadId=" + downloadId + " ,status = STATUS_PAUSED");
                    break;
                case STATUS_UN_FIND:
                    Logger.d("downloadId=" + downloadId + " ,status = STATUS_UN_FIND");
                    startDownload(updaterConfig);
                    break;
                default:
                    Logger.d("downloadId=" + downloadId + " ,status = " + status);
                    break;
            }
        } else {
            startDownload(updaterConfig);
        }
    }

    private void startDownload(UpdateConfig updaterConfig) {
        long id = DownloadManager.getInstance().startDownload(updaterConfig);
        Logger.d("apk download start, downloadId is " + id);
    }
}
