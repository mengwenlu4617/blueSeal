package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.viewutil.alertdialog.MActionSheetDialog;
import com.fastwork.library.viewutil.alertdialog.MActionSheetDialog.SheetItemColor;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 部门详情
 */

public class DepartmentInfoActivity extends BaseActivity {

    private Activity mActivity = DepartmentInfoActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    @BindView(R.id.info_department_etName)
    MTextView infoDepartmentEtName;
    //    @BindView(R.id.info_department_tvStaff)
//    MTextView infoDepartmentTvStaff;
    @BindView(R.id.info_department_llStaff)
    LinearLayout infoDepartmentLlStaff;
    //    @BindView(R.id.info_department_tvSeal)
//    MTextView infoDepartmentTvSeal;
    @BindView(R.id.info_department_llSeal)
    LinearLayout infoDepartmentLlSeal;
    private String departId = "", departName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_depart_info_layout;
    }

    @Override
    protected void initData() {
        titleRightImage.setImageResource(R.mipmap.icon_import);
        titleRightImage.setVisibility(View.VISIBLE);
        //
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            departId = bundle.getString("departId");
            departName = bundle.getString("departName");
            if (departName != null) {
                titleCenterText.setText(departName);
                infoDepartmentEtName.setText(departName);
            } else {
                titleCenterText.setText(getString(R.string.department_info));
            }
        }

    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        titleRightLayout.setOnClickListener(clickListener);
        infoDepartmentEtName.setOnClickListener(clickListener);
        infoDepartmentLlStaff.setOnClickListener(clickListener);
        infoDepartmentLlSeal.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.title_right_layout:
                    new MActionSheetDialog(mActivity)
                            .builder()
                            .setCancelable(true)
                            .setCanceledOnTouchOutside(true)
                            .setTitle(getString(R.string.import_txt))
                            .addSheetItem(getString(R.string.import_staff), SheetItemColor.Blue,
                                    new MActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("departId", departId);
                                            bundle.putString("type", "import");
                                            IntentUtil.startActivity(mActivity, DepartStaffActivity.class, bundle);
                                        }
                                    })
                            .addSheetItem(getString(R.string.import_seal), SheetItemColor.Blue,
                                    new MActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("departId", departId);
                                            bundle.putString("type", "import");
                                            IntentUtil.startActivity(mActivity, DepartSealActivity.class, bundle);
                                        }
                                    })
                            .show();
                    break;
                case R.id.info_department_etName:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("departId", departId);
                    bundle1.putString("departName", departName);
                    IntentUtil.startActivity(mActivity, DepartChangeNameActivity.class, bundle1);
                    break;
                case R.id.info_department_llStaff:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("departId", departId);
                    bundle2.putString("type", "delete");
                    IntentUtil.startActivity(mActivity, DepartStaffActivity.class, bundle2);
                    break;
                case R.id.info_department_llSeal:
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("departId", departId);
                    bundle3.putString("type", "delete");
                    IntentUtil.startActivity(mActivity, DepartSealActivity.class, bundle3);
                    break;
            }
        }
    };

}
