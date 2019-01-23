package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseEntity;
import com.bjzhijian.bluetoothseal.intelligentseal.model.DepartmentModel;

import java.util.List;

/**
 * Created by lenovo on 2019/1/17.
 * 部门实体类
 */

public class DepartmentEntity extends BaseEntity{

    private List<DepartmentModel> data;

    public List<DepartmentModel> getData() {
        return data;
    }

    public void setData(List<DepartmentModel> data) {
        this.data = data;
    }
}
