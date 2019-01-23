package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/12/25.
 * 关于印章设备
 */

public class AboutSealActivity extends BaseActivity {

    private Activity mActivity = AboutSealActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.aboutSeal_layout_founction)
    LinearLayout aboutSealLayoutFounction;
    @BindView(R.id.aboutSeal_layout_videoDesc)
    LinearLayout aboutSealLayoutVideoDesc;
    @BindView(R.id.aboutSeal_layout_documentDesc)
    LinearLayout aboutSealLayoutDocumentDesc;
    @BindView(R.id.aboutSeal_layout_version)
    LinearLayout aboutSealLayoutVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about_seal_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(getString(R.string.txt_about_device));
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        //
        aboutSealLayoutFounction.setOnClickListener(clickListener);
        aboutSealLayoutVideoDesc.setOnClickListener(clickListener);
        aboutSealLayoutDocumentDesc.setOnClickListener(clickListener);
        aboutSealLayoutVersion.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.aboutSeal_layout_founction:
                    IntentUtil.startActivity(mActivity,HtmlActivity.class,"help");
                    break;
                case R.id.aboutSeal_layout_videoDesc:

                    break;
                case R.id.aboutSeal_layout_documentDesc:
                    IntentUtil.startActivity(mActivity,HtmlActivity.class,"help");
                    break;
                case R.id.aboutSeal_layout_version:

                    break;
            }
        }
    };

}
