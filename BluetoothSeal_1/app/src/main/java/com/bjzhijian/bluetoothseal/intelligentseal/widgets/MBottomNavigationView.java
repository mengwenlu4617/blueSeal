package com.bjzhijian.bluetoothseal.intelligentseal.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;

import com.fastwork.library.mutils.MLogUtil;

import java.lang.reflect.Field;

/**
 * Created by lenovo on 2018/12/4.
 * 自定义底部导航栏
 * 为了适配超过3个按钮布局问题
 */

public class MBottomNavigationView extends BottomNavigationView {

    public MBottomNavigationView(Context context) {
        super(context);
        disableShiftMode();
    }

    public MBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        disableShiftMode();
    }

    public MBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        disableShiftMode();
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) this.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            MLogUtil.e("BNVHelper", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            MLogUtil.e("BNVHelper", "Unable to change value of shift mode");
        }
    }
}
