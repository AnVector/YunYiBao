package com.anyihao.ayb.utils;

import android.content.Context;

/**
 * Description：下载器参数配置
 * <br/>
 * Created by kumu on 2017/5/17.
 */

public class UpdateConfig {

    private String mTitle; //title
    private String mDescription;//description
    private String mDownloadPath;//download path
    private String mFileUrl;// file url
    private String mFilename;// file name
    private boolean mIsShowDownloadUI = true;
    private int mNotificationVisibility = 0;
    private boolean mCanMediaScanner;
    private boolean mAllowedOverRoaming;
    private int mAllowedNetworkTypes = ~0;// default to all network types allowed
    private Context mContext;


    private UpdateConfig(Context context) {
        this.mContext = context;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getDownloadPath() {
        return mDownloadPath;
    }

    public void setDownloadPath(String mDownloadPath) {
        this.mDownloadPath = mDownloadPath;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.mFileUrl = fileUrl;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public boolean isShowDownloadUI() {
        return mIsShowDownloadUI;
    }

    public void setIsShowDownloadUI(boolean mIsShowDownloadUI) {
        this.mIsShowDownloadUI = mIsShowDownloadUI;
    }

    public int getNotificationVisibility() {
        return mNotificationVisibility;
    }

    public void setNotificationVisibility(int mNotificationVisibility) {
        this.mNotificationVisibility = mNotificationVisibility;
    }

    public boolean isCanMediaScanner() {
        return mCanMediaScanner;
    }

    public void setCanMediaScanner(boolean mCanMediaScanner) {
        this.mCanMediaScanner = mCanMediaScanner;
    }

    public boolean isAllowedOverRoaming() {
        return mAllowedOverRoaming;
    }

    public void setAllowedOverRoaming(boolean mAllowedOverRoaming) {
        this.mAllowedOverRoaming = mAllowedOverRoaming;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public int getAllowedNetworkTypes() {
        return mAllowedNetworkTypes;
    }

    public static class Builder {

        UpdateConfig updateConfig;

        public Builder(Context context) {
            updateConfig = new UpdateConfig(context.getApplicationContext());
        }

        public Builder setTitle(String title) {
            updateConfig.setTitle(title);
            return this;
        }

        public Builder setDescription(String description) {
            updateConfig.setDescription(description);
            return this;
        }

        /**
         * 文件下载路径
         *
         * @param downloadPath
         * @return
         */
        public Builder setDownloadPath(String downloadPath) {
            updateConfig.setDownloadPath(downloadPath);
            return this;
        }

        /**
         * 下载的文件名
         *
         * @param filename
         * @return
         */
        public Builder setFilename(String filename) {
            updateConfig.setFilename(filename);
            return this;
        }

        /**
         * 文件网络地址
         *
         * @param url
         * @return
         */
        public Builder setFileUrl(String url) {
            updateConfig.setFileUrl(url);
            return this;
        }

        public Builder setIsShowDownloadUI(boolean isShowDownloadUI) {
            updateConfig.setIsShowDownloadUI(isShowDownloadUI);
            return this;
        }

        public Builder setNotificationVisibility(int notificationVisibility) {
            updateConfig.mNotificationVisibility = notificationVisibility;
            return this;
        }

        /**
         * 能否被 MediaScanner 扫描
         *
         * @param canMediaScanner
         * @return
         */
        public Builder setCanMediaScanner(boolean canMediaScanner) {
            updateConfig.mCanMediaScanner = canMediaScanner;
            return this;
        }

        /**
         * 移动网络是否允许下载
         *
         * @param allowedOverRoaming
         * @return
         */
        public Builder setAllowedOverRoaming(boolean allowedOverRoaming) {
            updateConfig.mAllowedOverRoaming = allowedOverRoaming;
            return this;
        }

        public Builder setContext(Context context) {
            updateConfig.mContext = context;
            return this;

        }

        /**
         * By default, all network types are allowed
         *
         * @param allowedNetworkTypes
         * @see AllowedNetworkType#NETWORK_MOBILE
         * @see AllowedNetworkType#NETWORK_WIFI
         */
        public Builder setAllowedNetworkTypes(int allowedNetworkTypes) {
            updateConfig.mAllowedNetworkTypes = allowedNetworkTypes;
            return this;
        }


        public UpdateConfig build() {
            return updateConfig;
        }


    }

    public interface AllowedNetworkType {
        /**
         * Bit flag for {@link android.app.DownloadManager.Request#NETWORK_MOBILE}
         */
        int NETWORK_MOBILE = 1 << 0;

        /**
         * Bit flag for {@link android.app.DownloadManager.Request#NETWORK_WIFI}
         */
        int NETWORK_WIFI = 1 << 1;
    }
}
