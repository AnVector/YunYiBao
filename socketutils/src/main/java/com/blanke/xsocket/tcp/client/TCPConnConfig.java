package com.blanke.xsocket.tcp.client;

import com.blanke.xsocket.tcp.client.helper.decode.AbsDecodeHelper;
import com.blanke.xsocket.tcp.client.helper.decode.BaseDecodeHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.AbsStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.BaseStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.validation.AbsValidationHelper;
import com.blanke.xsocket.tcp.client.helper.validation.BaseValidationHelper;
import com.blanke.xsocket.utils.CharsetUtil;
import com.blanke.xsocket.utils.StringValidationUtils;

import java.nio.ByteOrder;

/**
 * 连接配置
 */
public class TCPConnConfig {

    private String charsetName = CharsetUtil.UTF_8;//默认编码
    private long connTimeout = 5000;//连接超时时间
    private long receiveTimeout = 0;//接受消息的超时时间,0为无限大
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;//大端还是小端
    private AbsStickPackageHelper mStickPackageHelper = new BaseStickPackageHelper();//解决粘包
    private AbsDecodeHelper mDecodeHelper = new BaseDecodeHelper();//解析数据
    private AbsValidationHelper mValidationHelper = new BaseValidationHelper();//消息验证
    private boolean isReconnect = false;//是否重连
    private int localPort = -1;//本地端口号

    private TCPConnConfig() {
    }

    public String getCharsetName() {
        return charsetName;
    }

    public long getConnTimeout() {
        return connTimeout;
    }

    public boolean isReconnect() {
        return isReconnect;
    }

    public AbsValidationHelper getValidationHelper() {
        return mValidationHelper;
    }

    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    public AbsDecodeHelper getDecodeHelper() {
        return mDecodeHelper;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public AbsStickPackageHelper getStickPackageHelper() {
        return mStickPackageHelper;
    }

    public int getLocalPort() {
        return localPort;
    }

    public static class Builder {
        private TCPConnConfig mTCPConnConfig;

        public Builder() {
            mTCPConnConfig = new TCPConnConfig();
        }

        public TCPConnConfig create() {
            return mTCPConnConfig;
        }

        public Builder setCharsetName(String charsetName) {
            mTCPConnConfig.charsetName = charsetName;
            return this;
        }

        public Builder setByteOrder(ByteOrder byteOrder) {
            mTCPConnConfig.byteOrder = byteOrder;
            return this;
        }

        public Builder setValidationHelper(AbsValidationHelper validationHelper) {
            mTCPConnConfig.mValidationHelper = validationHelper;
            return this;
        }

        public Builder setConnTimeout(long timeout) {
            mTCPConnConfig.connTimeout = timeout;
            return this;
        }

        public Builder setIsReconnect(boolean b) {
            mTCPConnConfig.isReconnect = b;
            return this;
        }

        //bug
        @Deprecated
        public Builder setLocalPort(int localPort) {
            if (localPort > 0 && StringValidationUtils.validateRegex
                    (localPort + "", StringValidationUtils.RegexPort)) {
                mTCPConnConfig.localPort = localPort;
            }
            return this;
        }

        public Builder setStickPackageHelper(AbsStickPackageHelper helper) {
            mTCPConnConfig.mStickPackageHelper = helper;
            return this;
        }

        public Builder setDecodeHelper(AbsDecodeHelper helper) {
            mTCPConnConfig.mDecodeHelper = helper;
            return this;
        }
    }
}
