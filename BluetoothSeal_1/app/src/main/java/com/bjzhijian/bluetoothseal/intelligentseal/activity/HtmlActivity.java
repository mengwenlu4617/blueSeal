package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MWebView;
import com.fastwork.library.listener.OnMultiClickListener;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/12/25.
 * 公共的网页显示
 */

public class HtmlActivity extends BaseActivity {

    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.html_WebView)
    MWebView htmlWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_html_layout;
    }

    @Override
    protected void initData() {
        String url = "";
        String extra = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(extra)) {
            if ("service".equals(extra)) {
                titleCenterText.setText("服务协议");
                url = Urls.URL_HOST + "/init/negotiate";
            } else if ("secrecy".equals(extra)) {
                titleCenterText.setText("保密协议");
                url = Urls.URL_HOST + "/init/privacy";
            } else if ("help".equals(extra)) {
                titleCenterText.setText("文字介绍");
                url = Urls.URL_HOST + "/init/help";
            } else if ("shareDocument".equals(extra)) {
                titleCenterText.setText("上传介绍");
                url = Urls.URL_HOST + "/init/shareDocument";
            }
            htmlWebView.loadUrl(url);
        }
    }

    @Override
    protected void setListener() {
        findViewById(R.id.title_left_layout).setOnClickListener(
                new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        finishActivity();
                    }
                }
        );
    }
}
