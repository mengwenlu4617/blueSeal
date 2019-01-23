package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.config.PrefreConfig;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.PreferencesUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/12/25.
 * 引导页
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private Activity mActivity = GuideActivity.this;
    @BindView(R.id.guide_viewPager)
    ViewPager guideViewPager;
    @BindView(R.id.guide_btn_toLogin)
    Button guideBtnToLogin;
    private int[] imgs = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private ArrayList<ImageView> imageViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_guide_layout;
    }

    @Override
    protected void initData() {
        //设置每一张图片都填充窗口
        LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageViews = new ArrayList<ImageView>();
        for (int img : imgs) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams); // 设置布局
            iv.setImageResource(img);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(iv);
        }
        guideViewPager.setAdapter(new MyPagerAdapter(imageViews));
    }

    @Override
    protected void setListener() {
        guideViewPager.addOnPageChangeListener(this);
        guideBtnToLogin.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                PreferencesUtils.put(mActivity, PrefreConfig.isShowGuide, false);
                if ((boolean) PreferencesUtils.get(mActivity, PrefreConfig.loginStatus, false)) {
                    finish();
                } else {
                    IntentUtil.startActivityFinish(mActivity, LoginActivity.class);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == (imageViews.size() - 1)) {
            guideBtnToLogin.setVisibility(View.VISIBLE);
        } else {
            guideBtnToLogin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // pager 适配
    private class MyPagerAdapter extends PagerAdapter {

        private ArrayList<ImageView> views;

        MyPagerAdapter(ArrayList<ImageView> imageViews) {
            this.views = imageViews;
        }

        /**
         * 获取当前要显示对象的数量
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 判断是否用对象生成界面
         */
        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0 == arg1;
        }

        /**
         * 从ViewGroup中移除当前对象（图片）
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        /**
         * 当前要显示的对象（图片）
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }
}
