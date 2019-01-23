package com.bjzhijian.bluetoothseal.intelligentseal.callback;

/**
 * Created by lenovo on 2019/1/15.
 * adapter 中实现监听
 */

public interface HomeAdapterListener {

    // banner 点击
    void bannerClick(int position);

    // 功能模块点击
    void channelClick(int viewId);

    // 使用列表点击
    void itemClick(int position);

}
