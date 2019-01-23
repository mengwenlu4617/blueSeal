package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.DepartStaffAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.DepartmentAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.Constants;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.DepartmentEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.StaffEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.DepartmentModel;
import com.bjzhijian.bluetoothseal.intelligentseal.model.StaffModel;
import com.bjzhijian.bluetoothseal.intelligentseal.views.LoadDataView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 部门 -- 》  员工列表
 */

public class DepartStaffActivity extends BaseActivity {

    private Activity mActivity = DepartStaffActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    @BindView(R.id.depart_staff_listView)
    ListView departmentListView;
    @BindView(R.id.loadData_layoutView)
    LoadDataView loadDataLayoutView;
    private String departId = "", type = "delete";// type : delete 批量删除；import 批量导入
    private int beginNum = 0;// 页数
    private List<StaffModel> dataList;
    private DepartStaffAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_depart_staff_layout;
    }

    @Override
    protected void initData() {
        titleRightImage.setVisibility(View.VISIBLE);
        // 获取部门id
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            departId = bundle.getString("departId");
            type = bundle.getString("type");
        }
        if (TextUtils.isEmpty(departId)) {
            titleCenterText.setText(getString(R.string.import_staff));
            titleRightImage.setImageResource(R.mipmap.icon_import);
        } else {
            if ("import".equals(type)) {
                titleCenterText.setText(getString(R.string.import_staff));
                titleRightImage.setImageResource(R.mipmap.icon_import);
            } else {
                titleCenterText.setText(getString(R.string.manager_staff));
                titleRightImage.setImageResource(R.mipmap.icon_delete);
            }
        }
        // 添加适配器
        dataList = new ArrayList<>();
        adapter = new DepartStaffAdapter(mActivity, dataList);
        departmentListView.setAdapter(adapter);

        getData();
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        titleRightLayout.setOnClickListener(clickListener);
        // item 点击
        departmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StaffModel model = dataList.get(position);
                if (model.isSelect()) {
                    dataList.get(position).setSelect(false);
                } else {
                    dataList.get(position).setSelect(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.title_right_layout:
                    if (TextUtils.isEmpty(departId)) {
                        getSelectData();
                    } else {
                        if ("delete".equals(type)) {
                            // 批量删除
                            deletePersons();
                        } else if ("import".equals(type)) {
                            // 批量导入
                            importPersons();
                        }
                    }
                    break;
            }
        }
    };

    // 批量导入员工
    private void importPersons() {
        final List<StaffModel> selectData = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isSelect()) {
                selectData.add(dataList.get(i));
            }
        }
        StringBuilder strStaff = new StringBuilder();
        if (selectData.size() > 0) {
            for (int i = 0; i < selectData.size(); i++) {
                strStaff.append(selectData.get(i).getPhone()).append(",");
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("departId", departId);
            hashMap.put("phone", UserManager.getUserManager().getUserPhone());
            hashMap.put("phones", strStaff.toString().substring(0, (strStaff.toString().length() - 1)));
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.insertPersonToDepartment)
                    .postMD5Request(hashMap, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            MToastUtil.showShortToast(mActivity, "导入员工成功");
                            dataList.removeAll(selectData);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onRequestFailed(String error) {

                        }
                    });
        }
    }

    // 批量删除员工
    private void deletePersons() {
        final List<StaffModel> selectData = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isSelect()) {
                selectData.add(dataList.get(i));
            }
        }
        StringBuilder strStaff = new StringBuilder();
        if (selectData.size() > 0) {
            for (int i = 0; i < selectData.size(); i++) {
                strStaff.append(selectData.get(i).getPhone()).append(",");
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("departId", departId);
            hashMap.put("phone", UserManager.getUserManager().getUserPhone());
            hashMap.put("phones", strStaff.toString().substring(0, (strStaff.toString().length() - 1)));
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.updatePersonDepartment)
                    .postMD5Request(hashMap, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            MToastUtil.showShortToast(mActivity, "导入员工成功");
                            dataList.removeAll(selectData);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onRequestFailed(String error) {

                        }
                    });
        }
    }

    // 获取选中的条目
    private void getSelectData() {
        List<StaffModel> selectData = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isSelect()) {
                selectData.add(dataList.get(i));
            }
        }
        StringBuilder strStaff = new StringBuilder();
        for (int i = 0; i < selectData.size(); i++) {
            strStaff.append(selectData.get(i).getPhone()).append(",");
        }
        MLogUtil.e(" strStaff    11111111  " + strStaff.toString());
        MLogUtil.e(" strStaff    22222222  " + strStaff.toString().substring(0, (strStaff.toString().length() - 1)));
        String strGson = "";
        if (selectData.size() > 0) {
            Gson gson = new Gson();
            strGson = gson.toJson(selectData);
        }
        MLogUtil.e(" strGson  " + strGson);
    }

    // 获取部门数据
    private void getData() {
        final String userPhone = UserManager.getUserManager().getUserPhone();
        if (TextUtils.isEmpty(userPhone)) {
            MLogUtil.e(" 手机号为空  ");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            if (!TextUtils.isEmpty(departId)) {
                hashMap.put("departId", departId);
            }
            hashMap.put("phone", userPhone);
            hashMap.put("beginNum", beginNum);
            hashMap.put("dataNum", Constants.pagerNum);
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.getPersonByDepartment)
                    .postMD5Request(hashMap, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            StaffEntity entity = MJsonUtil.gsonToBean(data, StaffEntity.class);
                            if (entity != null) {
                                dataList.clear();
                                List<StaffModel> models = entity.getData();
                                if (models != null && models.size() > 0) {
                                    dataList.addAll(models);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onRequestFailed(String error) {

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
