// IWifiInfoManager.aidl
package com.anyihao.ayb;

// Declare any non-default types here with import statements

import com.anyihao.ayb.WifiInfo;
import com.anyihao.ayb.ITaskCallback;

interface IWifiInfoManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     List<WifiInfo> getWifiInfoList();
     void registerCallback(ITaskCallback cb);
     void unregisterCallback(ITaskCallback cb);
}