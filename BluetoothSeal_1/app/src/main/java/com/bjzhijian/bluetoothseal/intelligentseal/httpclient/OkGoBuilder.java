package com.bjzhijian.bluetoothseal.intelligentseal.httpclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.FileEntity;
import com.fastwork.library.mutils.AesUtil;
import com.fastwork.library.mutils.MD5Util;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by lenovo on 2018/12/4.
 * 对 okgo 再次封装  --->>>  建造者模式
 */

public class OkGoBuilder {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 请求网址
     */
    private String url;
    /**
     * 参数
     */
    private HttpParams params;
    /**
     * 单列模式
     **/
    @SuppressLint("StaticFieldLeak")
    private static OkGoBuilder mOkGoBuilder = null;

    /**
     * 构造函数私有化
     **/
    private OkGoBuilder() {
    }

    /**
     * 公有的静态函数，对外暴露获取单例对象的接口
     **/
    public static OkGoBuilder getInstance() {
        if (mOkGoBuilder == null) {
            synchronized (OkGoBuilder.class) {
                if (mOkGoBuilder == null) {
                    mOkGoBuilder = new OkGoBuilder();
                }
            }
        }
        return mOkGoBuilder;
    }


    public OkGoBuilder builder(Context context) {
        this.mContext = context;
        return this;
    }

    public OkGoBuilder url(String url) {
        this.url = url;
        return this;
    }

    public OkGoBuilder params(HttpParams params) {
        this.params = params;
        return this;
    }

    /**
     * get请求
     */
    public void getRequest(final RequestCallback callback) {

    }

    /**
     * 上传文件（附件、图片）
     */
    public void upLoadRequest(@NonNull final RequestCallback callback) {
        OkGo.<String>post(url)
                .tag(mContext)
                .params(params)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        if (!TextUtils.isEmpty(body)) {
                            if (callback != null) {
                                callback.onRequestSuccess(body);
                            }
                        } else {
                            if (callback != null) {
                                callback.onRequestFailed("上传失败");
                            }
                        }
                    }
                });
    }

    /**
     * post请求(正常参数请求)
     */
    public void postRequest(@NonNull final RequestCallback callback) {
        String del = url.replace(Urls.URL_HOST, "");
        String method = Urls.getMethodMap(del);
        String valid = randomInt();
        String validMd5 = MD5Util.getMD5UpperString(valid + method);
        params.put("valid", valid);
        params.put("validMd5", validMd5);
        OkGo.<String>post(url)
                .tag(mContext)
                .params(params)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        if (!TextUtils.isEmpty(body)) {
                            BaseEntity entity = MJsonUtil.gsonToBean(body, BaseEntity.class);
                            if (entity != null && entity.getCode() == -1) {
                                MLogUtil.e("OkGoBuilder  :  接口   method   为空或者错误");
                            } else if (entity != null && entity.getCode() == 0) {
                                try {
                                    body = URLDecoder.decode(body, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                if (callback != null) {
                                    callback.onRequestSuccess(body);
                                }
                            } else {
                                if (callback != null && !TextUtils.isEmpty(entity.getMessage())) {
                                    callback.onRequestFailed("" + ErrInfos.getErrMsg(entity.getMessage()));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable exception = response.getException();
                        if (exception instanceof ConnectException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.no_net_check));
                        } else if (exception instanceof TimeoutException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.net_timeout));
                        } else if (exception instanceof SocketException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.no_net_check));
                        } else if (exception instanceof SocketTimeoutException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.net_timeout));
                        } else {
                            MLogUtil.e("onError   ==========    " + exception.toString());
                        }
                    }
                });
    }

    /**
     * post请求(参数 MD5 加密请求)
     */
    public void postMD5Request(HashMap map, @NonNull final RequestCallback callback) {
        String del = url.replace(Urls.URL_HOST, "");
        String method = Urls.getMethodMap(del);
        HttpParams httpParams = new HttpParams(); // 创建一个新HttpParams
        httpParams.put("md5", MJsonUtil.getMd5Data(map, method));
        httpParams.put("data", MJsonUtil.hashMapToJson(map));
        OkGo.<String>post(url)
                .tag(mContext)
                .params(httpParams)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        if (!TextUtils.isEmpty(body)) {
                            BaseEntity entity = MJsonUtil.gsonToBean(body, BaseEntity.class);
                            if (entity != null && entity.getCode() == -1) {
                                MLogUtil.e("OkGoBuilder  :  接口   method   为空或者错误");
                            } else if (entity != null && entity.getCode() == 0) {
                                try {
                                    body = URLDecoder.decode(body, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                if (callback != null) {
                                    callback.onRequestSuccess(body);
                                }
                            } else {
                                if (callback != null && !TextUtils.isEmpty(entity.getMessage())) {
                                    callback.onRequestFailed("" + ErrInfos.getErrMsg(entity.getMessage()));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable exception = response.getException();
                        if (exception instanceof ConnectException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.no_net_check));
                        } else if (exception instanceof TimeoutException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.net_timeout));
                        } else if (exception instanceof SocketException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.no_net_check));
                        } else if (exception instanceof SocketTimeoutException) {
                            MToastUtil.showShortToast(mContext, mContext.getString(R.string.net_timeout));
                        } else {
                            MLogUtil.e("onError   ==========    " + exception.toString());
                        }
                    }
                });
    }

    /**
     * 取消 tag 请求
     */
    public static void cancelTag(Context context) {
        OkGo.getInstance().cancelTag(context);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        OkGo.getInstance().cancelAll();
    }

    /**
     * 随机数
     */
    private String randomInt() {
        return (int) (1 + Math.random() * (10 - 1 + 1)) + "";
    }

}
