package com.blanke.xsocket.tcp.client.helper.decode;

import com.blanke.xsocket.tcp.client.TCPConnConfig;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;

public class BaseDecodeHelper implements AbsDecodeHelper {
    @Override
    public byte[][] execute(byte[] data, TargetInfo targetInfo, TCPConnConfig TCPConnConfig) {
        return new byte[][]{data};
    }
}
