package com.fastwork.library.mutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by lenovo on 2018/12/3.
 * 封装 Toast 管理
 */

public class MToastUtil {

    private static Toast mToast = null;//全局唯一的Toast

    public static void showToast(Context mContext, String msg, int duration) {
        createToast(mContext, msg, duration);
    }

    public static void showShortToast(Context mContext, String msg) {
        createToast(mContext, msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context mContext, int strRes) {
        createToast(mContext, mContext.getResources().getString(strRes), Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context mContext, String msg) {
        createToast(mContext, msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context mContext, int strRes) {
        createToast(mContext, mContext.getResources().getString(strRes), Toast.LENGTH_LONG);
    }

    private static void createToast(Context context, String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, message, duration);
//        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }


}
