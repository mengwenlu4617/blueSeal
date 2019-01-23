package com.bjzhijian.bluetoothseal.intelligentseal.eventbus;

import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate.NetType;

/**
 * Created by lenovo on 2019/1/14.
 * 网络状态改变
 */

public class NetStatusEvent {

    private NetType netType;


    public NetStatusEvent(NetType netType) {
        this.netType = netType;
    }

    public NetType getNetType() {
        return netType;
    }
}
