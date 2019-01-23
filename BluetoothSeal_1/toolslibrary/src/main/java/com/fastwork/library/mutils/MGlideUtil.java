package com.fastwork.library.mutils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fastwork.library.R;
import com.fastwork.library.viewutil.transformation.CircleTransformation;
import com.fastwork.library.viewutil.transformation.RadiusTransformation;

import java.io.File;

/**
 * Created by lenovo on 2018/12/3.
 * Glide 封装
 */

public class MGlideUtil {

    /**
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）
     **/

    /*
     *加载网络图片(默认)
     */
    public static void loadImage(Activity activity, Uri uri, int resourceId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(resourceId) //占位图
                .error(resourceId)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(activity)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    /*
     *加载网络图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image_default) //占位图
                .error(R.drawable.image_default)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /*
     *加载File图片(默认)
     */
    public static void loadImage(Context context, File file, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image_default) //占位图
                .error(R.drawable.image_default)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(file)
                .apply(options)
                .into(imageView);
    }

    /*
     *加载资源图片(默认)
     */
    public static void loadImage(Context context, int resourceId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image_default) //占位图
                .error(R.drawable.image_default)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(resourceId)
                .apply(options)
                .into(imageView);
    }

    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image_default) //占位图
                .error(R.drawable.image_default)       //错误图
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    /**
     * 加载圆角图片
     */
    public static void loadRadiusImage(Context context, String url, ImageView imageView, int radius) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image_default) //占位图
                .error(R.drawable.image_default)       //错误图
                .transforms(new RadiusTransformation(radius))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * @param url 图片路径
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(R.drawable.image_circle_photo) //占位图
                .error(R.drawable.image_circle_photo)       //错误图
                .transforms(new CircleTransformation())
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * @param file 图片文件
     * 加载圆形图片
     */
    public static void loadCircleFile(Context context, File file, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(R.drawable.image_circle_photo) //占位图
                .error(R.drawable.image_circle_photo)       //错误图
                .transforms(new CircleTransformation())
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context).load(file).apply(options).into(imageView);
    }

}
