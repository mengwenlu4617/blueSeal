package com.bjzhijian.bluetoothseal.intelligentseal.config;

/**
 * Created by lenovo on 2018/12/25.
 * 常用的公共参数
 */

public class Constants {

    // 每页条数
    public static int pagerNum = 10;
    // 自定义秘钥  key   iv
    public static String aesKey = "";
    public static String aesIv = "";
    //登录权限 0=员工 1=老板 2=管理员
    public static int loginLimit = 0;
    // 登录用户手机号
    public static String userPhone = "";
    // Boss 手机号
    public static String bossPhone = "";
    // 全局的mac
    public static String bluetoothMac = "";
}
