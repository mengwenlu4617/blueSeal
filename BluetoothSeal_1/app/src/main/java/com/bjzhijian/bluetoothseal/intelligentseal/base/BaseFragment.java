package com.bjzhijian.bluetoothseal.intelligentseal.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzhijian.bluetoothseal.intelligentseal.views.LoadDialogView;

/**
 * Created by lenovo on 2018/11/6.
 * Fragment 基类
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 加载框
     **/
    private LoadDialogView loadDialogView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // 显示加载弹框
    protected void showLoadDialog(Context context) {
        if (loadDialogView == null) {
            loadDialogView = new LoadDialogView(context);
        }
        loadDialogView.show();
    }

    // 取消加载框
    protected void dismissLoadDialog() {
        if (loadDialogView != null) {
            loadDialogView.dismiss();
        }
    }
}
