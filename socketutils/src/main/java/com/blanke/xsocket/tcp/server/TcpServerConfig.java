package com.blanke.xsocket.tcp.server;

import com.blanke.xsocket.tcp.client.TCPConnConfig;

/**
 * server配置
 */
public class TcpServerConfig {
    private int maxClientSize = Integer.MAX_VALUE;// TODO
    private TCPConnConfig mTCPConnConfig;

    private TcpServerConfig() {
        mTCPConnConfig = new TCPConnConfig.Builder().create();
    }

    public int getMaxClientSize() {
        return maxClientSize;
    }

    public TCPConnConfig getTcpConnConfig() {
        return mTCPConnConfig;
    }

    public static class Builder {
        private TcpServerConfig tcpServerConfig;

        public Builder() {
            tcpServerConfig = new TcpServerConfig();
        }

        public TcpServerConfig create() {
            return tcpServerConfig;
        }

        public Builder setMaxClientSize(int maxSize) {
            tcpServerConfig.maxClientSize = maxSize;
            return this;
        }

        public Builder setTcpConnConfig(TCPConnConfig TCPConnConfig) {
            tcpServerConfig.mTCPConnConfig = TCPConnConfig;
            return this;
        }
    }
}
