package com.fastwork.library.listener;

import android.view.View;

/**
 * Created by lenovo on 2018/12/3.
 * 防止点击过快发生多次响应
 */

public abstract class OnMultiClickListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 600;
    private static long lastClickTime;

    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        }
    }
}
