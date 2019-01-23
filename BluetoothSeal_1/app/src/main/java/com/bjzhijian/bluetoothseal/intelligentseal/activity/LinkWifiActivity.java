package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.config.Constants;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.BluetoothEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AesUtil;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/14.
 * 扫码配网
 */

public class LinkWifiActivity extends BaseActivity {

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_linkwifi_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(R.string.zxing_linkwifi);
        //
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera_layout);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.linkwifi_fl_container, captureFragment).commit();
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finishActivity();
            }
        });
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            if (!TextUtils.isEmpty(result)) {
                try {
                    result = AesUtil.decrypt(result, AesUtil.KEY, AesUtil.IV);
                    MLogUtil.e(" result   " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BluetoothEntity entity = MJsonUtil.gsonToBean(result, BluetoothEntity.class);
                if (entity != null) {
                    Constants.bluetoothMac = entity.getMac();
                    switch (entity.getStatus()) {
                        case 0:

                            break;
                        case 1:
                            IntentUtil.startActivityFinish(LinkWifiActivity.this, LinkSearchActivity.class);
                            break;
                    }
                }
            }
        }

        @Override
        public void onAnalyzeFailed() {
            MLogUtil.e(" 扫码失败   ");
        }
    };
}
