package com.fastwork.library.mutils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by lenovo on 2018/12/3.
 * 封装 文件夹 管理
 */

public class MFilerUtil {

    // 异常崩溃文件
    public static String crashFileName = "/bjzhijian_file/crash/";
    // 缓存目录
    public static String cacheFileName = "/bjzhijian_file/cache/";
    // 相机拍照裁剪目录
    public static String cameraFileName = "/bjzhijian_file/camera/";
    // 附件文档缓存目录
    public static String documentFileName = "/bjzhijian_file/document/";
    // 数据库缓存目录
    public static String dbFileName = "/bjzhijian_file/dbdata/";

    private static final String TAG = MFilerUtil.class.getSimpleName();

    /**
     * 检查是否已挂载SD卡镜像（是否存在SD卡）
     *
     * @return
     */
    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            MLogUtil.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    /**
     * 获取可用的SD卡路径（若SD卡不没有挂载则返回""）
     *
     * @return
     */

    public static String gainSDCardPath() {
        if (isMountedSDCard()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                MLogUtil.w(TAG, "SDCARD can not write !");
            }
            return sdcardDir.getPath();
        }
        return "";
    }

    /**
     * 创建目录
     *
     * @param context
     * @param dirName 文件夹名称
     */
    public static File createFileDir(Context context, String dirName) {
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (isMountedSDCard()) {
            // SD卡路径
            filePath = gainSDCardPath() + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            MLogUtil.i(TAG, filePath + " has created. " + isCreate);
        }
        return destDir;
    }

}
