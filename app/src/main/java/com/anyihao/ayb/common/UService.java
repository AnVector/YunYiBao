package com.anyihao.ayb.common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.blanke.xsocket.tcp.client.TCPConnConfig;
import com.blanke.xsocket.tcp.client.XTCPClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TCPMsg;
import com.blanke.xsocket.tcp.client.listener.TCPClientListener;
import com.orhanobut.logger.Logger;

public class UService extends Service implements TCPClientListener {

    private XTCPClient mXTCPClient;

    @Override
    public void onCreate() {
        super.onCreate();
        connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void connect() {
        if (mXTCPClient != null && mXTCPClient.isConnected()) {
            mXTCPClient.disconnect();
        } else {
                TargetInfo targetInfo = new TargetInfo("192.168.1.102", 8888);
                mXTCPClient = XTCPClient.getTCPClient(targetInfo);
                mXTCPClient.addTCPClientListener(this);
                mXTCPClient.config(new TCPConnConfig.Builder()
                        .setIsReconnect(true)
                        .create());
                if (mXTCPClient.isDisconnected()) {
                    mXTCPClient.connect();
                } else {
                    Logger.d("已经存在该连接");
                }
            }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnectError(XTCPClient client, String msg, Exception e) {

    }

    @Override
    public void onConnected(XTCPClient client) {

    }

    @Override
    public void onSent(XTCPClient client, TCPMsg TCPMsg) {

    }

    @Override
    public void onDisconnected(XTCPClient client, String msg, Exception e) {

    }

    @Override
    public void onReceived(XTCPClient client, TCPMsg TCPMsg) {

    }

    @Override
    public void onValidationFailed(XTCPClient client, TCPMsg TCPMsg) {

    }
}
