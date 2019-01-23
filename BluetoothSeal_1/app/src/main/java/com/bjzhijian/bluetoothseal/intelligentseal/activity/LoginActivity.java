package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
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
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MButton;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AppInfoUtil;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MD5Util;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MRegexUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.PreferencesUtils;
import com.lzy.okgo.model.HttpParams;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by lenovo on 2019/1/3.
 * 登录
 */

public class LoginActivity extends BaseActivity {

    private Activity mActivity = LoginActivity.this;

    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.login_et_phone)
    MEditText loginEtPhone;
    @BindView(R.id.login_et_pass)
    MEditText loginEtPass;
    @BindView(R.id.login_btn_login)
    MButton loginBtnLogin;
    @BindView(R.id.login_tv_forgetPass)
    MTextView loginTvForgetPass;
    @BindView(R.id.login_tv_regist)
    MTextView loginTvRegist;
    @BindView(R.id.login_tv_service)
    MTextView loginTvService;
    @BindView(R.id.login_tv_secrecy)
    MTextView loginTvSecrecy;
    private String pushId = "", phone = "", pass = "";
//    private UserTableMar userTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(R.string.login);
        //
//        File f5 = MFilerUtil.createFileDir(this, MFilerUtil.dbFileName);
//        if (f5.exists()) {
//            userTable = UserTableMar.getManager(mActivity);
//        }
        // 获取缓存的手机号和密码
        phone = (String) PreferencesUtils.get(mActivity, PrefreConfig.loginPhone, "");
        pass = (String) PreferencesUtils.get(mActivity, PrefreConfig.loginPass, "");
        if (!TextUtils.isEmpty(phone)) {
            loginEtPhone.setText(phone);
        }
        if (!TextUtils.isEmpty(pass)) {
            loginEtPass.setText(pass);
        }
        // getRegistrationID
        pushId = JPushInterface.getRegistrationID(mActivity);
        MLogUtil.e(" 推送id   " + pushId);
    }

    @Override
    protected void setListener() {
        loginBtnLogin.setOnClickListener(clickListener);
        loginTvForgetPass.setOnClickListener(clickListener);
        loginTvRegist.setOnClickListener(clickListener);
        loginTvService.setOnClickListener(clickListener);
        loginTvSecrecy.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_login:
                    login();
                    MLogUtil.e("");
                    break;
                case R.id.login_tv_forgetPass:
                    IntentUtil.startActivity(mActivity, PhoneVerificationActivity.class, "forgetPass");
                    break;
                case R.id.login_tv_regist:
                    IntentUtil.startActivity(mActivity, PhoneVerificationActivity.class, "regist");
                    break;
                case R.id.login_tv_service:
                    IntentUtil.startActivity(mActivity, HtmlActivity.class, "service");
                    break;
                case R.id.login_tv_secrecy:
                    IntentUtil.startActivity(mActivity, HtmlActivity.class, "secrecy");
                    break;
            }
        }
    };

    // 登录
    private void login() {
        phone = loginEtPhone.getText().toString();
        pass = loginEtPass.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.login_phone));
        } else if (!MRegexUtil.isMobilePhoneNumber(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.phone_no));
        } else if (TextUtils.isEmpty(pass)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.login_pass));
        } else {
            showLoadDialog();
            HttpParams params = new HttpParams();
            params.put("phone", phone);
            if (pushId != null)
                params.put("pushId", pushId);
            params.put("phoneType", "0");
            params.put("pwd", MD5Util.getMD5UpperString(pass));
            params.put("mdstr", MD5Util.getMD5UpperString(pass));
            params.put("phoneVersion", AppInfoUtil.getVersionName(mActivity));
            params.put("phoneMac", AppInfoUtil.getImei());
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.loginUser)
                    .params(params)
                    .postRequest(new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            dismissLoadDialog();
                            if (!TextUtils.isEmpty(data)) {
                                UserEntity entity = MJsonUtil.gsonToBean(data, UserEntity.class);
                                if (entity != null) {
                                    entity.setPhone(phone);
                                    // 将登录用户信息放到单例中
                                    UserManager.getUserManager().setUserEntity(entity);
                                    // 缓存账号和密码
                                    PreferencesUtils.put(mActivity, PrefreConfig.loginPhone, phone);
                                    PreferencesUtils.put(mActivity, PrefreConfig.loginPass, pass);
                                    // 缓存登录状态
                                    PreferencesUtils.put(mActivity, PrefreConfig.loginStatus, true);
                                    // 将登录用户存到数据库中
//                                    userTable.closeDataBase();
                                    IntentUtil.startActivityFinish(mActivity, MTabActivity.class);
                                }
                            }
                        }

                        @Override
                        public void onRequestFailed(String error) {
                            dismissLoadDialog();
                            if (!TextUtils.isEmpty(error)) {
                                MToastUtil.showShortToast(mActivity,error);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGoBuilder.cancelTag(mActivity);
    }
}
