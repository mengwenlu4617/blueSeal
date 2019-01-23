package com.bjzhijian.bluetoothseal.intelligentseal.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate.NetChangeObserver;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate.NetStateReceiver;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate.NetType;
import com.bjzhijian.bluetoothseal.intelligentseal.views.LoadDialogView;
import com.fastwork.library.mutils.AppManager;

import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/11/6.
 * Activity 的基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private LoadDialogView loadDialogView;
    /**
     * 网络观察者
     */
    protected NetChangeObserver mNetChangeObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        // activity 管理
        AppManager.getAppManager().addActivity(this);
        // 加载弹框
        loadDialogView = new LoadDialogView(this);

        initData();
        setListener();

        netChangeObserver();
    }

    /**
     * 显示加载弹框
     */
    protected void showLoadDialog() {
        loadDialogView.show();
    }

    /**
     * 取消加载框
     */
    protected void dismissLoadDialog() {
        loadDialogView.dismiss();
    }

    // 网络改变的一个回掉类
    private void netChangeObserver() {
        //开启广播去监听 网络 改变事件
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnectedState(NetType type) {
                onNetConnectedStatus(type);
            }
        };
        NetStateReceiver.registerObserver(mNetChangeObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除广播去监听 网络 改变事件
        if (mNetChangeObserver != null) {
            NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        }
    }

    /**
     * 设置布局
     */
    protected abstract int getLayoutResID();

    /**
     * 初始化数据源
     */
    protected abstract void initData();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 网络连接状态
     */
    protected void onNetConnectedStatus(NetType type) {
    }

    public void finishActivity() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

}
