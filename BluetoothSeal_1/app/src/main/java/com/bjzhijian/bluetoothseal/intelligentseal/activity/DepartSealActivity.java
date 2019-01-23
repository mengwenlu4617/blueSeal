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
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.DepartSealAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.DepartmentAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.Constants;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.DepartmentEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.SealEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.DepartmentModel;
import com.bjzhijian.bluetoothseal.intelligentseal.model.SealModel;
import com.bjzhijian.bluetoothseal.intelligentseal.model.StaffModel;
import com.bjzhijian.bluetoothseal.intelligentseal.views.LoadDataView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.mutils.MLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/3.
 * 部门 -- 》  印章列表
 */

public class DepartSealActivity extends BaseActivity {

    private Activity mActivity = DepartSealActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    @BindView(R.id.depart_seal_listView)
    ListView departmentListView;
    @BindView(R.id.loadData_layoutView)
    LoadDataView loadDataLayoutView;
    private String departId = "";
    private int beginNum = 0;// 页数
    private List<SealModel> dataList;
    private DepartSealAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_depart_seal_layout;
    }

    @Override
    protected void initData() {
        titleRightImage.setVisibility(View.VISIBLE);
        // 获取部门id
        departId = getIntent().getStringExtra("data");
        if (TextUtils.isEmpty(departId)) {
            titleCenterText.setText(getString(R.string.import_seal));
            titleRightImage.setImageResource(R.mipmap.icon_import);
        } else {
            titleCenterText.setText(getString(R.string.manager_seal));
            titleRightImage.setImageResource(R.mipmap.icon_delete);
        }
        // 添加适配器
        dataList = new ArrayList<>();
        adapter = new DepartSealAdapter(mActivity, dataList);
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
                SealModel model = dataList.get(position);
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

                    break;
            }
        }
    };

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
                    .url(Urls.getDeviceByDepartment)
                    .postMD5Request(hashMap, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            SealEntity entity = MJsonUtil.gsonToBean(data, SealEntity.class);
                            if (entity != null) {
                                dataList.clear();
                                List<SealModel> models = entity.getData();
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
