package com.blanke.xsocket.tcp.client;

import com.blanke.xsocket.BaseXSocket;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TCPMsg;
import com.blanke.xsocket.tcp.client.bean.TCPMsg.MsgType;
import com.blanke.xsocket.tcp.client.listener.TCPClientListener;
import com.blanke.xsocket.tcp.client.manager.TCPClientManager;
import com.blanke.xsocket.tcp.client.state.ClientState;
import com.blanke.xsocket.utils.CharsetUtil;
import com.blanke.xsocket.utils.ExceptionUtils;
import com.blanke.xsocket.utils.XSocketLog;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * tcp客户端
 */
public class XTCPClient extends BaseXSocket {
    public static final String TAG = "XTcpClient";
    protected TargetInfo mTargetInfo;//服务端ip及端口号
    protected Socket mSocket;
    protected ClientState mClientState;//客户端状态
    protected TCPConnConfig mTCPConnConfig;//TCP连接配置
    protected ConnectionThread mConnectionThread;//连接线程
    protected SendThread mSendThread;//消息发送线程
    protected ReceiveThread mReceiveThread;//消息接收线程
    protected List<TCPClientListener> mTCPClientListeners;
    private LinkedBlockingQueue<TCPMsg> msgQueue;//消息发送队列

    private XTCPClient() {
        super();
    }

    /**
     * 创建tcp连接，需要提供服务器信息
     *
     * @param targetInfo
     * @return
     */
    public static XTCPClient getTCPClient(TargetInfo targetInfo) {
        return getTCPClient(targetInfo, null);
    }

    public static XTCPClient getTCPClient(TargetInfo targetInfo, TCPConnConfig TCPConnConfig) {
        XTCPClient XTCPClient = TCPClientManager.getTCPClient(targetInfo);
        if (XTCPClient == null) {
            XTCPClient = new XTCPClient();
            XTCPClient.init(targetInfo, TCPConnConfig);
            TCPClientManager.putTCPClient(XTCPClient);
        }
        return XTCPClient;
    }

    /**
     * 根据socket创建client端，目前仅用在socketServer接受client之后
     *
     * @param socket
     * @return
     */
    public static XTCPClient getTCPClient(Socket socket, TargetInfo targetInfo) {
        return getTCPClient(socket, targetInfo, null);
    }

    public static XTCPClient getTCPClient(Socket socket, TargetInfo targetInfo, TCPConnConfig
            connConfig) {
        if (!socket.isConnected()) {
            ExceptionUtils.throwException("socket is closeed");
        }
        XTCPClient XTCPClient = new XTCPClient();
        XTCPClient.init(targetInfo, connConfig);
        XTCPClient.mSocket = socket;
        XTCPClient.mClientState = ClientState.Connected;
        XTCPClient.onConnectSuccess();
        return XTCPClient;
    }


    private void init(TargetInfo targetInfo, TCPConnConfig connConfig) {
        this.mTargetInfo = targetInfo;
        mClientState = ClientState.Disconnected;
        mTCPClientListeners = new ArrayList<>();
        if (mTCPConnConfig == null && connConfig == null) {
            mTCPConnConfig = new TCPConnConfig.Builder().create();
        } else if (connConfig != null) {
            mTCPConnConfig = connConfig;
        }
    }

    public synchronized TCPMsg sendMsg(String message) {
        TCPMsg msg = new TCPMsg(message, mTargetInfo, TCPMsg.MsgType.Send);
        return sendMsg(msg);
    }

    public synchronized TCPMsg sendMsg(byte[] message) {
        TCPMsg msg = new TCPMsg(message, mTargetInfo, TCPMsg.MsgType.Send);
        return sendMsg(msg);
    }

    public synchronized TCPMsg sendMsg(TCPMsg msg) {
        if (isDisconnected()) {
            XSocketLog.d(TAG, "发送消息 " + msg + "，当前没有tcp连接，先进行连接");
            connect();
        }
        boolean re = enqueueTcpMsg(msg);
        if (re) {
            return msg;
        }
        return null;
    }

    public synchronized boolean cancelMsg(TCPMsg msg) {
        return getSendThread().cancel(msg);
    }

    public synchronized boolean cancelMsg(int msgId) {
        return getSendThread().cancel(msgId);
    }

    public synchronized void connect() {
        if (!isDisconnected()) {
            XSocketLog.d(TAG, "已经连接了或正在连接");
            return;
        }
        XSocketLog.d(TAG, "tcp connecting");
        setClientState(ClientState.Connecting);//正在连接
        getConnectionThread().start();
    }

