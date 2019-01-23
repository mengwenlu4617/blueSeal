package com.fastwork.library.mutils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.fastwork.library.R;

import java.io.File;

/**
 * Created by lenovo on 2018/12/25.
 * Intent 跳转
 */

public class IntentUtil {

    /**
     * Intent 正常跳转
     */
    public static void startActivity(Activity act, Class<?> cls) {
        act.startActivity(new Intent(act, cls));
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * Intent 带 String 参数跳转
     */
    public static void startActivity(Activity act, Class<?> cls, String data) {
        Intent intent = new Intent(act, cls);
        intent.putExtra("data", data);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * Intent 带 Bundle 跳转
     */
    public static void startActivity(Activity act, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(act, cls);
        intent.putExtra("bundle", bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 正常跳转销毁当前页
     */
    public static void startActivityFinish(Activity act, Class<?> cls) {
        act.startActivity(new Intent(act, cls));
        act.finish();
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 带参数跳转销毁当前页
     */
    public static void startActivityFinish(Activity act, Class<?> cls, String data) {
        Intent intent = new Intent(act, cls);
        intent.putExtra("data", data);
        act.startActivity(intent);
        act.finish();
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 带参数跳转销毁当前页
     */
    public static void startActivityFinish(Activity act, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(act, cls);
        intent.putExtra("bundle", bundle);
        act.startActivity(intent);
        act.finish();
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 跳转并接收下个页面的数据
     */
    public static void startActivityForResult(Activity act, Class<?> cls, int requestCode) {
        act.startActivityForResult(new Intent(act, cls), requestCode);
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 带参数跳转并接收下个页面的数据
     */
    public static void startActivityForResult(Activity act, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(act, cls);
        intent.putExtra("bundle", bundle);
        act.startActivityForResult(intent, requestCode);
        act.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 跳转到权限设置界面
     */
    public static void openPermisSetting(Activity act) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", act.getPackageName(), null));
        act.startActivityForResult(intent, 111);
    }

    /**
     * 打开网络设置界面
     */
    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 222);
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

}
