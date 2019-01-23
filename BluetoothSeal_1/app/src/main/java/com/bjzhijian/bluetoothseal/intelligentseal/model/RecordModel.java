package com.bjzhijian.bluetoothseal.intelligentseal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/15.
 * 记录实体
 */

public class RecordModel implements Serializable{

    private String applyId;// 申请id
    private String applyTime; // 申请时间
    private String createTime; // 创建时间
    private String mac;// 使用的印章  mac
    private String deviceName; // 设备名称（印章名）
    private String deviceVersion; // 设备版本
    private String version; // 设备版本
    private int deviceType; // 设备类型

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
