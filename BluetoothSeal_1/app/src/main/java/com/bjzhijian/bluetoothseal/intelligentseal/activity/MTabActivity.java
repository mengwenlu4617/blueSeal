package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.Constants;
import com.bjzhijian.bluetoothseal.intelligentseal.config.PrefreConfig;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.eventbus.NetStatusEvent;
import com.bjzhijian.bluetoothseal.intelligentseal.fragment.HomeFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.fragment.LinkWifiFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.fragment.MineFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.fragment.UseSealFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.netstate.NetType;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.utils.MPermissionUtil;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MBottomNavigationView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AppManager;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.PreferencesUtils;
import com.lzy.okgo.model.HttpParams;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/12/24.
 * 主页切换tab
 */

public class MTabActivity extends BaseActivity implements OnNavigationItemSelectedListener {

    private Activity mActivity = MTabActivity.this;

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.mtab_navigation)
    MBottomNavigationView mtabNavigation;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    private final int INDEX_0 = 0;
    private final int INDEX_1 = 1;
    private final int INDEX_2 = 2;
    private final int INDEX_3 = 3;
    private HomeFragment homeFragment;
    private UseSealFragment useSealFragment;
    private LinkWifiFragment linkWifiFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_mtab_layout;
    }

    @Override
    protected void initData() {
        titleLeftImage.setVisibility(View.INVISIBLE);
        titleRightImage.setVisibility(View.VISIBLE);

        changeFragment(INDEX_0);
    }

    @Override
    protected void setListener() {
        // 切换监听
        mtabNavigation.setOnNavigationItemSelectedListener(this);
        titleRightLayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                final String[] permis = MPermissionUtil.PERMISSION_CAMERA;
                MPermissionUtil.checkAndRequestMorePermissions(mActivity,
                        permis, MPermissionUtil.PERMISSION_REQUEST_CODE,
                        new MPermissionUtil.PermissionRequestSuccessCallBack() {
                            @Override
                            public void onHasPermission() {
                                Intent intent = new Intent(mActivity, CaptureActivity.class);
                                startActivityForResult(intent, 111);
                            }
                        });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtil.onRequestMorePermissionsResult(mActivity, permissions, new MPermissionUtil.PermissionCheckCallBack() {

            @Override
            public void onHasPermission() {
                Intent intent = new Intent(mActivity, CaptureActivity.class);
                startActivityForResult(intent, 111);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                MToastUtil.showShortToast(mActivity, "相机授权失败，无法使用相机功能");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                MToastUtil.showShortToast(mActivity, "相机授权失败，无法使用相机功能");
            }
        });
    }

    @Override
    protected void onNetConnectedStatus(NetType type) {
        EventBus.getDefault().post(new NetStatusEvent(type));
    }

    // 上次点击返回时间戳
    long lastSelectTime = 0;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (System.currentTimeMillis() - lastSelectTime > 100) {
            lastSelectTime = System.currentTimeMillis();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(INDEX_0);
                    return true;
                case R.id.navigation_useSeal:
                    changeFragment(INDEX_1);
                    return true;
                case R.id.navigation_linkWifi:
                    changeFragment(INDEX_2);
                    return true;
                case R.id.navigation_mine:
                    changeFragment(INDEX_3);
                    return true;
            }
        }
        return false;
    }

    //显示第一个fragment
    @SuppressLint("CommitTransaction")
    private void changeFragment(int index) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (index) {
            case INDEX_0:
                titleCenterText.setText(R.string.tab_title_home);
                if (homeFragment == null) {
                    homeFragment = HomeFragment.getInstance();
                    ft.add(R.id.tab_frameLayout, homeFragment);
                }
                ft.show(homeFragment);
                break;
            case INDEX_1:
                titleCenterText.setText(R.string.tab_title_useSeal);
                if (useSealFragment == null) {
                    useSealFragment = UseSealFragment.getInstance();
                    ft.add(R.id.tab_frameLayout, useSealFragment);
                }
                ft.show(useSealFragment);
                break;
            case INDEX_2:
                titleCenterText.setText(R.string.tab_title_linkWifi);
                if (linkWifiFragment == null) {
                    linkWifiFragment = LinkWifiFragment.getInstance();
                    ft.add(R.id.tab_frameLayout, linkWifiFragment);
                }
                ft.show(linkWifiFragment);
                break;
            case INDEX_3:
                titleCenterText.setText(R.string.tab_title_mime);
                if (mineFragment == null) {
                    mineFragment = MineFragment.getInstance();
                    ft.add(R.id.tab_frameLayout, mineFragment);
                }
                ft.show(mineFragment);
                break;
        }
        //提交事务
        ft.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (useSealFragment != null) {
            ft.hide(useSealFragment);
        }
        if (linkWifiFragment != null) {
            ft.hide(linkWifiFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }


    private long lastBackPressed = 0;

    // 按两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > 2000) {
                lastBackPressed = backPressed;
                Toast.makeText(this, getString(R.string.press_again_exit), Toast.LENGTH_SHORT).show();
            } else {
                AppManager.getAppManager().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取用户信息
     **/
    private void getUserInfo() {
        final String phone = (String) PreferencesUtils.get(mActivity, PrefreConfig.loginPhone, "");
        if (TextUtils.isEmpty(phone)) {
            MLogUtil.e("登录手机号为空");
            return;
        }
        HttpParams params = new HttpParams();
        params.put("phone", phone);
        OkGoBuilder.getInstance()
                .builder(mActivity)
                .url(Urls.getPersonMessage)
                .params(params)
                .postRequest(new RequestCallback() {
                    @Override
                    public void onRequestSuccess(String data) {
                        if (!TextUtils.isEmpty(data)) {
                            UserEntity entity = MJsonUtil.gsonToBean(data, UserEntity.class);
                            if (entity != null) {
                                Constants.loginLimit = entity.getType();
                                Constants.userPhone = phone;
                                Constants.bossPhone = entity.getBossPhone();
                                entity.setPhone(phone);
                                // 将登录用户信息放到单例中
                                UserManager.getUserManager().setUserEntity(entity);
                            }
                        }
                    }

                    @Override
                    public void onRequestFailed(String error) {
                        if (!TextUtils.isEmpty(error)) {
                            MToastUtil.showShortToast(mActivity, error);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGoBuilder.cancelAll();
    }
}
