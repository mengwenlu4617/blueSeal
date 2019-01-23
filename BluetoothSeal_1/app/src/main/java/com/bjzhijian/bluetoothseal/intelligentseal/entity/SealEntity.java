package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.model.SealModel;

import java.util.List;

/**
 * Created by lenovo on 2019/1/17.
 * 印章实体类
 */

public class SealEntity extends BaseEntity{

    private List<SealModel> data;

    public List<SealModel> getData() {
        return data;
    }

    public void setData(List<SealModel> data) {
        this.data = data;
    }
}
