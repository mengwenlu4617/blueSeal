package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.PrefreConfig;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.utils.MPermissionUtil;
import com.fastwork.library.mutils.DialogUIUtils;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.PreferencesUtils;
import com.lzy.okgo.model.HttpParams;

public class MainActivity extends BaseActivity {

    private Activity mActivity = MainActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main_layout;
    }

    @Override
    protected void initData() {
        getUserInfo();
        requestPermission();
    }

    @Override
    protected void setListener() {

    }

    private void requestPermission() {
        final String[] permis = MPermissionUtil.PERMISSION_GROUP;
        MPermissionUtil.checkMorePermissions(mActivity, permis, new MPermissionUtil.PermissionCheckCallBack() {

            @Override
            public void onHasPermission() {
                toChangeWindow();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                showExplainDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MPermissionUtil.requestMorePermissions(mActivity, permis, MPermissionUtil.PERMISSION_REQUEST_CODE);
                    }
                });
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                MPermissionUtil.requestMorePermissions(mActivity, permis, MPermissionUtil.PERMISSION_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtil.onRequestMorePermissionsResult(mActivity, permissions, new MPermissionUtil.PermissionCheckCallBack() {

            @Override
            public void onHasPermission() {
                MToastUtil.showShortToast(MainActivity.this, "权限申请成功");
                toChangeWindow();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                MToastUtil.showShortToast(MainActivity.this, "权限申请失败");
                finishActivity();
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                showToAppSettingDialog();
            }
        });
    }

    // 跳转
    private void toChangeWindow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isShowGuide = (boolean) PreferencesUtils.get(mActivity, PrefreConfig.isShowGuide, true);
                boolean loginStatus = (boolean) PreferencesUtils.get(mActivity, PrefreConfig.loginStatus, false);
                // isShowGuide ：是否显示引导页 true 显示  false 不显示
                if (isShowGuide) {
                    IntentUtil.startActivityFinish(mActivity, GuideActivity.class);
                } else {
                    // loginStatus ：登录状态  true 直接到首页   false 跳转到登录
                    UserEntity userEntity = UserManager.getUserManager().getUserEntity();
                    if (loginStatus) {
                        if (userEntity != null && !TextUtils.isEmpty(userEntity.getPhone())) {
                            IntentUtil.startActivityFinish(mActivity, MTabActivity.class);
                        } else {
                            PreferencesUtils.put(mActivity,PrefreConfig.loginStatus, false);
                            IntentUtil.startActivityFinish(mActivity, LoginActivity.class);
                        }
                    } else {
                        IntentUtil.startActivityFinish(mActivity, LoginActivity.class);
                    }
                }
            }
        }, 2000);
    }

    // 同步用户数据
    private void getUserInfo() {
        final String phone = (String) PreferencesUtils.get(mActivity, PrefreConfig.loginPhone, "");
        if (!TextUtils.isEmpty(phone)) {
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
                                    entity.setPhone(phone);
                                    // 将登录用户信息放到单例中
                                    UserManager.getUserManager().setUserEntity(entity);
                                }
                            }
                        }

                        @Override
                        public void onRequestFailed(String error) {

                        }
                    });
        }
    }

    /**
     * 解释权限的dialog
     */
    private void showExplainDialog(View.OnClickListener onClickListener) {
        DialogUIUtils.showTwoButtonAlertDialog(mActivity, "申请权限", "我们需要相关权限，才能实现功能",
                "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUIUtils.dismiss();
                        finishActivity();
                    }
                }, "确定", onClickListener, true);
    }

    /**
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        DialogUIUtils.showOnlyOneButtonAlertDialog(mActivity, "需要权限",
                "我们需要相关权限，才能实现功能\n请前往应用的权限设置界面开启相关权限",
                "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUIUtils.dismiss();
                        finishActivity();
                    }
                });
    }
}
