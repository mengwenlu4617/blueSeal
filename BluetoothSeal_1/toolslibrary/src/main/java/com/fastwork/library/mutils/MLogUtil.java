package com.fastwork.library.mutils;

import android.util.Log;

/**
 * Created by lenovo on 2018/12/3.
 * 封装 Log 管理
 */

public class MLogUtil {

    private static String TAG = MLogUtil.class.getSimpleName();
    //是否输出
    private static boolean isShowLog = true;

    /*
     * 设置 ShowLog (true:打印日志  false：不打印)
     */
    public static void setIsShowLog(boolean isShowLog) {
        MLogUtil.isShowLog = isShowLog;
    }

    public static void i(String msg) {
        if (isShowLog && msg != null) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isShowLog && tag != null && msg != null) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        if (isShowLog && msg != null) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isShowLog && tag != null && msg != null) {
            Log.w(tag, msg);
        }
    }

    public static void d(String msg) {
        if (isShowLog && msg != null) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isShowLog && tag != null && msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void v(String msg) {
        if (isShowLog && msg != null) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isShowLog && tag != null && msg != null) {
            Log.v(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isShowLog && msg != null) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isShowLog && tag != null && msg != null) {
            Log.e(tag, msg);
        }
    }

}
