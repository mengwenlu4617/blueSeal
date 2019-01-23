package com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate;

/**
 * Created by lenovo on 2018/12/26.
 * 网络改变观察者，观察网络改变后回调的方法
 */

public interface  NetChangeObserver {

    /**
     * 网络连接回调 type为网络类型
     */
    void onNetConnectedState(NetType type);

}
