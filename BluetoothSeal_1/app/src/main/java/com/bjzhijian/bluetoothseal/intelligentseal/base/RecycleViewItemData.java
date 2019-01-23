package com.bjzhijian.bluetoothseal.intelligentseal.base;

/**
 * Created by lenovo on 2019/1/15.
 * 创建通用的item数据bean类
 * 我们需要一个通用的数据项RecycleViewItemData来方便我们使用和进行子项的类型判断。
 */

public class RecycleViewItemData {
    //用来装载不同类型的item数据bean
    private Object object;
    //item数据bean的类型
    private int dataType;

    public RecycleViewItemData() {
    }

    public RecycleViewItemData(Object object, int dataType) {
        this.object = object;
        this.dataType = dataType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}
