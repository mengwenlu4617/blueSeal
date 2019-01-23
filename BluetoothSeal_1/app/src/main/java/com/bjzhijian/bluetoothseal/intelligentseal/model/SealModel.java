package com.bjzhijian.bluetoothseal.intelligentseal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/17.
 * 印章实体类
 */

public class SealModel implements Serializable{

    private String deviceElectricity;
    private String deviceVersion;// 设备硬件版本
    private String lastTime;
    private String mac;
    private String name;
    private int type;
    private String version;
    private boolean isSelect;

    public String getDeviceElectricity() {
        return deviceElectricity;
    }

    public void setDeviceElectricity(String deviceElectricity) {
        this.deviceElectricity = deviceElectricity;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
