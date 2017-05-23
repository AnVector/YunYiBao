package com.blanke.xsocket.tcp.server.listener;

import com.blanke.xsocket.tcp.client.XTCPClient;
import com.blanke.xsocket.tcp.client.bean.TCPMsg;
import com.blanke.xsocket.tcp.server.XTCPServer;

/**
 * tcpserver
 */
public interface TcpServerListener {
    void onCreated(XTCPServer server);

    void onListened(XTCPServer server);

    void onAccept(XTCPServer server, XTCPClient tcpClient);

    void onSended(XTCPServer server, XTCPClient tcpClient, TCPMsg TCPMsg);

    void onReceive(XTCPServer server, XTCPClient tcpClient, TCPMsg TCPMsg);

    void onValidationFail(XTCPServer server, XTCPClient client, TCPMsg TCPMsg);

    void onClientClosed(XTCPServer server, XTCPClient tcpClient, String msg, Exception e);

    void onServerClosed(XTCPServer server, String msg, Exception e);
}
