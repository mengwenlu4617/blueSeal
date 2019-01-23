package com.bjzhijian.bluetoothseal.intelligentseal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.AboutAppActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.AboutSealActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.GuideActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.LoginActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.ModifyPassActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.activity.UserInfoActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.adapter.MyAdapter;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseFragment;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.config.PrefreConfig;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.MineItemModel;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MListView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AppInfoUtil;
import com.fastwork.library.mutils.DialogUIUtils;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MD5Util;
import com.fastwork.library.mutils.MGlideUtil;
import com.fastwork.library.mutils.MToastUtil;
import com.fastwork.library.mutils.PreferencesUtils;
import com.fastwork.library.viewutil.widget.CircleImageView;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/12/27.
 * 我的
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.my_ListView)
    MListView myListView;
    @BindView(R.id.my_img_photo)
    CircleImageView myImgPhoto;
    @BindView(R.id.my_txt_name)
    MTextView myTxtName;
    @BindView(R.id.my_txt_job)
    MTextView myTxtJob;
    @BindView(R.id.my_txt_company)
    MTextView myTxtCompany;
    @BindView(R.id.my_img_ZxingCode)
    ImageView myImgZxingCode;
    @BindView(R.id.my_layout_userInfo)
    LinearLayout myLayoutUserInfo;
    private Unbinder unbinder;
    private List<MineItemModel> itemModels;

    @SuppressLint("StaticFieldLeak")
    private static MineFragment instance = null;

    public static MineFragment getInstance() {
        if (instance == null) {
            instance = new MineFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        itemModels = new ArrayList<>();
        itemModels.add(new MineItemModel(R.mipmap.icon_use_record, getString(R.string.txt_use_record), GuideActivity.class, false, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_apply_progress, getString(R.string.txt_apply_progress), GuideActivity.class, false, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_makecopy_my, getString(R.string.txt_makecopy_my), GuideActivity.class, false, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_nouse_list, getString(R.string.txt_nouse_record), GuideActivity.class, true, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_modify_pass, getString(R.string.txt_modify_pass), ModifyPassActivity.class, false, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_about_seal, getString(R.string.txt_about_device), AboutSealActivity.class, false, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_about_app, getString(R.string.txt_about_app), AboutAppActivity.class, true, true));
        itemModels.add(new MineItemModel(R.mipmap.icon_exit_logout, getString(R.string.txt_exit_logout), GuideActivity.class, false, false));

        myListView.setAdapter(new MyAdapter(getActivity(), itemModels));

        setListener();
    }

    private void setListener() {
        myLayoutUserInfo.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                IntentUtil.startActivity(getActivity(), UserInfoActivity.class);
            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == itemModels.size() - 1) {
                    DialogUIUtils.showTwoButtonAlertDialog(getActivity(), "提示", "确定退出登录吗？",
                            "取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    DialogUIUtils.dismiss();
                                }
                            }, "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    logOut();
                                }
                            }, true);
                } else {
                    IntentUtil.startActivity(getActivity(), itemModels.get(position).getaClass());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        UserEntity entity = UserManager.getUserManager().getUserEntity();
        if (entity != null) {
            if (entity.getPhotoUrl() != null) {
                MGlideUtil.loadCircleImage(getActivity(), Urls.IMAGE_URL_HOST + entity.getPhotoUrl(), myImgPhoto);
            }
            if (entity.getName() != null) {
                myTxtName.setText(entity.getName());
            }
            if (TextUtils.isEmpty(entity.getJob())) {
                switch (entity.getType()) {
                    // 0=员工 1=老板 2=管理员
                    case 0:
                        myTxtJob.setText("身份：员工");
                        break;
                    case 1:
                        myTxtJob.setText("身份：老板");
                        break;
                    case 2:
                        myTxtJob.setText("身份：印章管理员");
                        break;
                }
            } else {
                myTxtJob.setText(getString(R.string.job) + entity.getJob());
            }
            if (entity.getCompany() != null) {
                myTxtCompany.setText(entity.getCompany());
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    // 退出登录； 目的将 pushid 置空
    private void logOut() {
        // 获取缓存的手机号和密码
        String phone = (String) PreferencesUtils.get(getActivity(), PrefreConfig.loginPhone, "");
        String pass = (String) PreferencesUtils.get(getActivity(), PrefreConfig.loginPass, "");
        HttpParams params = new HttpParams();
        if (phone != null) {
            params.put("phone", phone);
        }
        params.put("pushId", "");
        params.put("phoneType", "0");
        if (pass != null) {
            params.put("pwd", MD5Util.getMD5UpperString(pass));
            params.put("mdstr", MD5Util.getMD5UpperString(pass));
        }
        params.put("phoneVersion", AppInfoUtil.getVersionName(getActivity()));
        params.put("phoneMac", AppInfoUtil.getImei());
        OkGoBuilder.getInstance()
                .builder(getActivity())
                .url(Urls.loginUser)
                .params(params)
                .postRequest(new RequestCallback() {
                    @Override
                    public void onRequestSuccess(String data) {
                        // 将登录用户置空
                        UserManager.getUserManager().setUserEntity(new UserEntity());
                        PreferencesUtils.put(getActivity(), PrefreConfig.loginStatus, false);
                        IntentUtil.startActivityFinish(getActivity(), LoginActivity.class);
                    }

                    @Override
                    public void onRequestFailed(String error) {
                        if (!TextUtils.isEmpty(error)) {
                            MToastUtil.showShortToast(getActivity(), error);
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkGoBuilder.cancelTag(getActivity());
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
