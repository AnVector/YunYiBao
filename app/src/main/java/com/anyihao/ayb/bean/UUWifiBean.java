package com.anyihao.ayb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class UUWifiBean implements Serializable {


    /**
     * msg : GET-ACK
     * code : 200
     * desc : OK
     * seq : 4294967295
     * data : {"device_selector":"UUWIFI201610300000100125","version":"2.1.42","battery":90,"cpuusage":1,
     * "uptime":70,"datatype":"R","datatval":"R","rev":2706,"ispid":0,"ispname":"","sigint":0,
     * "sigtype":"none","simst":1,"dataup":0,"datadown":0,"cellid":"","rate":"0 kb/s",
     * "nettype":0,"netenb":"","gcellid":"","gsigint":0,"gdataup":84,"gdatadown":90,"gnettype":0,
     * "gnetenb":"","wificonnt":1,"wifisig":0,"wififreq":0,"apn":"3GNET","apnnum":"*99#",
     * "apnusr":"","apnpwd":""}
     */

    private String msg;
    private int code;
    private String desc;
    private long seq;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * device_selector : UUWIFI201610300000100125
         * version : 2.1.42
         * battery : 90
         * cpuusage : 1
         * uptime : 70
         * datatype : R
         * datatval : R
         * rev : 2706
         * ispid : 0
         * ispname :
         * sigint : 0
         * sigtype : none
         * simst : 1
         * dataup : 0
         * datadown : 0
         * cellid :
         * rate : 0 kb/s
         * nettype : 0
         * netenb :
         * gcellid :
         * gsigint : 0
         * gdataup : 84
         * gdatadown : 90
         * gnettype : 0
         * gnetenb :
         * wificonnt : 1
         * wifisig : 0
         * wififreq : 0
         * apn : 3GNET
         * apnnum : *99#
         * apnusr :
         * apnpwd :
         */

        private String device;
        private String version;
        private int battery;
        private int cpuusage;
        private int uptime;
        private String datatype;
        private String datatval;
        private int rev;
        private int ispid;
        private String ispname;
        private int sigint;
        private String sigtype;
        private int simst;
        private int dataup;
        private int datadown;
        private String cellid;
        private String rate;
        private int nettype;
        private String netenb;
        private String gcellid;
        private int gsigint;
        private int gdataup;
        private int gdatadown;
        private int gnettype;
        private String gnetenb;
        private int wificonnt;
        private int wifisig;
        private int wififreq;
        private String apn;
        private String apnnum;
        private String apnusr;
        private String apnpwd;

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getBattery() {
            return battery;
        }

        public void setBattery(int battery) {
            this.battery = battery;
        }

        public int getCpuusage() {
            return cpuusage;
        }

        public void setCpuusage(int cpuusage) {
            this.cpuusage = cpuusage;
        }

        public int getUptime() {
            return uptime;
        }

        public void setUptime(int uptime) {
            this.uptime = uptime;
        }

        public String getDatatype() {
            return datatype;
        }

        public void setDatatype(String datatype) {
            this.datatype = datatype;
        }

        public String getDatatval() {
            return datatval;
        }

        public void setDatatval(String datatval) {
            this.datatval = datatval;
        }

        public int getRev() {
            return rev;
        }

        public void setRev(int rev) {
            this.rev = rev;
        }

        public int getIspid() {
            return ispid;
        }

        public void setIspid(int ispid) {
            this.ispid = ispid;
        }

        public String getIspname() {
            return ispname;
        }

        public void setIspname(String ispname) {
            this.ispname = ispname;
        }

        public int getSigint() {
            return sigint;
        }

        public void setSigint(int sigint) {
            this.sigint = sigint;
        }

        public String getSigtype() {
            return sigtype;
        }

        public void setSigtype(String sigtype) {
            this.sigtype = sigtype;
        }

        public int getSimst() {
            return simst;
        }

        public void setSimst(int simst) {
            this.simst = simst;
        }

        public int getDataup() {
            return dataup;
        }

        public void setDataup(int dataup) {
            this.dataup = dataup;
        }

        public int getDatadown() {
            return datadown;
        }

        public void setDatadown(int datadown) {
            this.datadown = datadown;
        }

        public String getCellid() {
            return cellid;
        }

        public void setCellid(String cellid) {
            this.cellid = cellid;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public int getNettype() {
            return nettype;
        }

        public void setNettype(int nettype) {
            this.nettype = nettype;
        }

        public String getNetenb() {
            return netenb;
        }

        public void setNetenb(String netenb) {
            this.netenb = netenb;
        }

        public String getGcellid() {
            return gcellid;
        }

        public void setGcellid(String gcellid) {
            this.gcellid = gcellid;
        }

        public int getGsigint() {
            return gsigint;
        }

        public void setGsigint(int gsigint) {
            this.gsigint = gsigint;
        }

        public int getGdataup() {
            return gdataup;
        }

        public void setGdataup(int gdataup) {
            this.gdataup = gdataup;
        }

        public int getGdatadown() {
            return gdatadown;
        }

        public void setGdatadown(int gdatadown) {
            this.gdatadown = gdatadown;
        }

        public int getGnettype() {
            return gnettype;
        }

        public void setGnettype(int gnettype) {
            this.gnettype = gnettype;
        }

        public String getGnetenb() {
            return gnetenb;
        }

        public void setGnetenb(String gnetenb) {
            this.gnetenb = gnetenb;
        }

        public int getWificonnt() {
            return wificonnt;
        }

        public void setWificonnt(int wificonnt) {
            this.wificonnt = wificonnt;
        }

        public int getWifisig() {
            return wifisig;
        }

        public void setWifisig(int wifisig) {
            this.wifisig = wifisig;
        }

        public int getWififreq() {
            return wififreq;
        }

        public void setWififreq(int wififreq) {
            this.wififreq = wififreq;
        }

        public String getApn() {
            return apn;
        }

        public void setApn(String apn) {
            this.apn = apn;
        }

        public String getApnnum() {
            return apnnum;
        }

        public void setApnnum(String apnnum) {
            this.apnnum = apnnum;
        }

        public String getApnusr() {
            return apnusr;
        }

        public void setApnusr(String apnusr) {
            this.apnusr = apnusr;
        }

        public String getApnpwd() {
            return apnpwd;
        }

        public void setApnpwd(String apnpwd) {
            this.apnpwd = apnpwd;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.device);
            dest.writeString(this.version);
            dest.writeInt(this.battery);
            dest.writeInt(this.cpuusage);
            dest.writeInt(this.uptime);
            dest.writeString(this.datatype);
            dest.writeString(this.datatval);
            dest.writeInt(this.rev);
            dest.writeInt(this.ispid);
            dest.writeString(this.ispname);
            dest.writeInt(this.sigint);
            dest.writeString(this.sigtype);
            dest.writeInt(this.simst);
            dest.writeInt(this.dataup);
            dest.writeInt(this.datadown);
            dest.writeString(this.cellid);
            dest.writeString(this.rate);
            dest.writeInt(this.nettype);
            dest.writeString(this.netenb);
            dest.writeString(this.gcellid);
            dest.writeInt(this.gsigint);
            dest.writeInt(this.gdataup);
            dest.writeInt(this.gdatadown);
            dest.writeInt(this.gnettype);
            dest.writeString(this.gnetenb);
            dest.writeInt(this.wificonnt);
            dest.writeInt(this.wifisig);
            dest.writeInt(this.wififreq);
            dest.writeString(this.apn);
            dest.writeString(this.apnnum);
            dest.writeString(this.apnusr);
            dest.writeString(this.apnpwd);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.device = in.readString();
            this.version = in.readString();
            this.battery = in.readInt();
            this.cpuusage = in.readInt();
            this.uptime = in.readInt();
            this.datatype = in.readString();
            this.datatval = in.readString();
            this.rev = in.readInt();
            this.ispid = in.readInt();
            this.ispname = in.readString();
            this.sigint = in.readInt();
            this.sigtype = in.readString();
            this.simst = in.readInt();
            this.dataup = in.readInt();
            this.datadown = in.readInt();
            this.cellid = in.readString();
            this.rate = in.readString();
            this.nettype = in.readInt();
            this.netenb = in.readString();
            this.gcellid = in.readString();
            this.gsigint = in.readInt();
            this.gdataup = in.readInt();
            this.gdatadown = in.readInt();
            this.gnettype = in.readInt();
            this.gnetenb = in.readString();
            this.wificonnt = in.readInt();
            this.wifisig = in.readInt();
            this.wififreq = in.readInt();
            this.apn = in.readString();
            this.apnnum = in.readString();
            this.apnusr = in.readString();
            this.apnpwd = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public String toString() {
        DataBean bean = this.getData();
        return "DataBean --> device_selector: " + bean.getDevice()
                + "; ver: " + bean.getVersion()
                + "; battery: " + bean.getBattery()
                + "; cpuusage: " + bean.getCpuusage()
                + ";\n uptime: " + bean.getUptime()
                + "; data type: " + bean.getDatatype()
                + "; isp id: " + bean.getIspid()
                + "; isp name: " + bean.getIspname()
                + "; sig int: " + bean.getSigint()
                + ";\n rev: " + bean.getRev()
                + "; dataup: " + bean.getDataup()
                + "; datadown: " + bean.getDatadown()
                + "; cell id: " + bean.getCellid()
                + "; simst: " + bean.getSimst()
                + "; rate: " + bean.getRate()
                + "; net type: " + bean.getNettype()
                + ";\n net enb: " + bean.getNetenb()
                + "; gcell id: " + bean.getGcellid()
                + "; gsigint: " + bean.getGsigint()
                + "; gdata up: " + bean.getGdataup()
                + "; gdata down: " + bean.getGdatadown()
                + "; gdata type: " + bean.getGnettype()
                + "; gdata enb: " + bean.getNetenb()
                + ";\n wifi count: " + bean.getWificonnt()
                + "; wifi sig: " + bean.getWifisig()
                + "; wifi freq: " + bean.getWififreq()
                + ";\n apn: " + bean.getApn()
                + "; apn num: " + bean.getApnnum()
                + "; apn usr: " + bean.getApnusr()
                + "; apn pwd: " + bean.getApnpwd();
    }
}
