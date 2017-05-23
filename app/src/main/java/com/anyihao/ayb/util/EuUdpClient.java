package com.anyihao.ayb.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 从硬件处获取硬件信息
 *
 * @author SUU
 */
public class EuUdpClient {

    private static volatile EuUdpClient instance = null;
    private DatagramSocket m_socket;
    private String m_serverIP;
    private int m_serverPort;
    // 定义接收网络数据的字节数组
    byte[] inBuff;
    // 以指定字节数组创建准备接收数据的DatagramPacket对象
    private DatagramPacket in;

    private EuUdpClient() {
        m_serverIP = "192.168.1.102";
        m_serverPort = 8090;
        inBuff = new byte[4096];
        in = new DatagramPacket(inBuff, inBuff.length);
    }

    public static EuUdpClient getInstance() {
        if (null == instance) {
            synchronized (EuUdpClient.class) {
                if (null == instance) {
                    instance = new EuUdpClient();
                }
            }
        }
        return instance;
    }

    private String sendMsg(String msg) throws IOException {
        String result = null;
        m_socket = new DatagramSocket();
        m_socket.setSoTimeout(1500);
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName
                (m_serverIP), m_serverPort);
        m_socket.send(packet);
        m_socket.receive(in);
        if (in.getData() == null) {
            return null;
        } else {
            result = new String(in.getData(), 0, in.getLength());
        }
        m_socket.close();
        return result;
    }

    public String setPwd(String newPwd) throws IOException {
        String msg = "{'msg':'SET','seq':'123','uid':'root','pwd':'123','wifipwd':" + "'" + newPwd + "'" + "}";//修改WIFI密码
        return sendMsg(msg);
    }

    public String getWifiInfo() throws IOException {
        String sendStr = "{'msg':'GET','seq':'4294967295'}";//获取设备信息
        return sendMsg(sendStr);
    }
}
