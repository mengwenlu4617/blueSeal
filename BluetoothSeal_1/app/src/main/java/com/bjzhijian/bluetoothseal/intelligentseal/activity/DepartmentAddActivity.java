package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MEditText;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MRegexUtil;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 添加部门
 */

public class DepartmentAddActivity extends BaseActivity {

    private Activity mActivity = DepartmentAddActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    @BindView(R.id.add_department_etName)
    MEditText addDepartmentEtName;
//    @BindView(R.id.add_department_tvStaff)
//    MTextView addDepartmentTvStaff;
    @BindView(R.id.add_department_llStaff)
    LinearLayout addDepartmentLlStaff;
//    @BindView(R.id.add_department_tvSeal)
//    MTextView addDepartmentTvSeal;
    @BindView(R.id.add_department_llSeal)
    LinearLayout addDepartmentLlSeal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_depart_add_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(getString(R.string.department_add));
        titleRightImage.setImageResource(R.mipmap.icon_ok_sure);
        titleRightImage.setVisibility(View.VISIBLE);
        // 部门名称格式
        addDepartmentEtName.setFilters(new InputFilter[]{MRegexUtil.inputFilter, new InputFilter.LengthFilter(20)});

    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        titleRightLayout.setOnClickListener(clickListener);
        addDepartmentLlStaff.setOnClickListener(clickListener);
        addDepartmentLlSeal.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.title_right_layout:
                    addDepartment();
                    break;
                case R.id.add_department_llStaff:
                    IntentUtil.startActivity(mActivity,DepartStaffActivity.class);
                    break;
                case R.id.add_department_llSeal:
                    IntentUtil.startActivity(mActivity,DepartSealActivity.class);
                    break;
            }
        }
    };

    // 添加部门
    private void addDepartment(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGoBuilder.cancelTag(mActivity);
    }
}
