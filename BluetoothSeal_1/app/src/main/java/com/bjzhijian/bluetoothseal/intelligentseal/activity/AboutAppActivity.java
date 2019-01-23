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
import com.fastwork.library.mutils.MToastUtil;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/12/25.
 * 关于app
 */

public class AboutAppActivity extends BaseActivity {

    private Activity mActivity = AboutAppActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.aboutApp_layout_guide)
    LinearLayout aboutAppLayoutGuide;
    @BindView(R.id.aboutApp_layout_service)
    LinearLayout aboutAppLayoutService;
    @BindView(R.id.aboutApp_layout_secrecy)
    LinearLayout aboutAppLayoutSecrecy;
    @BindView(R.id.aboutApp_layout_version)
    LinearLayout aboutAppLayoutVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about_app_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(getString(R.string.txt_about_app));
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        //
        aboutAppLayoutGuide.setOnClickListener(clickListener);
        aboutAppLayoutService.setOnClickListener(clickListener);
        aboutAppLayoutSecrecy.setOnClickListener(clickListener);
        aboutAppLayoutVersion.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.aboutApp_layout_guide:
                    IntentUtil.startActivity(mActivity,GuideActivity.class);
                    break;
                case R.id.aboutApp_layout_service:
                    IntentUtil.startActivity(mActivity,HtmlActivity.class,"service");
                    break;
                case R.id.aboutApp_layout_secrecy:
                    IntentUtil.startActivity(mActivity,HtmlActivity.class,"secrecy");
                    break;
                case R.id.aboutApp_layout_version:
                    MToastUtil.showShortToast(mActivity,"当前已是最新版本");
                    break;
            }
        }
    };

}
