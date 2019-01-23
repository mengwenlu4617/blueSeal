package com.bjzhijian.bluetoothseal.intelligentseal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/17.
 * 员工实体类
 */

public class StaffModel implements Serializable{

    private String job;
    private String jobNumber;
    private String name;
    private String phone;
    private String photo;
    private boolean isSelect;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
