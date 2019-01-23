package com.bjzhijian.bluetoothseal.intelligentseal.httpclient;

import java.util.HashMap;

/**
 * Created by lenovo on 2018/12/4.
 * 请求地址
 */

public class Urls {

    //本地       :  9020     :   8080
    public static String URL_HOST = "http://192.168.1.121:9020/zhijiantwo/";
    // 线上
//    public static String URL_HOST = "http://123.56.78.223:9060/zhijianApp/";

    //本地
    public static String IMAGE_URL_HOST = "http://192.168.1.121:9020/zhijiantwo";
    // 线上
//    public static String IMAGE_URL_HOST = "http://123.56.78.223:9060/zhijianApp";


    //apk下载地址
    public static String app_update = URL_HOST + "file/downloadFile.download?filename=android.apk";
    // 查询手机号是否注册过
    public static String registerCheck = URL_HOST + "register/check";
    // 新用户注册
    public static String registerUser = URL_HOST + "register/user";
    // 用户登录
    public static String loginUser = URL_HOST + "login/user";
    // 重置密码
    public static String updatePwd = URL_HOST + "updatePwd";
    // 获取用户信息（员工信息）
    public static String getPersonMessage = URL_HOST + "getPersonMessage";
    // 修改用户信息
    public static String updataPersonMessage = URL_HOST + "updataPersonMessage";
    // 修改邮箱
    public static String email = URL_HOST + "person/email";
    //员工主动退出公司
    public static String exitCompany = URL_HOST + "person/exitCompany";
    //上传图片
    public static String file_upload = URL_HOST + "file_upload";
    //员工获取自己的使用记录
    public static String person = URL_HOST + "record/person";
    //历史记录获取(管理员和boss)
    public static String showAllDeviceRecord = URL_HOST + "showAllDeviceRecord";
    // 获取当前手机号下的公司所有部门 ； 验证当前人手机号是否不为员工,禁止员工查询
    public static String getDepartmentByPhone = URL_HOST + "depart/getDepartmentByPhone";
    // 修改部门名称
    public static String replaceDepartmentName = URL_HOST + "depart/replaceDepartmentName";
    // 添加部门
    public static String insert = URL_HOST + "depart/insert";
    // 删除部门
    public static String delDepartment = URL_HOST + "depart/delDepartment";
    // 获取未添加部门的员工列表
    public static String getPersonByDepartment = URL_HOST + "depart/getPersonByDepartment";
    // 获取未添加部门的印章列表
    public static String getDeviceByDepartment = URL_HOST + "depart/getDeviceByDepartment";
    // 批量添加员工到指定部门
    public static String insertPersonToDepartment = URL_HOST + "/depart/insertPersonToDepartment";
    // 批量添加印章到指定部门
    public static String insertDeviceToDepartment = URL_HOST + "/depart/insertDeviceToDepartment";
    // 批量移除部门下的所有员工
    public static String updatePersonDepartment = URL_HOST + "/depart/updatePersonDepartment";
    // 批量移除部门下的所有印章
    public static String updateDeviceDepartment = URL_HOST + "/depart/updateDeviceDepartment";


    /**
     * Method
     */
    static String getMethodMap(String key) {
        HashMap<String, String> mathMap = new HashMap<>();
        mathMap.put("register/check", "registerCheck45");
        mathMap.put("register/user", "register57");
        mathMap.put("login/user", "login126");
        mathMap.put("updatePwd", "updatePwd213");
        mathMap.put("getPersonMessage", "getPersonMessage");
        mathMap.put("updataPersonMessage", "updataPersionMessage134");
        mathMap.put("person/email", "email");
        mathMap.put("person/exitCompany", "delPerson121");
        mathMap.put("record/person", "selectAllUseRecordByPhone");
        mathMap.put("showAllDeviceRecord", "showAllDeviceRecordByPhone");
        mathMap.put("depart/getDepartmentByPhone", "getDepartmentByPhone");
        mathMap.put("depart/replaceDepartmentName", "changeDepartName");
        mathMap.put("depart/insert", "insertDepartment");
        mathMap.put("depart/delDepartment", "updateThingDepartment");
        mathMap.put("depart/getPersonByDepartment", "getPersonByDepartment");
        mathMap.put("depart/getDeviceByDepartment", "getDeviceByDepartment");
        mathMap.put("depart/insertPersonToDepartment", "insertDepartment");
        mathMap.put("depart/insertDeviceToDepartment", "insertDepartment");
        mathMap.put("depart/updatePersonDepartment", "updateThingDepartment");
        mathMap.put("depart/updateDeviceDepartment", "updateThingDepartment");
        return mathMap.get(key);
    }
}
