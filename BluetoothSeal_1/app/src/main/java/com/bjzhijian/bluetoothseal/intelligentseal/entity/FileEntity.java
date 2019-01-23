package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.bjzhijian.bluetoothseal.intelligentseal.model.FileModel;

import java.util.List;

/**
 * Created by lenovo on 2019/1/14.
 * 图片、文件的实体
 */

public class FileEntity {

//    {"jsonArray":[{"fileUrl":"/upfiles/image//2019/01/things15472009046964/1.jpg"}],"number":1}
    private List<FileModel> jsonArray;
    private int number;

    public List<FileModel> getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(List<FileModel> jsonArray) {
        this.jsonArray = jsonArray;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
