package com.bjzhijian.bluetoothseal.intelligentseal.callback;

/**
 * Created by M on 2018/11
 * 蓝牙适配服务的接口
 */

public interface OnLinkStatusCallBack {

    /**
     * 连接成功
     **/
    void onDisconnectSuccess();

    /**
     * 连接失败/断开连接
     **/
    void onDisconnectFailed(int status);

    /**
     * 发送数据完成
     **/
    void onSendDataFinish();

    /**
     * 接收数据完成
     **/
    void onReciveData(String data);

    /**
     * 接收数据完成
     **/
    void onReadRemoteRssi(int rssi);

}
