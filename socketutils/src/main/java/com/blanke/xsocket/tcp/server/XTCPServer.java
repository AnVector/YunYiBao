package com.blanke.xsocket.tcp.server;

import com.blanke.xsocket.BaseXSocket;
import com.blanke.xsocket.tcp.client.XTCPClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TCPMsg;
import com.blanke.xsocket.tcp.client.listener.TCPClientListener;
import com.blanke.xsocket.tcp.server.listener.TcpServerListener;
import com.blanke.xsocket.tcp.server.manager.TcpServerManager;
import com.blanke.xsocket.tcp.server.state.ServerState;
import com.blanke.xsocket.utils.XSocketLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * tcp服务端
 */
public class XTCPServer extends BaseXSocket implements TCPClientListener {
    private static final String TAG = "XTcpServer";
    protected int port;
    protected ServerState mServerState;
    protected ServerSocket mServerSocket;
    protected Map<TargetInfo, XTCPClient> mXTcpClients;
    protected ListenThread mListenThread;
    protected TcpServerConfig mTcpServerConfig;
    protected List<TcpServerListener> mTcpServerListeners;

    private XTCPServer() {
        super();
    }

    public static XTCPServer getTcpServer(int port) {
        XTCPServer xTcpServer = TcpServerManager.getTcpServer(port);
        if (xTcpServer == null) {
            xTcpServer = new XTCPServer();
            xTcpServer.init(port);
            TcpServerManager.putTcpServer(xTcpServer);
        }
        return xTcpServer;
    }

    private void init(int port) {
        this.port = port;
        setServerState(ServerState.Closed);
        mXTcpClients = new LinkedHashMap<>();
        mTcpServerListeners = new ArrayList<>();
        if (mTcpServerConfig == null) {
            mTcpServerConfig = new TcpServerConfig.Builder().create();
        }
    }

    //开启tcpserver
    public void startServer() {
        if (!getListenThread().isAlive()) {
            XSocketLog.d(TAG, "tcp server启动ing ");
            getListenThread().start();
        }
    }

    public void stopServer() {
        stopServer("手动关闭tcpServer", null);
    }

    protected void stopServer(String msg, Exception e) {
        getListenThread().interrupt();//关闭listen
        setServerState(ServerState.Closed);
        if (closeSocket()) {
            for (XTCPClient client : mXTcpClients.values()) {
                if (client != null) {
                    client.disconnect();
                }
            }
            notifyTcpServerClosed(msg, e);
        }
        XSocketLog.d(TAG, "tcp server closed");
    }

