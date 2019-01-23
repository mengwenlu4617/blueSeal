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
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.DepartmentAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.DepartmentEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.DepartmentModel;
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
 * 部门列表
 */

public class DepartmentActivity extends BaseActivity {

    private Activity mActivity = DepartmentActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_layout)
    FrameLayout titleRightLayout;
    @BindView(R.id.department_listView)
    ListView departmentListView;
    @BindView(R.id.loadData_layoutView)
    LoadDataView loadDataLayoutView;
    private List<DepartmentModel> dataList;
    private DepartmentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_department_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(getString(R.string.department));
        titleRightImage.setImageResource(R.mipmap.icon_add);
        titleRightImage.setVisibility(View.VISIBLE);
        // 添加适配器
        dataList = new ArrayList<>();
        adapter = new DepartmentAdapter(mActivity, dataList);
        departmentListView.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(clickListener);
        titleRightLayout.setOnClickListener(clickListener);
        // item 点击
        departmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("departId",dataList.get(position).getId());
                bundle.putString("departName",dataList.get(position).getName());
                IntentUtil.startActivity(mActivity, DepartmentInfoActivity.class, bundle);
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
                    IntentUtil.startActivity(mActivity, DepartmentAddActivity.class);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    // 获取部门数据
    private void getData() {
        final String userPhone = UserManager.getUserManager().getUserPhone();
        if (TextUtils.isEmpty(userPhone)) {
            MLogUtil.e(" 手机号为空  ");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("phone", userPhone);
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.getDepartmentByPhone)
                    .postMD5Request(hashMap, new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            DepartmentEntity entity = MJsonUtil.gsonToBean(data, DepartmentEntity.class);
                            if (entity != null) {
                                dataList.clear();
                                List<DepartmentModel> models = entity.getData();
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
