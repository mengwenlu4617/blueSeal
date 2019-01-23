package com.bjzhijian.bluetoothseal.intelligentseal.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebView;

/**
 * Created by lenovo on 2019/1/7.
 * 自定义web
 */

public class MWebView extends WebView {

    public MWebView(Context context) {
        super(context);
        init();
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        this.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        this.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        this.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        this.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.getSettings().setLoadWithOverviewMode(true);
        this.setWebViewClient(new WebViewClient());
    }
}
