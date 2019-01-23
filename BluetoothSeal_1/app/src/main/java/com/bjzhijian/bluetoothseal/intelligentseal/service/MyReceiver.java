package com.bjzhijian.bluetoothseal.intelligentseal.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fastwork.library.mutils.MLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lenovo on 2019/1/9.
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (isMainActivity) {
//            EventBus.getDefault().post(new MainRefresh(true));
//        }
        Bundle bundle = intent.getExtras();
        String s = printBundle(bundle);
        // 添加员工（更新main小红点）
        if (s.contains("bossAddPerson2")) {
//            if (Constants.isLoginExit) {
//                httpData(context);
//            }
        }
        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
//            if (Constants.isLoginExit) {
//                if (s.contains("applyThings")) {
//                    Intent i = new Intent(context, MyCheckActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//                } else if (s.contains("copeApplyDevice")) {
//                    Intent i = new Intent(context, CopyMeActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//                } else if (s.contains("addNewPerson")) {
//                    Intent i = new Intent(context, ApplyUserActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//                } else if (s.contains("managerApplyDevice")) {
//                    Intent i = new Intent(context, CheckActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//                } else if (s.contains("uploadAfterPhoto")) {
//                    Intent i = new Intent(context, CopyMeDetailActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    CopyMe copyMe = new CopyMe();
//                    String a = bundle.getString("cn.jpush.android.EXTRA");
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(a);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (a.contains("applyId") && jsonObject != null) {
//                        try {
//                            copyMe.setApplyId(jsonObject.getString("applyId"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        copyMe.setEndStatus("2");
//                        Bundle mBundle = new Bundle();
//                        mBundle.putSerializable("data", copyMe);
//                        i.putExtra("data", mBundle);
//                        activity_flag = 0;
//                        context.startActivity(i);
//                    }
//                } else if (s.contains("useDeviceWarning")) {
//                    Intent i = new Intent(context, CallPoliceActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    HttpParams httpParams = new HttpParams();
////                    httpParams.put("personPhone", Constants.user_phone);
////                    httpParams.put("bossPhone", Constants.bossPhone);
////                    httpParams.put("dataNum", "10");
////                    Bundle bundle2 = new Bundle();
////                    bundle2.putSerializable("data", httpParams);
////                    i.putExtra("data", bundle2);
//                    context.startActivity(i);
//                } else if (s.contains("reduceExamNo")) {
//                    Intent i = new Intent(context, MyCheckActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//                }
//            }
        }
    }

    //更新个人信息
    private void httpData(final Context context) {
//        HttpParams httpParams = new HttpParams();
//        httpParams.put("phone", user_phone);
//        OnLoad onLoad = new Loading();
//        onLoad.load(4, context, Urls.getPersonMessage, httpParams, new CallBackLinstener() {
//            @Override
//            public void onsuccess(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    if (s.contains("companyName")) {
//                        if (jsonObject.getString("companyName").contains("&"))
//                            user.setCompany(jsonObject.getString("companyName").split("&")[0]);
//                        else
//                            user.setCompany(jsonObject.getString("companyName"));
//                    }
//
//                    if (s.contains("type")) {
//                        String newLimit = jsonObject.getString("type");
//                        user.setLimit(newLimit);
//                        loginLimit = Integer.parseInt(user.getLimit());
//                    }
//                    if (s.contains("bossPhone")) {
//                        String bossPhone = jsonObject.getString("bossPhone");
//                        user.setBossPhone(bossPhone);
//                        Constants.bossPhone = bossPhone;
//                    }
//                    MyOpenHelper myOpenHelper = DbManger.getIntance(context);
//                    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
//                    DbUser.update2(db, user.getCompany(), user.getLimit(), user.getBossPhone());
//                    db.close();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onfail(String s) {
//
//            }
//        });
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            switch (key) {
                case JPushInterface.EXTRA_NOTIFICATION_ID:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
                    break;
                case JPushInterface.EXTRA_CONNECTION_CHANGE:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
                    break;
                case JPushInterface.EXTRA_EXTRA:
                    if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                        MLogUtil.i(TAG, "This message has no Extra data");
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();
                        while (it.hasNext()) {
                            String myKey = it.next();
                            sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
                    break;
            }
        }
        return sb.toString();
    }

}
