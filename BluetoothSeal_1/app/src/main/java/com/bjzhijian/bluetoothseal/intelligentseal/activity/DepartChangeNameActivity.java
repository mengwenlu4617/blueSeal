package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.DepartmentAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.DepartmentEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.DepartmentModel;
import com.bjzhijian.bluetoothseal.intelligentseal.utils.KeyBoardUtil;
import com.bjzhijian.bluetoothseal.intelligentseal.views.LoadDataView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MRegexUtil;
import com.fastwork.library.mutils.MToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 修改部门名称
 */

public class DepartChangeNameActivity extends BaseActivity {

    private Activity mActivity = DepartChangeNameActivity.this;

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
    private String departId = "", departName = "";

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
        titleRightImage.setImageResource(R.mipmap.icon_ok_sure);
        titleRightImage.setVisibility(View.VISIBLE);
        // 部门名称格式
        editEtInfo.setFilters(new InputFilter[]{MRegexUtil.inputFilter, new InputFilter.LengthFilter(20)});
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            departId = bundle.getString("departId");
            departName = bundle.getString("departName");
            if (departName != null) {
                titleCenterText.setText(departName);
                editEtInfo.setText(departName);
                editEtInfo.setSelection(departName.length());
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
            KeyBoardUtil.closeKeybord(editEtInfo, mActivity);
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.title_right_layout:
                    modifyName();
                    break;
            }
        }
    };

    // 修改部门名称
    private void modifyName() {
        String userPhone = UserManager.getUserManager().getUserPhone();
        departName = editEtInfo.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            MLogUtil.e(" 手机号为空  ");
        } else if (TextUtils.isEmpty(departName)) {
            MToastUtil.showShortToast(mActivity, "请输入部门名称");
        } else {
            showLoadDialog();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("phone", userPhone);
            hashMap.put("departId", departId); //
            hashMap.put("department", departName);//
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.replaceDepartmentName)
                    .postMD5Request(hashMap, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            dismissLoadDialog();
                            IntentUtil.startActivity(mActivity, DepartmentActivity.class);
                            finish();
                        }

                        @Override
                        public void onRequestFailed(String error) {
                            dismissLoadDialog();
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
