package com.bjzhijian.bluetoothseal.intelligentseal.callback;

/**
 * Created by lenovo on 2018/12/4.
 * 请求的回调
 */

public interface RequestCallback {

    /**
     * 请求成功
     */
    void onRequestSuccess(String data);

    /**
     * 请求的失败
     */
    void onRequestFailed(String error);
}