    private boolean closeSocket() {
        if (mServerSocket != null && !mServerSocket.isClosed()) {
            try {
                mServerSocket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean sendMsgToAll(TCPMsg msg) {
        boolean re = true;
        for (XTCPClient client : mXTcpClients.values()) {
            if (client.sendMsg(msg) == null) {
                re = false;
            }
        }
        return re;
    }

    public boolean sendMsgToAll(String msg) {
        boolean re = true;
        for (XTCPClient client : mXTcpClients.values()) {
            if (client.sendMsg(msg) == null) {
                re = false;
            }
        }
        return re;
    }

    public boolean sendMsgToAll(byte[] msg) {
        boolean re = true;
        for (XTCPClient client : mXTcpClients.values()) {
            if (client.sendMsg(msg) == null) {
                re = false;
            }
        }
        return re;
    }

    public boolean sendMsg(TCPMsg msg, XTCPClient client) {
        return client.sendMsg(msg) != null;
    }

    public boolean sendMsg(String msg, XTCPClient client) {
        return client.sendMsg(msg) != null;
    }

    public boolean sendMsg(byte[] msg, XTCPClient client) {
        return client.sendMsg(msg) != null;
    }

    public boolean sendMsg(TCPMsg msg, String ip) {
        XTCPClient client = mXTcpClients.get(ip);
        if (client != null) {
            return client.sendMsg(msg) != null;
        }
        return false;
    }

    public boolean sendMsg(String msg, String ip) {
        XTCPClient client = mXTcpClients.get(ip);
        if (client != null) {
            return client.sendMsg(msg) != null;
        }
        return false;
    }

    public boolean sendMsg(byte[] msg, String ip) {
        XTCPClient client = mXTcpClients.get(ip);
        if (client != null) {
            return client.sendMsg(msg) != null;
        }
        return false;
    }

    @Override
    public void onConnectError(XTCPClient client, String msg, Exception e) {

    }

    @Override
    public void onConnected(XTCPClient client) {
        //no callback,ignore
    }

    @Override
    public void onSent(XTCPClient client, TCPMsg TCPMsg) {
        notifyTcpServerSended(client, TCPMsg);
    }

    @Override
    public void onDisconnected(XTCPClient client, String msg, Exception e) {
        notifyTcpClientClosed(client, msg, e);
    }

    @Override
    public void onReceived(XTCPClient client, TCPMsg TCPMsg) {
        notifyTcpServerReceive(client, TCPMsg);
    }

    @Override
    public void onValidationFailed(XTCPClient client, TCPMsg TCPMsg) {
        notifyTcpServerValidationFail(client, TCPMsg);
    }

    class ListenThread extends Thread {
        @Override
        public void run() {
            Socket socket;
            while (!Thread.interrupted()) {
                try {
                    XSocketLog.d(TAG, "tcp server listening");
                    socket = getServerSocket().accept();
                    TargetInfo targetInfo = new TargetInfo(socket.getInetAddress().getHostAddress(), socket.getPort());
                    XTCPClient mXTCPClient = XTCPClient.getTCPClient(socket, targetInfo,
                            mTcpServerConfig.getTcpConnConfig());//创建一个client，接受和发送消息
                    notifyTcpServerAccept(mXTCPClient);
                    mXTCPClient.addTCPClientListener(XTCPServer.this);
                    mXTcpClients.put(targetInfo, mXTCPClient);
                } catch (IOException e) {
                    XSocketLog.d(TAG, "tcp server listening error:" + e);
//                    e.printStackTrace();
                    stopServer("监听失败", e);
//                    return;
                }
            }
        }
    }

    protected ListenThread getListenThread() {
        if (mListenThread == null || !mListenThread.isAlive()) {
            mListenThread = new ListenThread();
        }
        return mListenThread;
    }

    protected ServerSocket getServerSocket() {
        if (mServerSocket == null || mServerSocket.isClosed()) {
            try {
                mServerSocket = new ServerSocket(port);
                setServerState(ServerState.Created);
                notifyTcpServerCreate();
                setServerState(ServerState.Listening);
                notifyTcpServerLinten();
            } catch (IOException e) {
//                e.printStackTrace();
                stopServer("创建失败", e);
            }
        }
        return mServerSocket;
    }

    public void addTcpServerListener(TcpServerListener listener) {
        if (mTcpServerListeners.contains(listener)) {
            return;
        }
        this.mTcpServerListeners.add(listener);
    }

    public void removeTcpServerListener(TcpServerListener listener) {
        this.mTcpServerListeners.remove(listener);
    }

    private void notifyTcpServerCreate() {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onCreated(XTCPServer.this);
                    }
                });
            }
        }
    }

    private void notifyTcpServerLinten() {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onListened(XTCPServer.this);
                    }
                });
            }
        }
    }

    private void notifyTcpServerAccept(final XTCPClient client) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onAccept(XTCPServer.this, client);
                    }
                });
            }
        }
    }

    private void notifyTcpServerReceive(final XTCPClient client, final TCPMsg TCPMsg) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onReceive(XTCPServer.this, client, TCPMsg);
                    }
                });
            }
        }
    }

    private void notifyTcpServerSended(final XTCPClient client, final TCPMsg TCPMsg) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onSended(XTCPServer.this, client, TCPMsg);
                    }
                });
            }
        }
    }

    private void notifyTcpServerValidationFail(final XTCPClient client, final TCPMsg TCPMsg) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onValidationFail(XTCPServer.this, client, TCPMsg);
                    }
                });
            }
        }
    }

    private void notifyTcpClientClosed(final XTCPClient client, final String msg, final Exception e) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onClientClosed(XTCPServer.this, client, msg, e);
                    }
                });
            }
        }
    }

    private void notifyTcpServerClosed(final String msg, final Exception e) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onServerClosed(XTCPServer.this, msg, e);
                    }
                });
            }
        }
    }


    public int getPort() {
        return port;
    }

    private void setServerState(ServerState state) {
        this.mServerState = state;
    }

    public boolean isClosed() {
        return mServerState == ServerState.Closed;
    }

    public boolean isListening() {
        return mServerState == ServerState.Listening;
    }

    public void config(TcpServerConfig tcpServerConfig) {
        mTcpServerConfig = tcpServerConfig;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Xtcpserver port=" + port + ",state=" + mServerState);
        sb.append(" client size=" + mXTcpClients.size());
        return sb.toString();
    }
}
