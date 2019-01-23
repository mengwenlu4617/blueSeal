package com.bjzhijian.bluetoothseal.intelligentseal.manager;

import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;

/**
 * Created by lenovo on 2019/1/9.
 * 登录用户的信息（单例）
 */

public class UserManager {

    private static UserManager userManager = null;
    private UserEntity userEntity;

    public static UserManager getUserManager() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }

    private UserManager() {
        userEntity = new UserEntity();
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getBossPhone() {
        if (userEntity != null) {
            return userEntity.getPhone();
        }
        return "";
    }

    public String getUserPhone() {
        if (userEntity != null) {
            return userEntity.getPhone();
        }
        return "";
    }
}
