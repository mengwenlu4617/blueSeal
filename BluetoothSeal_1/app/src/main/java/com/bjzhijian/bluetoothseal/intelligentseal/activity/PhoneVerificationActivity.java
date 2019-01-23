package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MRegexUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.TimeCountUtil;
import com.lzy.okgo.model.HttpParams;

import butterknife.BindView;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

/**
 * Created by lenovo on 2019/1/3.
 * 手机验证
 */

public class PhoneVerificationActivity extends BaseActivity {

    private Activity mActivity = PhoneVerificationActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.verification_et_phone)
    MEditText verificationEtPhone;
    @BindView(R.id.verification_et_code)
    MEditText verificationEtCode;
    @BindView(R.id.verification_btn_sendCode)
    MButton verificationBtnSendCode;
    @BindView(R.id.verification_btn_next)
    MButton verificationBtnNext;
    @BindView(R.id.verification_tv_service)
    MTextView verificationTvService;
    @BindView(R.id.verification_tv_secrecy)
    MTextView verificationTvSecrecy;
    private TimeCountUtil countUtil;
    private String type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_phone_verification_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(R.string.phone_Verification);

        // 判断是忘记密码还是新用户注册
        type = getIntent().getStringExtra("data");

        verificationBtnSendCode.setBackgroundResource(R.drawable.butn_select_bg);
        verificationBtnSendCode.setEnabled(false);
        // 发送验证码控制
        countUtil = new TimeCountUtil(verificationBtnSendCode, 60000, 1000);

    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        // 输入框
        verificationEtPhone.addTextChangedListener(textWatcher);
        // 控件
        verificationBtnSendCode.setOnClickListener(clickListener);
        verificationBtnNext.setOnClickListener(clickListener);
        verificationTvService.setOnClickListener(clickListener);
        verificationTvSecrecy.setOnClickListener(clickListener);
    }

    // 输入框监听
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() < 11) {
                verificationBtnSendCode.setEnabled(false);
            } else {
                verificationBtnSendCode.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // 点击监听
    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.verification_btn_sendCode:
                    if ("forgetPass".equals(type)) {
                        getSmsCodeAsyn();
                    } else if ("regist".equals(type)) {
                        checkPhone();
                    }
                    break;
                case R.id.verification_btn_next:
                    checkSmsCodeAsyn();
                    break;
                case R.id.verification_tv_service:
                    IntentUtil.startActivity(mActivity, HtmlActivity.class, "service");
                    break;
                case R.id.verification_tv_secrecy:
                    IntentUtil.startActivity(mActivity, HtmlActivity.class, "secrecy");
                    break;
            }
        }
    };

    // 查询是否注册过 未注册则开始倒计时
    private void checkPhone() {
        String phone = verificationEtPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.regis_phone));
        } else if (!MRegexUtil.isMobilePhoneNumber(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.phone_no));
        } else {
            showLoadDialog();
            HttpParams params = new HttpParams();
            params.put("phone", phone);
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.registerCheck)
                    .params(params)
                    .postRequest(new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            dismissLoadDialog();
                            getSmsCodeAsyn();
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

    // 获取验证码
    public void getSmsCodeAsyn() {
        String phone = verificationEtPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.regis_phone));
        } else if (!MRegexUtil.isMobilePhoneNumber(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.phone_no));
        } else {
            if (countUtil != null) {
                countUtil.start();
            }
            SMSSDK.getInstance().getSmsCodeAsyn(phone, "1", new SmscodeListener() {
                @Override
                public void getCodeSuccess(final String uuid) {
                    // 发送短信成功
                    MLogUtil.e("getCodeSuccess");
                }

                @Override
                public void getCodeFail(int errCode, final String errmsg) {
                    // 发送短信失败
                    MLogUtil.e("getCodeFail");
                }
            });
        }
    }

    private void checkSmsCodeAsyn() {
        final String phone = verificationEtPhone.getText().toString();
        String code = verificationEtCode.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.regis_phone));
        } else if (!MRegexUtil.isMobilePhoneNumber(phone)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.phone_no));
        } else if (TextUtils.isEmpty(code)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.regist_code));
        } else {
            showLoadDialog();
            SMSSDK.getInstance().checkSmsCodeAsyn(phone, code, new SmscheckListener() {
                @Override
                public void checkCodeSuccess(String s) {
                    dismissLoadDialog();
                    if ("forgetPass".equals(type)) {
                        IntentUtil.startActivityFinish(mActivity, ReSetPassActivity.class, phone);
                    } else if ("regist".equals(type)) {
                        IntentUtil.startActivityFinish(mActivity, RegistActivity.class, phone);
                    }
                }

                @Override
                public void checkCodeFail(int i, String s) {
                    dismissLoadDialog();
                    switch (i) {
                        case 2997:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_0));
                            break;
                        case 2998:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_1));
                            break;
                        case 3001:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_2));
                            break;
                        case 3002:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_3));
                            break;
                        case 4011:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_4));
                            break;
                        case 4015:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_5));
                            break;
                        case 4017:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_6));
                            break;
                        case 2996:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_7));
                            break;
                        case 2993:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_8));
                            break;
                        case 4018:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_9));
                            break;
                        default:
                            MToastUtil.showShortToast(mActivity, getString(R.string.sms_err_10));
                            break;
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
