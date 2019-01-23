package com.bjzhijian.bluetoothseal.intelligentseal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.LinkWifiActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.bletooth.BleManager;
import com.bjzhijian.bluetoothseal.intelligentseal.eventbus.NetStatusEvent;
import com.bjzhijian.bluetoothseal.intelligentseal.utils.MPermissionUtil;
import com.bjzhijian.bluetoothseal.intelligentseal.utils.NetworkUtil;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MButton;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/12/27.
 * 配网
 */

public class LinkWifiFragment extends BaseFragment {

    @BindView(R.id.linkFifi_tv_wifiName)
    MTextView linkFifiTvWifiName;
    @BindView(R.id.linkFifi_et_wifiPass)
    MEditText linkFifiEtWifiPass;
    @BindView(R.id.linkFifi_btn_go)
    MButton linkFifiBtnGo;
    private String wifiname, wifipass;// 名称   密码
    private Unbinder unbinder;
    @SuppressLint("StaticFieldLeak")
    private static LinkWifiFragment instance = null;

    public static LinkWifiFragment getInstance() {
        if (instance == null) {
            instance = new LinkWifiFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_linkwifi_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);//注册EventBus
        initView();
        return rootView;
    }

    private void initView() {
        initData();
    }

    private void initData() {
        setListener();
    }

    private void setListener() {
        linkFifiBtnGo.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            wifiname = linkFifiTvWifiName.getText().toString();
            wifipass = linkFifiEtWifiPass.getText().toString();
            if (TextUtils.isEmpty(wifiname)) {
                MToastUtil.showShortToast(getActivity(), getString(R.string.wifi_name));
            } else {
                if (NetworkUtil.is5GWifi(getActivity())) {
                    MToastUtil.showShortToast(getActivity(), getString(R.string.link_no_five));
                } else {
                    if (BleManager.getBleManager(getActivity()).isOpenBle()){
                        cameraPermission();
                    }else {
                        BleManager.getBleManager(getActivity()).openBle();
                    }
                }
            }
        }
    };

    // 相机权限
    private void cameraPermission() {
        final String[] permis = MPermissionUtil.PERMISSION_CAMERA;
        MPermissionUtil.checkAndRequestMorePermissions(getActivity(), permis, MPermissionUtil.PERMISSION_REQUEST_CODE,
                new MPermissionUtil.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        PreferencesUtils.put(getActivity(), wifiname, wifipass);
                        IntentUtil.startActivity(getActivity(), LinkWifiActivity.class);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtil.onRequestMorePermissionsResult(getActivity(), permissions, new MPermissionUtil.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                PreferencesUtils.put(getActivity(), wifiname, wifipass);
                IntentUtil.startActivity(getActivity(), LinkWifiActivity.class);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                MToastUtil.showShortToast(getActivity(),"相机授权失败，无法使用相机功能");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                MToastUtil.showShortToast(getActivity(),"相机授权失败，无法使用相机功能");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiname = NetworkUtil.getWifiName(getActivity());
        if (!TextUtils.isEmpty(wifiname)) {
            linkFifiTvWifiName.setText(wifiname);
            wifipass = (String) PreferencesUtils.get(getActivity(), wifiname, "");
            if (wifipass != null) {
                linkFifiEtWifiPass.setText(wifipass);
                linkFifiEtWifiPass.setSelection(wifipass.length());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(NetStatusEvent statusEvent) {
        if (statusEvent != null) {
            if (statusEvent.getNetType().name().equals("WIFI")) {
                wifiname = NetworkUtil.getWifiName(getActivity());
                linkFifiTvWifiName.setText(wifiname);
                wifipass = (String) PreferencesUtils.get(getActivity(), wifiname, "");
                if (wifipass != null) {
                    linkFifiEtWifiPass.setText(wifipass);
                    linkFifiEtWifiPass.setSelection(wifipass.length());
                }
            } else {
                linkFifiTvWifiName.setText("");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//反注册EventBus
        }
    }
}
