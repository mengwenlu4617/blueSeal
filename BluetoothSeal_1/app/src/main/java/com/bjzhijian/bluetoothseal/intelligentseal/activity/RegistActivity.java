package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MButton;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AppInfoUtil;
import com.fastwork.library.mutils.MD5Util;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MRegexUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.lzy.okgo.model.HttpParams;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by lenovo on 2019/1/3.
 * 注册
 */

public class RegistActivity extends BaseActivity {

    private Activity mActivity = RegistActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.regist_et_name)
    MEditText registEtName;
    @BindView(R.id.regist_et_pass1)
    MEditText registEtPass1;
    @BindView(R.id.regist_et_pass2)
    MEditText registEtPass2;
    @BindView(R.id.regist_btn_regist)
    MButton registBtnRegist;
    private String phone = "", pushId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_regist_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(R.string.regist);
        // getRegistrationID
        pushId = JPushInterface.getRegistrationID(mActivity);
        // 手机号
        phone = getIntent().getStringExtra("data");
        // 用户名只能输入汉字、字母、、数字
        registEtName.setFilters(new InputFilter[]{MRegexUtil.inputFilterCharAndNumber});
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        registBtnRegist.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.regist_btn_regist:
                    regist();
                    break;
            }
        }
    };

    // 注册
    private void regist() {
        String name = registEtName.getText().toString();
        String pass1 = registEtPass1.getText().toString();
        String pass2 = registEtPass2.getText().toString();
        if (TextUtils.isEmpty(name)) {
            MToastUtil.showShortToast(mActivity, R.string.regis_name);
        } else if (TextUtils.isEmpty(pass1)) {
            MToastUtil.showShortToast(mActivity, R.string.regis_pwd);
        } else if (!pass2.equals(pass1)) {
            MToastUtil.showShortToast(mActivity, R.string.pass_no_the_same_as);
        } else {
            showLoadDialog();
            HttpParams params = new HttpParams();
            if (phone != null)
                params.put("phone", phone);
            if (pushId != null) {
                params.put("pushId", pushId);
            }
            params.put("phoneType", "0");
            params.put("pwd", MD5Util.getMD5UpperString(pass1));
            params.put("phoneMac", AppInfoUtil.getImei());
            params.put("name", name);
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.registerUser)
                    .params(params)
                    .postRequest(new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            dismissLoadDialog();
                            finish();
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
