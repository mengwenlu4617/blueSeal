package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.utils.KeyBoardUtil;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MRegexUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 修改（编辑 ）用户信息
 */

public class EditInfoActivity extends BaseActivity {

    private Activity mActivity = EditInfoActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    @BindView(R.id.edit_et_info)
    MEditText editEtInfo;
    private String type = "";
    private UserEntity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_editinfo_layout;
    }

    @Override
    protected void initData() {
        titleRightImage.setVisibility(View.VISIBLE);
        titleRightImage.setImageResource(R.mipmap.icon_ok_sure);
        titleCenterText.setText(getString(R.string.edit_info_title));
        // 打开软键盘
        editEtInfo.setFocusable(true);
        KeyBoardUtil.openKeybord(editEtInfo, mActivity);
        // 编辑类型
        type = getIntent().getStringExtra("data");

        initInputType();
    }

    private void initInputType() {
        entity = UserManager.getUserManager().getUserEntity();
        if (entity == null) {
            finishActivity();
        } else {
            switch (type) {
                case "name":
                    editEtInfo.setFilters(new InputFilter[]{MRegexUtil.inputFilterCharAndNumber,
                            new InputFilter.LengthFilter(4)});
                    if (!TextUtils.isEmpty(entity.getName())) {
                        editEtInfo.setText(entity.getName());
                        editEtInfo.setSelection(entity.getName().length());
                    }
                    break;
//                case "phone":
//                    editEtInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    editEtInfo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
//                    if (!TextUtils.isEmpty(entity.getPhone())) {
//                        editEtInfo.setText(entity.getPhone());
//                        editEtInfo.setSelection(entity.getPhone().length());
//                    }
//                    break;
                case "email":
                    editEtInfo.setFilters(new InputFilter[]{MRegexUtil.inputFilter,
                            new InputFilter.LengthFilter(30)});
                    if (!TextUtils.isEmpty(entity.getEmail())) {
                        editEtInfo.setText(entity.getEmail());
                        editEtInfo.setSelection(entity.getEmail().length());
                    }
                    break;
                case "company":
                    editEtInfo.setFilters(new InputFilter[]{MRegexUtil.inputFilter,
                            new InputFilter.LengthFilter(30)});
                    if (!TextUtils.isEmpty(entity.getCompany())) {
                        editEtInfo.setText(entity.getCompany());
                        editEtInfo.setSelection(entity.getCompany().length());
                    }
                    break;
                case "job":
                    editEtInfo.setFilters(new InputFilter[]{MRegexUtil.inputFilter,
                            new InputFilter.LengthFilter(16)});
                    if (!TextUtils.isEmpty(entity.getJob())) {
                        editEtInfo.setText(entity.getJob());
                        editEtInfo.setSelection(entity.getJob().length());
                    }
                    break;
                case "jobNumber":
                    editEtInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editEtInfo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    if (!TextUtils.isEmpty(entity.getJobNumber())) {
                        editEtInfo.setText(entity.getJobNumber());
                        editEtInfo.setSelection(entity.getJobNumber().length());
                    }
                    break;
            }
        }
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        titleRightLayout.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    KeyBoardUtil.closeKeybord(editEtInfo, mActivity);
                    finishActivity();
                    break;
                case R.id.title_right_layout:
                    KeyBoardUtil.closeKeybord(editEtInfo, mActivity);
                    if (type.equals("email")) {
                        editEmail();
                    } else {
                        String content = editEtInfo.getText().toString();
                        switch (type) {
                            case "name":
                                if (TextUtils.isEmpty(content)) {
                                    MToastUtil.showShortToast(mActivity, getString(R.string.regis_name));
                                } else {
                                    entity.setName(content);
                                    editEtInfo();
                                }
                                break;
//                            case "phone":
//                                if (TextUtils.isEmpty(content)) {
//                                    MToastUtil.showShortToast(mActivity, getString(R.string.regis_phone));
//                                } else if (!MRegexUtil.isMobilePhoneNumber(content)) {
//                                    MToastUtil.showShortToast(mActivity, getString(R.string.phone_no));
//                                } else {
//                                    entity.setPhone(content);
//                                    editEtInfo();
//                                }
//                                break;
                            case "company":
                                entity.setCompany(content);
                                editEtInfo();
                                break;
                            case "job":
                                entity.setJob(content);
                                editEtInfo();
                                break;
                            case "jobNumber":
                                entity.setJobNumber(content);
                                editEtInfo();
                                break;
                        }
                    }
                    break;
            }
        }
    };

    // 编辑信息
    private void editEtInfo() {
        HttpParams params = new HttpParams();
        params.put("phone", entity.getPhone());
        params.put("name", entity.getName());
        params.put("company", entity.getCompany());
        params.put("job", entity.getJob());
        params.put("jobNumber", entity.getJobNumber());
        OkGoBuilder.getInstance()
                .builder(mActivity)
                .url(Urls.updataPersonMessage)
                .params(params)
                .postRequest(new RequestCallback() {
                    @Override
                    public void onRequestSuccess(String data) {
                        UserManager.getUserManager().setUserEntity(entity);
                        finishActivity();
                    }

                    @Override
                    public void onRequestFailed(String error) {
                        MLogUtil.e("" + error);
                    }
                });
    }

    // 编辑邮箱
    private void editEmail() {
        String email = editEtInfo.getText().toString();
        if (TextUtils.isEmpty(email)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.regis_email));
        } else if (!MRegexUtil.isEmail(email)) {
            MToastUtil.showShortToast(mActivity, getString(R.string.email_no));
        } else {
            entity.setEmail(email);
            final HashMap<String, Object> map = new HashMap<>();
            map.put("phone", entity.getPhone());
            map.put("email", entity.getEmail());
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.email)
                    .postMD5Request(map, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            UserManager.getUserManager().setUserEntity(entity);
                            finishActivity();
                        }

                        @Override
                        public void onRequestFailed(String error) {
                            MLogUtil.e("" + error);
                        }
                    });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            KeyBoardUtil.closeKeybord(editEtInfo, mActivity);
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGoBuilder.cancelTag(mActivity);
    }
}
