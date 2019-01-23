package com.bjzhijian.bluetoothseal.intelligentseal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/17.
 * 部门实体类
 */

public class DepartmentModel implements Serializable{

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
