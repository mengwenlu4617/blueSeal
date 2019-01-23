package com.bjzhijian.bluetoothseal.intelligentseal.entity;

import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2019/1/9.
 * 用户的实体类
 */

public class UserEntity extends BaseEntity{

    @SerializedName(value = "name",alternate = {"admin"})
    private String name;// 用户名
    @SerializedName(value = "phone",alternate = {"userPhone"})
    private String phone;// 用户手机号
    private String bossPhone;// boss手机号
    @SerializedName(value = "company",alternate = {"companyName"})
    private String company;// 公司
    private String email;// 邮箱
    private String job;// 职位
    private String jobNumber;// 工号
    private String photoUrl;// 头像
    private String departId;// 部门id
    private String departName;// 所属部门
    @SerializedName(value = "type",alternate = {"powerType"})
    private int type;// 用户身份   0=员工 1=老板 2=管理员
    private String androidVersion;// app版本号
    private int applyPeopleNo;// 申请加入公司的人数
    private int auditing;// 审批的人数
    private String macVersion;// 设备的版本
    private String newVersion;// 设备的最新版本
    private String phoneMac;// 登录设备的唯一标志

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

    public String getBossPhone() {
        return bossPhone;
    }

    public void setBossPhone(String bossPhone) {
        this.bossPhone = bossPhone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public int getApplyPeopleNo() {
        return applyPeopleNo;
    }

    public void setApplyPeopleNo(int applyPeopleNo) {
        this.applyPeopleNo = applyPeopleNo;
    }

    public int getAuditing() {
        return auditing;
    }

    public void setAuditing(int auditing) {
        this.auditing = auditing;
    }

    public String getMacVersion() {
        return macVersion;
    }

    public void setMacVersion(String macVersion) {
        this.macVersion = macVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getPhoneMac() {
        return phoneMac;
    }

    public void setPhoneMac(String phoneMac) {
        this.phoneMac = phoneMac;
    }
}
