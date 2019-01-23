package com.bjzhijian.bluetoothseal.intelligentseal.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/11/6.
 * 实体类
 */

public class BaseEntity implements Serializable {

    private int code;
    @SerializedName("m")
    private String message;
    private String method;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
