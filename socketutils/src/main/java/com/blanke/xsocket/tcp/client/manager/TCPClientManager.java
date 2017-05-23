package com.blanke.xsocket.tcp.client.manager;

import com.blanke.xsocket.tcp.client.XTCPClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * tcpclient的管理者
 */
public class TCPClientManager {
    private static Set<XTCPClient> sMXTcpClients = new HashSet<>();

    public static void putTCPClient(XTCPClient XTCPClient) {
        sMXTcpClients.add(XTCPClient);
    }

    public static XTCPClient getTCPClient(TargetInfo targetInfo) {
        for (XTCPClient tc : sMXTcpClients) {
            if (tc.getTargetInfo().equals(targetInfo)) {
                return tc;
            }
        }
        return null;
    }
}
