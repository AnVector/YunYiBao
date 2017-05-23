package com.blanke.xsocket.tcp.server.manager;

import com.blanke.xsocket.tcp.server.XTCPServer;

import java.util.HashSet;
import java.util.Set;

/**
 * tcpserver
 */
public class TcpServerManager {
    private static Set<XTCPServer> sMXTcpServers = new HashSet<>();

    public static void putTcpServer(XTCPServer XTcpServer) {
        sMXTcpServers.add(XTcpServer);
    }

    public static XTCPServer getTcpServer(int port) {
        for (XTCPServer ts : sMXTcpServers) {
            if (ts.getPort() == port) {
                return ts;
            }
        }
        return null;
    }
}
