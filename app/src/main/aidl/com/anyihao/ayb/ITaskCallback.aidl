// ITaskCallback.aidl
package com.anyihao.ayb;

// Declare any non-default types here with import statements
import com.anyihao.ayb.WifiInfo;

interface ITaskCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNetworkChanged(in WifiInfo wifiInfo,int count,boolean isConnected);
}
