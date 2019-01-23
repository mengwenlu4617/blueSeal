package com.bjzhijian.bluetoothseal.intelligentseal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.DepartmentActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.HomeAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.base.RecycleViewItemData;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.HomeAdapterListener;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.Constants;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.RecordEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.RecordModel;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.viewutil.widget.MyRecycleView;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/12/27.
 * 功能
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_recyclerView)
    MyRecycleView homeRecyclerView;
    @BindView(R.id.home_refreshLayout)
    SmartRefreshLayout homeRefreshLayout;
    private List<RecordModel> recordList;
    private List<RecycleViewItemData> dataList;
    private HomeAdapter adapter;
    private int pageNum = 0;
    private Unbinder unbinder;
    @SuppressLint("StaticFieldLeak")
    private static HomeFragment instance = null;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        dataList = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), dataList);
        homeRecyclerView.setAdapter(adapter);

        homeRefreshLayout.autoRefresh();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeRefreshLayout.finishRefresh();
                initData();
            }
        }, 500);
        setListener();
    }

    // 设置监听
    private void setListener() {
        adapter.setAdapterListener(new HomeAdapterListener() {
            @Override
            public void bannerClick(int position) {

            }

            @Override
            public void channelClick(int viewId) {
                switch (viewId) {
                    case R.id.home_header_channel_view1:

                        break;
                    case R.id.home_header_channel_view2:

                        break;
                    case R.id.home_header_channel_view3:

                        break;
                    case R.id.home_header_channel_view4:
                        IntentUtil.startActivity(getActivity(), DepartmentActivity.class);
                        break;
                    case R.id.home_header_channel_view5:

                        break;
                    case R.id.home_header_channel_view6:

                        break;
                    case R.id.home_header_channel_view7:

                        break;
                    case R.id.home_header_channel_view8:

                        break;
                }
            }

            @Override
            public void itemClick(int position) {

            }
        });
    }

    // 请求数据
    private void initData() {
        dataList.clear();
        dataList.add(new RecycleViewItemData("image", 0));
        dataList.add(new RecycleViewItemData("buttom", 1));
        UserEntity entity = UserManager.getUserManager().getUserEntity();
        if (entity == null) {
            adapter.notifyDataSetChanged();
        } else {
            String url;
            if (entity.getType() == 0) {
                // 员工从第  1   页  查询自己的记录
                url = Urls.person;
                if (pageNum == 0)
                    pageNum++;
            } else {
                // 管理员和boss 从第  0  页  查询记录
                url = Urls.showAllDeviceRecord;
            }
            HttpParams httpParams = new HttpParams();
            httpParams.put("personPhone", entity.getPhone());
            httpParams.put("bossPhone", entity.getBossPhone());
            httpParams.put("pageNum", pageNum + "");
            httpParams.put("dataNum", Constants.pagerNum);
            httpParams.put("isNewRequest", "1");
            OkGoBuilder.getInstance()
                    .builder(getActivity())
                    .url(url)
                    .params(httpParams)
                    .postRequest(new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            dismissLoadDialog();
                            RecordEntity entity = MJsonUtil.gsonToBean(data, RecordEntity.class);
                            if (entity != null) {
                                recordList = entity.getData();
                                if (recordList != null && recordList.size() > 0) {
                                    for (RecordModel model : recordList) {
                                        dataList.add(new RecycleViewItemData(model, 2));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onRequestFailed(String error) {
                            dismissLoadDialog();
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        OkGoBuilder.cancelTag(getActivity());
    }
}
