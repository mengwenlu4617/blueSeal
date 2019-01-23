package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.RequestCallback;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.FileEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.OkGoBuilder;
import com.bjzhijian.bluetoothseal.intelligentseal.httpclient.Urls;
import com.bjzhijian.bluetoothseal.intelligentseal.imageloader.GlideImageLoader;
import com.bjzhijian.bluetoothseal.intelligentseal.manager.UserManager;
import com.bjzhijian.bluetoothseal.intelligentseal.model.FileModel;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MButton;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.AppManager;
import com.fastwork.library.mutils.DialogUIUtils;
import com.fastwork.library.mutils.IntentUtil;
import com.fastwork.library.mutils.MFilerUtil;
import com.fastwork.library.mutils.MGlideUtil;
import com.fastwork.library.mutils.MJsonUtil;
import com.fastwork.library.viewutil.widget.CircleImageView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.fastwork.library.mutils.MFilerUtil.cameraFileName;

/**
 * Created by lenovo on 2019/1/3.
 * 用户详情
 */

public class UserInfoActivity extends BaseActivity {

    private Activity mActivity = UserInfoActivity.this;

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.userInfo_img_photo)
    CircleImageView userInfoImgPhoto;
    @BindView(R.id.userInfo_layout_photo)
    LinearLayout userInfoLayoutPhoto;
    @BindView(R.id.userInfo_txt_name)
    MTextView userInfoTxtName;
    @BindView(R.id.userInfo_layout_name)
    LinearLayout userInfoLayoutName;
    @BindView(R.id.userInfo_txt_phone)
    MTextView userInfoTxtPhone;
    @BindView(R.id.userInfo_layout_phone)
    LinearLayout userInfoLayoutPhone;
    @BindView(R.id.userInfo_txt_email)
    MTextView userInfoTxtEmail;
    @BindView(R.id.userInfo_layout_email)
    LinearLayout userInfoLayoutEmail;
    @BindView(R.id.userInfo_layout_zxing)
    LinearLayout userInfoLayoutZxing;
    @BindView(R.id.userInfo_txt_company)
    MTextView userInfoTxtCompany;
    @BindView(R.id.userInfo_layout_company)
    LinearLayout userInfoLayoutCompany;
    @BindView(R.id.userInfo_txt_job)
    MTextView userInfoTxtJob;
    @BindView(R.id.userInfo_layout_job)
    LinearLayout userInfoLayoutJob;
    @BindView(R.id.userInfo_txt_jobNumber)
    MTextView userInfoTxtJobNumber;
    @BindView(R.id.userInfo_layout_jobNumber)
    LinearLayout userInfoLayoutJobNumber;
    @BindView(R.id.userInfo_btn_exitCompany)
    MButton userInfoBtnExitCompany;

    private UserEntity entity;
    private final int IMG_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_user_info_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(getString(R.string.info_title));

        entity = UserManager.getUserManager().getUserEntity();
        if (entity != null) {
            if (entity.getPhotoUrl() != null) {
                MGlideUtil.loadCircleImage(mActivity, Urls.IMAGE_URL_HOST + entity.getPhotoUrl(), userInfoImgPhoto);
            }
            if (entity.getName() != null) {
                userInfoTxtName.setText(entity.getName());
            }
            if (entity.getPhone() != null) {
                userInfoTxtPhone.setText(entity.getPhone());
            }
            if (entity.getEmail() != null) {
                userInfoTxtEmail.setText(entity.getEmail());
            }
            if (entity.getCompany() != null) {
                userInfoTxtCompany.setText(entity.getCompany());
            }
            if (entity.getJob() != null) {
                userInfoTxtJob.setText(entity.getJob());
            }
            if (entity.getJobNumber() != null) {
                userInfoTxtJobNumber.setText(entity.getJobNumber());
            }
            if (entity.getType() == 1) {
                userInfoLayoutZxing.setVisibility(View.VISIBLE);
                userInfoLayoutEmail.setVisibility(View.VISIBLE);
                userInfoLayoutCompany.setVisibility(View.VISIBLE);
                userInfoBtnExitCompany.setVisibility(View.GONE);
            } else {
                userInfoLayoutZxing.setVisibility(View.GONE);
                userInfoLayoutEmail.setVisibility(View.GONE);
                userInfoLayoutCompany.setVisibility(View.GONE);
                userInfoBtnExitCompany.setVisibility(View.VISIBLE);
            }
        }

        // 初始化拍照参数
        initPicker();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGoBuilder.cancelTag(mActivity);
    }

    @Override
    protected void setListener() {
        // back
        titleLeftLayout.setOnClickListener(clickListener);
        // 控件
        userInfoLayoutPhoto.setOnClickListener(clickListener);
        userInfoLayoutName.setOnClickListener(clickListener);
        userInfoLayoutPhone.setOnClickListener(clickListener);
        userInfoLayoutEmail.setOnClickListener(clickListener);
        userInfoLayoutZxing.setOnClickListener(clickListener);
        userInfoLayoutCompany.setOnClickListener(clickListener);
        userInfoLayoutJob.setOnClickListener(clickListener);
        userInfoLayoutJobNumber.setOnClickListener(clickListener);
        //
        userInfoBtnExitCompany.setOnClickListener(clickListener);
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.title_left_layout:
                    finishActivity();
                    break;
                case R.id.userInfo_layout_photo:
                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
                    startActivityForResult(intent, IMG_REQUEST_CODE);
                    break;
                case R.id.userInfo_layout_name:
                    IntentUtil.startActivity(mActivity, EditInfoActivity.class, "name");
                    break;
                case R.id.userInfo_layout_phone:
//                    MToastUtil.showShortToast(mActivity, "登录手机号不可编辑");
                    break;
                case R.id.userInfo_layout_email:
                    IntentUtil.startActivity(mActivity, EditInfoActivity.class, "email");
                    break;
                case R.id.userInfo_layout_zxing:

                    break;
                case R.id.userInfo_layout_company:
                    IntentUtil.startActivity(mActivity, EditInfoActivity.class, "company");
                    break;
                case R.id.userInfo_layout_job:
                    IntentUtil.startActivity(mActivity, EditInfoActivity.class, "job");
                    break;
                case R.id.userInfo_layout_jobNumber:
                    IntentUtil.startActivity(mActivity, EditInfoActivity.class, "jobNumber");
                    break;
                case R.id.userInfo_btn_exitCompany:
                    DialogUIUtils.showTwoButtonAlertDialog(mActivity, "退出公司", "确定退出该公司吗？",
                            "取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUIUtils.dismiss();
                                }
                            }, "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }, true);
                    break;
            }
        }
    };

    // 主动退出公司
    private void exitCompany() {
        HttpParams params = new HttpParams();
        params.put("personPhone", entity.getPhone());
        params.put("bossPhone", entity.getBossPhone());
        OkGoBuilder.getInstance()
                .builder(mActivity)
                .url(Urls.exitCompany)
                .params(params)
                .postRequest(new RequestCallback() {
                    @Override
                    public void onRequestSuccess(String data) {
                        IntentUtil.startActivity(mActivity,LoginActivity.class);
                        AppManager.getAppManager().exitApp();
                        
                    }

                    @Override
                    public void onRequestFailed(String error) {

                    }
                });
    }

    // 初始化相机相册
    private void initPicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setMultiMode(false);  //是否是多选
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        File cropCacheFolder = MFilerUtil.createFileDir(mActivity, cameraFileName);
        imagePicker.setCropCacheFolder(cropCacheFolder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case IMG_REQUEST_CODE:
                switch (resultCode) {
                    case ImagePicker.RESULT_CODE_ITEMS:
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        if (images != null && images.size() > 0) {
                            upPhoto(images.get(0).path);
                        }
                        break;
                }
                break;
        }
    }

    // 上传头像
    private void upPhoto(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            MGlideUtil.loadCircleFile(mActivity, imgFile, userInfoImgPhoto);
            HttpParams params = new HttpParams();
            params.put("photoUrl", imgFile);
            OkGoBuilder.getInstance()
                    .builder(mActivity)
                    .url(Urls.file_upload)
                    .params(params)
                    .upLoadRequest(new RequestCallback() {
                        @Override
                        public void onRequestSuccess(String data) {
                            if (!TextUtils.isEmpty(data)) {
                                FileEntity entity = MJsonUtil.gsonToBean(data, FileEntity.class);
                                if (entity != null) {
                                    List<FileModel> fileModels = entity.getJsonArray();
                                    if (fileModels != null && fileModels.size() > 0) {
                                        updateImg(fileModels.get(0).getFileUrl());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onRequestFailed(String error) {

                        }
                    });
        }
    }

    //上传头像url 更新头像
    private void updateImg(final String update_url) {
        HttpParams params = new HttpParams();
        params.put("phone", entity.getPhone());
        params.put("uploadRule", "1");
        params.put("photoUrl", update_url);
        OkGoBuilder.getInstance()
                .builder(mActivity)
                .url(Urls.updataPersonMessage)
                .params(params)
                .postRequest(new RequestCallback() {
                    @Override
                    public void onRequestSuccess(String data) {
                        entity.setPhotoUrl(update_url);
                        UserManager.getUserManager().setUserEntity(entity);
                    }

                    @Override
                    public void onRequestFailed(String error) {

                    }
                });
    }

}
