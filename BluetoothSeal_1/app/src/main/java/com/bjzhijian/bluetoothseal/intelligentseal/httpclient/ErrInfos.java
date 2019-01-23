package com.bjzhijian.bluetoothseal.intelligentseal.httpclient;

import android.os.Looper;

import com.fastwork.library.mutils.MToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2018/12/4.
 * 请求错误信息
 */

public class ErrInfos {

    public static String getErrMsg(final String err) {
        String errs = "{\"this user is exception ,his message many\":\"用户资料数据错误\"," +
                "\"param is null\":\"参数错误\",\"power is not enough\":\"您无权进行此操作修改\"," +
                "\"phone is null\":\"手机号未被使用\",\"name is null\":\"员工姓名为空\",\"password is error\":\"密码错误\"," +
                "\"this personPhone in other company\":\"此账户已被其他公司添加\"," +
                "\"this user is exception ,his message not unique\":\"用户不存在\"," +
                "\"pageNum or dataNum is null\":\"参数错误\",\"this phone message is null\":\"手机号不存在\"," +
                "\"phone is used\":\"手机号已被注册\",\"this phone  is being audited\":\"审核已提交,请等待审批结果\"," +
                "\"the phone already exists\":\"转发人已在审批流中，请不要重复选择\",\"time out\":\"超时\"," +
                "\"device message is error\":\"印章信息错误\",\"useNumber error\":\"使用次数错误\"," +
                "\"device not online\":\"印章未在线\",\"userType Exception\":\"身份异常\",\"device is useing\":\"印章正在使用中\"," +
                "\"this record exception\":\"\",\"no email\":\"主管理员未绑定企业邮箱\"," +
                "\"version wrong\":\"当前版本已停止维护，请前往应用宝更新最新版本以获得更好服务。\"," +
                "\"identity is wrong\":\"无管理权限，撤销失败\"}";
        if (errs.contains(err)) {
            JSONObject object = null;
            try {
                object = new JSONObject(errs);
                return object.getString(err);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