    public synchronized Socket getSocket() {
        if (mSocket == null || isDisconnected() || !mSocket.isConnected()) {
            mSocket = new Socket();
            try {
                mSocket.setSoTimeout((int) mTCPConnConfig.getReceiveTimeout());
            } catch (SocketException e) {
//                e.printStackTrace();
            }
        }
        return mSocket;
    }

    public synchronized void disconnect() {
        resetClient();
        notifyDisconnected("手动关闭tcpclient", null);
    }

    protected synchronized void onErrorDisConnect(String msg, Exception e) {
        if (isDisconnected()) {
            return;
        }
        resetClient();
        notifyConnectError(msg, e);
        if (mTCPConnConfig.isReconnect()) {//失败重连
            connect();
        }
    }

    protected synchronized void resetClient() {
        if (isDisconnected()) {
            return;
        }
        closeSocket();
        getConnectionThread().interrupt();
        getSendThread().interrupt();
        getReceiveThread().interrupt();
        setClientState(ClientState.Disconnected);
        XSocketLog.d(TAG, "tcp closed");
    }

    private synchronized boolean closeSocket() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        return true;
    }

    //连接已经连接，接下来的流程，创建发送和接受消息的线程
    private void onConnectSuccess() {
        XSocketLog.d(TAG, "tcp connect 建立成功");
        setClientState(ClientState.Connected);//标记为已连接
        getSendThread().start();
        getReceiveThread().start();
    }

    /**
     * tcp连接线程
     */
    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                int localPort = mTCPConnConfig.getLocalPort();
                if (localPort > 0) {
                    if (!getSocket().isBound()) {
                        getSocket().bind(new InetSocketAddress(localPort));
                    }
                }
                getSocket().connect(new InetSocketAddress(mTargetInfo.getIp(), mTargetInfo
                        .getPort()),
                        (int) mTCPConnConfig.getConnTimeout());
                XSocketLog.d(TAG, "创建连接成功,target=" + mTargetInfo + ",localport=" + localPort);
            } catch (Exception e) {
                XSocketLog.d(TAG, "创建连接失败,target=" + mTargetInfo + "," + e);
                onErrorDisConnect("创建连接失败", e);
                return;
            }
            notifyConnected();
            onConnectSuccess();
        }
    }

    public boolean enqueueTcpMsg(final TCPMsg TCPMsg) {
        if (TCPMsg == null || getMsgQueue().contains(TCPMsg)) {
            return false;
        }
        try {
            getMsgQueue().put(TCPMsg);
            return true;
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        return false;
    }

    protected LinkedBlockingQueue<TCPMsg> getMsgQueue() {
        if (msgQueue == null) {
            msgQueue = new LinkedBlockingQueue<>();
        }
        return msgQueue;
    }

    private class SendThread extends Thread {
        private TCPMsg sendingTCPMsg;

        protected SendThread setSendingTCPMsg(TCPMsg sendingTCPMsg) {
            this.sendingTCPMsg = sendingTCPMsg;
            return this;
        }

        public TCPMsg getSendingTCPMsg() {
            return this.sendingTCPMsg;
        }

        public boolean cancel(TCPMsg packet) {
            return getMsgQueue().remove(packet);
        }

        public boolean cancel(int tcpMsgID) {
            return getMsgQueue().remove(new TCPMsg(tcpMsgID));
        }

        @Override
        public void run() {
            TCPMsg msg;
            try {
                while (isConnected() && !Thread.interrupted() && (msg = getMsgQueue().take()) !=
                        null) {
                    setSendingTCPMsg(msg);//设置正在发送的
                    XSocketLog.d(TAG, "tcp sending msg=" + msg);
                    byte[] data = msg.getSourceDataBytes();
                    if (data == null) {//根据编码转换消息
                        data = CharsetUtil.stringToData(msg.getSourceDataString(), mTCPConnConfig
                                .getCharsetName());
                    }
                    if (data != null && data.length > 0) {
                        try {
                            getSocket().getOutputStream().write(data);
                            getSocket().getOutputStream().flush();
                            msg.setTime();
                            notifySent(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                            onErrorDisConnect("发送消息失败", e);
                            return;
                        }
                    }
                }
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                InputStream is = getSocket().getInputStream();
                while (isConnected() && !Thread.interrupted()) {
                    byte[] result = mTCPConnConfig.getStickPackageHelper().execute(is);//粘包处理
                    if (result == null) {//报错
                        XSocketLog.d(TAG, "tcp Receive 粘包处理失败 " + Arrays.toString(result));
                        onErrorDisConnect("粘包处理中发送错误", null);
                        break;
                    }
                    XSocketLog.d(TAG, "tcp Receive 解决粘包之后的数据 " + Arrays.toString(result));
                    TCPMsg TCPMsg = new TCPMsg(result, mTargetInfo, MsgType.Receive);
                    TCPMsg.setTime();
                    String msgstr = CharsetUtil.dataToString(result, mTCPConnConfig
                            .getCharsetName());
                    TCPMsg.setSourceDataString(msgstr);
                    boolean va = mTCPConnConfig.getValidationHelper().execute(result);
                    if (!va) {
                        XSocketLog.d(TAG, "tcp Receive 数据验证失败 ");
                        notifyValidationFailed(TCPMsg);//验证失败
                        continue;
                    }
                    byte[][] decodebytes = mTCPConnConfig.getDecodeHelper().execute(result,
                            mTargetInfo, mTCPConnConfig);
                    TCPMsg.setEndDecodeData(decodebytes);
                    XSocketLog.d(TAG, "tcp Receive  succ msg= " + TCPMsg);
                    notifyReceived(TCPMsg);//notify listener
                }
            } catch (Exception e) {
                XSocketLog.d(TAG, "tcp Receive  error  " + e);
                onErrorDisConnect("接受消息错误", e);
            }
        }
    }

    protected ReceiveThread getReceiveThread() {
        if (mReceiveThread == null || !mReceiveThread.isAlive()) {
            mReceiveThread = new ReceiveThread();
        }
        return mReceiveThread;
    }

    protected SendThread getSendThread() {
        if (mSendThread == null || !mSendThread.isAlive()) {
            mSendThread = new SendThread();
        }
        return mSendThread;
    }

    protected ConnectionThread getConnectionThread() {
        if (mConnectionThread == null || !mConnectionThread.isAlive() || mConnectionThread
                .isInterrupted()) {
            mConnectionThread = new ConnectionThread();
        }
        return mConnectionThread;
    }

    public ClientState getClientState() {
        return mClientState;
    }

    protected void setClientState(ClientState state) {
        if (mClientState != state) {
            mClientState = state;
        }
    }

    public boolean isDisconnected() {
        return getClientState() == ClientState.Disconnected;
    }

    public boolean isConnected() {
        return getClientState() == ClientState.Connected;
    }

    private void notifyConnected() {
        TCPClientListener l;
        for (TCPClientListener wl : mTCPClientListeners) {
            final TCPClientListener finalL = wl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalL.onConnected(XTCPClient.this);
                }
            });
        }
    }

    private void notifyDisconnected(final String msg, final Exception e) {
        TCPClientListener l;
        for (TCPClientListener wl : mTCPClientListeners) {
            final TCPClientListener finalL = wl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalL.onDisconnected(XTCPClient.this, msg, e);
                }
            });
        }
    }

    private void notifyConnectError(final String msg, final Exception e) {
        TCPClientListener l;
        for (TCPClientListener wl : mTCPClientListeners) {
            final TCPClientListener finalL = wl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalL.onConnectError(XTCPClient.this, msg, e);
                }
            });
        }
    }


    private void notifyReceived(final TCPMsg TCPMsg) {
        TCPClientListener l;
        for (TCPClientListener wl : mTCPClientListeners) {
            final TCPClientListener finalL = wl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalL.onReceived(XTCPClient.this, TCPMsg);
                }
            });
        }
    }


    private void notifySent(final TCPMsg TCPMsg) {
        TCPClientListener l;
        for (TCPClientListener wl : mTCPClientListeners) {
            final TCPClientListener finalL = wl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalL.onSent(XTCPClient.this, TCPMsg);
                }
            });
        }
    }

    private void notifyValidationFailed(final TCPMsg TCPMsg) {
        TCPClientListener l;
        for (TCPClientListener wl : mTCPClientListeners) {
            final TCPClientListener finalL = wl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalL.onValidationFailed(XTCPClient.this, TCPMsg);
                }
            });
        }
    }

    public TargetInfo getTargetInfo() {
        return mTargetInfo;
    }

    public void addTCPClientListener(TCPClientListener listener) {
        if (mTCPClientListeners.contains(listener)) {
            return;
        }
        mTCPClientListeners.add(listener);
    }

    public void removeTCPClientListener(TCPClientListener listener) {
        mTCPClientListeners.remove(listener);
    }

    public void config(TCPConnConfig TCPConnConfig) {
        mTCPConnConfig = TCPConnConfig;
    }

    @Override
    public String toString() {
        return "XTCPClient{" +
                "mTargetInfo=" + mTargetInfo + ",state=" + mClientState + ",isconnect=" +
                isConnected() +
                '}';
    }
}
