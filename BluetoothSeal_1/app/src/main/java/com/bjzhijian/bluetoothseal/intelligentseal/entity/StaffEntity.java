package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.model.StaffModel;

import java.util.List;

/**
 * Created by lenovo on 2019/1/17.
 * 员工实体类
 */

public class StaffEntity extends BaseEntity{

    private List<StaffModel> data;

    public List<StaffModel> getData() {
        return data;
    }

    public void setData(List<StaffModel> data) {
        this.data = data;
    }
}
