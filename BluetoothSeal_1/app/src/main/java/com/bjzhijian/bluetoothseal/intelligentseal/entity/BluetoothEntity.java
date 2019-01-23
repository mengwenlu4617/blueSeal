package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/14.
 * 蓝牙设备回传的数据
 */

public class BluetoothEntity implements Serializable {
    // {"a":"qc","m":"30-AE-A4-5F-50-BA","v":"1.1","s":"0","uss":"0","000":"000"}
    @SerializedName(value = "a")
    private String action; // action
    @SerializedName(value = "m")
    private String mac; // mac
    @SerializedName(value = "v")
    private String version; // version
    @SerializedName(value = "e")
    private String electricity; // 电量
    @SerializedName(value = "s")
    private int status; //status 1 已添加  0 待添加   2正在使用
    private String cu;
    private long ra; // 随机数;
    private int uss;  // 0 未使用   1 正在使用

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCu() {
        return cu;
    }

    public void setCu(String cu) {
        this.cu = cu;
    }

    public long getRa() {
        return ra;
    }

    public void setRa(long ra) {
        this.ra = ra;
    }

    public int getUss() {
        return uss;
    }

    public void setUss(int uss) {
        this.uss = uss;
    }
}
