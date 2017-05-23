package com.blanke.xsocket.tcp.client.listener;


import com.blanke.xsocket.tcp.client.XTCPClient;
import com.blanke.xsocket.tcp.client.bean.TCPMsg;

/**
 */
public interface TCPClientListener {

    void onConnectError(XTCPClient client, String msg, Exception e);
    void onConnected(XTCPClient client);
    void onSent(XTCPClient client, TCPMsg TCPMsg);
    void onDisconnected(XTCPClient client, String msg, Exception e);
    void onReceived(XTCPClient client, TCPMsg TCPMsg);
    void onValidationFailed(XTCPClient client, TCPMsg TCPMsg);
}
