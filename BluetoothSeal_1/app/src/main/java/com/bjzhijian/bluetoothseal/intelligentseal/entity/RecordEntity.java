package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.model.RecordModel;

import java.util.List;

/**
 * Created by lenovo on 2019/1/15.
 * 记录实体
 */

public class RecordEntity extends BaseEntity{

    private List<RecordModel> data;

    public List<RecordModel> getData() {
        return data;
    }

    public void setData(List<RecordModel> data) {
        this.data = data;
    }

}
