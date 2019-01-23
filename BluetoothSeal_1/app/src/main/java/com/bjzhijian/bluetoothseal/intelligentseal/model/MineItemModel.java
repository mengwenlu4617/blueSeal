package com.bjzhijian.bluetoothseal.intelligentseal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/2.
 * 我的item 数据
 */

public class MineItemModel implements Serializable {

    private int imgId;// 图片id
    private String text; // item名称
    private Class<?> aClass; // 类名
    private boolean isGroup; // 是否显示分组线
    private boolean isRightImg; // 是否显示右边图片

    public MineItemModel(int imgId, String text, Class<?> aClass, boolean isGroup, boolean isRightImg) {
        this.imgId = imgId;
        this.text = text;
        this.aClass = aClass;
        this.isGroup = isGroup;
        this.isRightImg = isRightImg;
    }

    public int getImgId() {
        return imgId;
    }

    public String getText() {
        return text;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public boolean isRightImg() {
        return isRightImg;
    }
}
