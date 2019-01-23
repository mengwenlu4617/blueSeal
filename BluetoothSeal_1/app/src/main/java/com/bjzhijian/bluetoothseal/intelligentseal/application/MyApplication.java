package com.bjzhijian.bluetoothseal.intelligentseal.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate.NetStateReceiver;
import com.fastwork.library.mutils.CrashHandlerUtil;
import com.fastwork.library.mutils.DynamicTimeFormat;
import com.fastwork.library.mutils.MFilerUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.sms.SMSSDK;
import okhttp3.OkHttpClient;

/**
 * Created by lenovo on 2018/11/5.
 * Application
 */

public class MyApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private static MyApplication myApplication;

    static {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置（优先级最低）
                layout.setEnableRefresh(true);
                layout.setEnableLoadMore(false);
                layout.setEnableAutoLoadMore(true);
                layout.setEnableOverScrollDrag(false);
                layout.setEnableOverScrollBounce(true);
                layout.setEnableLoadMoreWhenContentNotFull(true);
                layout.setEnableScrollContentWhenRefreshed(true);
            }
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
                layout.setPrimaryColorsId(R.color.gray_666, android.R.color.white);
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setPrimaryColorId(R.color.gray_666).setAccentColorId(android.R.color.white);
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        context = getApplicationContext();

        initJpush();
        initOkGo();
        initSMSSDK();
        // 二维码
        ZXingLibrary.initDisplayOpinion(this);
        // 启动异常捕捉管理
        CrashHandlerUtil.getInstance().init(this);
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
    }

    private void initJpush() {
        //初始化sdk
        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("bjzhijian");//名字任意，可多添加几个
        set.add("andfixdemo");//名字任意，可多添加几个
        JPushInterface.setTags(this, 1, set);
        // 极光分享
//        JShareInterface.setDebugMode(true);
//        JShareInterface.init(this);
    }

    // 初始化极光短信
    private void initSMSSDK() {
        SMSSDK.getInstance().initSdk(this);
        SMSSDK.getInstance().setDebugMode(true);
    }


    public static MyApplication getMyApplication() {
        return myApplication;
    }

    // 配置okgo
    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS / 2, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS / 2, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS / 2, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers);                   //全局公共头
//                .addCommonParams(params);                     //全局公共参数
    }

//    private void createFiler() {
//        File f1 = MFilerUtil.createFileDir(this, MFilerUtil.crashFileName);
//        MLogUtil.i("MyApplication", "path : " + f1.getAbsolutePath());
//        File f2 = MFilerUtil.createFileDir(this, MFilerUtil.cacheFileName);
//        MLogUtil.i("MyApplication", "path : " + f2.getAbsolutePath());
//        File f3 = MFilerUtil.createFileDir(this, MFilerUtil.cameraFileName);
//        MLogUtil.i("MyApplication", "path : " + f3.getAbsolutePath());
//        File f4 = MFilerUtil.createFileDir(this, MFilerUtil.documentFileName);
//        MLogUtil.i("MyApplication", "path : " + f4.getAbsolutePath());
//        File f5 = MFilerUtil.createFileDir(this, MFilerUtil.dbFileName);
//        MLogUtil.i("MyApplication", "path : " + f5.getAbsolutePath());
//    }


    // 应用内存优化  OnLowMemory   OnTrimMemory
    // OnLowMemory是Android提供的API，在系统内存不足，所有后台程序
    // （优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // OnTrimMemory是Android 4.0之后提供的API，系统会根据不同的内存状态来回调。
    // 根据不同的内存状态，来响应不同的内存释放策略。
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    // 判断当前线程是否是主线程
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
