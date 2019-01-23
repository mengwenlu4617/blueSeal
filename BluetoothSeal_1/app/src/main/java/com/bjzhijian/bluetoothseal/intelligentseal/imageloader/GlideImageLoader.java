package com.bjzhijian.bluetoothseal.intelligentseal.imageloader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.fastwork.library.mutils.MGlideUtil;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * Created by lenovo on 2019/1/10.
 * GlideImageLoader
 */

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        MGlideUtil.loadImage(activity,Uri.fromFile(new File(path)), com.fastwork.library.R.drawable.image_default,imageView);

//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .error(R.drawable.ic_default_image)           //设置错误图片
//                .placeholder(R.drawable.ic_default_image)     //设置占位图片
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        MGlideUtil.loadImage(activity,Uri.fromFile(new File(path)), com.fastwork.library.R.drawable.image_default,imageView);
    }

    @Override
    public void clearMemoryCache() {
    }

}
