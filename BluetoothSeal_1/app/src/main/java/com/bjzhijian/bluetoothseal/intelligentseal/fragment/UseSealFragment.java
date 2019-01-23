package com.bjzhijian.bluetoothseal.intelligentseal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/12/27.
 * 用章
 */

public class UseSealFragment extends BaseFragment {

    private Unbinder unbinder;

    @SuppressLint("StaticFieldLeak")
    private static UseSealFragment instance = null;

    public static UseSealFragment getInstance() {
        if (instance == null) {
            instance = new UseSealFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_useseal_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {

    }

    private void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
