package com.fastwork.library.mutils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

import com.fastwork.library.viewutil.alertdialog.MActionSheetDialog;
import com.fastwork.library.viewutil.alertdialog.MActionSheetDialog.*;
import com.fastwork.library.viewutil.alertdialog.MAlertDialog;

/**
 * Created by lenovo on 2018/12/28.
 * Dialog
 */

public class DialogUIUtils {

    /**
     * 关闭弹出框
     */
    public static void dismiss(DialogInterface... dialogs) {
        if (dialogs != null && dialogs.length > 0) {
            for (DialogInterface dialog : dialogs) {
                if (dialog instanceof Dialog) {
                    Dialog dialog1 = (Dialog) dialog;
                    if (dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                } else if (dialog instanceof AppCompatDialog) {
                    AppCompatDialog dialog2 = (AppCompatDialog) dialog;
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                }
            }
        }
    }

    /**
     * 展示一个从下方弹出图库选择提示框
     *
     * @param activity
     * @param onImagesListListenr 图库按钮监听
     */
    public static void showBottomSheetDialog_SeletorImages(Activity activity, OnSheetItemClickListener onImagesListListenr) {
        new MActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setTitle("请选择图片来源")
                .addSheetItem("图库", SheetItemColor.Blue, onImagesListListenr)
//                .addSheetItem("相机", MActionSheetDialog.SheetItemColor.Blue, onCameraListener)
                .show();
    }

    /**
     * 展示一个从下方弹出的带红色删除item和取消按钮的sheet对话框.仿iOS
     *
     * @param activity                    必须为activity
     * @param title                       标题
     * @param onDeleteButtonTouchListener 删除按钮监听器
     * @return
     */
    public static void showIsDeleteSheetDialog(Activity activity, String title, OnSheetItemClickListener onDeleteButtonTouchListener) {
        new MActionSheetDialog(activity)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle(title)
                .addSheetItem("删除", SheetItemColor.Red, onDeleteButtonTouchListener)
                .show();
    }

    /**
     * 展示一个仅有一个按钮的对话框  仿iOS中间弹出
     *
     * @param activity        必须为activity
     * @param msg             提示的内容
     * @param bt_msg          按钮的文字
     * @param onClickListener 点击事件监听
     */
    public static void showOnlyOneButtonAlertDialog(Activity activity, String title, String msg, String bt_msg, View.OnClickListener onClickListener) {
        new MAlertDialog(activity)
                .builder()
                .setTitle(title)
                .setMsg(msg)
                .setNegativeButton(bt_msg, onClickListener)
                .show();
    }

    /**
     * 展示一个有两个按钮的对话框  仿iOS中间弹出
     *
     * @param activity       必须为activity
     * @param title          标题
     * @param msg            提示语
     * @param bt_msg_left    左边按钮的文字
     * @param listener_left  左边按钮点击事件
     * @param bt_msg_right   右边按钮文字
     * @param listener_right 右边按钮点击事件
     * @param cancelable     点击空白处取消?
     */
    public static void showTwoButtonAlertDialog(Activity activity, String title, String msg, String bt_msg_left,
                                                View.OnClickListener listener_left, String bt_msg_right, View.OnClickListener listener_right, boolean cancelable) {
        new MAlertDialog(activity)
                .builder()
                .setTitle(title)
                .setMsg(msg)
                .setPositiveButton(bt_msg_right, listener_right)
                .setNegativeButton(bt_msg_left, listener_left)
                .setCancelable(cancelable)
                .show();

    }

}
