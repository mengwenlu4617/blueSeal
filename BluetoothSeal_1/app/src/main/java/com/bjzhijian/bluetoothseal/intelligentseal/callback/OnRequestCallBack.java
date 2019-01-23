package com.bjzhijian.bluetoothseal.intelligentseal.callback;

/**
 * Created by M on 2018/11
 * 发送蓝牙指令请求的接口
 */

public interface OnRequestCallBack {
    /**
     * 发送成功
     **/
    void onRequestSuccess(String data);

    /**
     * 发送失败
     **/
    void onRequestFailed(int errCode);
}
