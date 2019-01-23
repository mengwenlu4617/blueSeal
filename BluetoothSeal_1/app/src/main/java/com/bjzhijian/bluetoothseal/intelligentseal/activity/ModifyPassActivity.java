package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.PrefreConfig;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MButton;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AesUtil;
import com.fastwork.library.mutils.MD5Util;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.PreferencesUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 修改密码
 */

public class ModifyPassActivity extends BaseActivity {

    private Activity mActivity = ModifyPassActivity.this;

    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.modify_oldPass_View)
    LinearLayout modifyOldPassView;
    @BindView(R.id.reset_pass_et_oldPass)
    MEditText resetPassEtOldPass;
    @BindView(R.id.reset_pass_et_newPass1)
    MEditText resetPassEtNewPass1;
    @BindView(R.id.reset_pass_et_newPass2)
    MEditText resetPassEtNewPass2;
    @BindView(R.id.reset_pass_btn_ok)
    MButton resetPassBtnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_reset_pass_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(R.string.txt_modify_pass);
        // 显示旧密码输入框
        modifyOldPassView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        resetPassBtnOk.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.reset_pass_btn_ok:
                    modifyPass();
                    break;
            }
        }
    };

    // 修改密码
    private void modifyPass() {
        String oldPass = resetPassEtOldPass.getText().toString();
        final String newPass1 = resetPassEtNewPass1.getText().toString();
        String newPass2 = resetPassEtNewPass2.getText().toString();
        if (TextUtils.isEmpty(oldPass)) {
            MToastUtil.showShortToast(mActivity, R.string.regis_pwd);
        } else if (TextUtils.isEmpty(newPass1)) {
            MToastUtil.showShortToast(mActivity, R.string.new_pwd);
        } else if (newPass1.equals(newPass2)) {
            MToastUtil.showShortToast(mActivity, R.string.pass_no_the_same_as);
        } else {
            showLoadDialog();
            HashMap<String, Object> map = new HashMap<>();
            map.put("oldPwd", MD5Util.getMD5UpperString(newPass1));
            map.put("newPwd", MD5Util.getMD5UpperString(newPass2));
            map.put("phone", UserManager.getUserManager().getUserPhone());
            String data = MJsonUtil.hashMapToJson(map);
            try {
                data = AesUtil.encrypt(data, AesUtil.KEY, AesUtil.IV);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpParams params = new HttpParams();
            if (data != null)
                params.put("data", data);
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.updatePwd)
                    .params(params)
                    .postRequest(new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            dismissLoadDialog();
                            PreferencesUtils.put(mActivity, PrefreConfig.loginPass, newPass1);
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
