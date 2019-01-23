package com.bjzhijian.bluetoothseal.intelligentseal.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bjzhijian.bluetoothseal.intelligentseal.db.helper.DbOpenHelper;
import com.bjzhijian.bluetoothseal.intelligentseal.entity.UserEntity;

/**
 * Created by lenovo on 2018/12/25.
 * 用户表
 */

public class UserTableMar {

    private Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static UserTableMar userTableMar = null;
    private SQLiteDatabase writeDatabase = null;

    public static UserTableMar getManager(Context context) {
        if (null == userTableMar) {
            synchronized (UserTableMar.class) {
                if (null == userTableMar) {
                    userTableMar = new UserTableMar(context);
                }
            }
        }
        return userTableMar;
    }

    // 打开数据库
    private UserTableMar(Context context) {
        this.mContext = context;
        openDataBase();
    }

    //打开数据库
    private void openDataBase() throws SQLException {
        writeDatabase = new DbOpenHelper(mContext).getWritableDatabase();
    }

    //关闭数据库
    public void closeDataBase() throws SQLException {
        if (writeDatabase != null) {
            writeDatabase.close();
        }
    }

    // 用户表名
    private static final String TABLE_NAME = "user";
    // 表字段
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String URL = "url";
    private static final String EMAIL = "email";
    private static final String COMPANY = "company";
    private static final String ZHIWU = "zhiwu";
    private static final String GONGHAO = "gonghao";
    private static final String TYPE = "type";
    private static final String BOSSPHONE = "bossPhone";

    // 创建用户表
    public static StringBuffer createUserTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("CREATE table IF NOT EXISTS " + TABLE_NAME + "( ");
        stringBuilder.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(NAME + " TEXT,");
        stringBuilder.append(PHONE + " TEXT,");
        stringBuilder.append(URL + " TEXT,");
        stringBuilder.append(EMAIL + " TEXT,");
        stringBuilder.append(COMPANY + " TEXT,");
        stringBuilder.append(ZHIWU + " TEXT,");
        stringBuilder.append(GONGHAO + " TEXT,");
        stringBuilder.append(TYPE + " INTEGER,");
        stringBuilder.append(BOSSPHONE + " TEXT");
        stringBuilder.append(" )");
        return stringBuilder;
    }

    // 删除用户表
    static StringBuffer dropUserTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("DROP table " + TABLE_NAME);
        return stringBuilder;
    }

    // 插入用户信息
    public void insetData(UserEntity entity) {
        ContentValues values = new ContentValues();
        values.put(NAME, entity.getName());
        values.put(PHONE, entity.getPhone());
        values.put(URL, entity.getPhotoUrl());
        values.put(EMAIL, entity.getEmail());
        values.put(COMPANY, entity.getCompany());
        values.put(ZHIWU, entity.getJob());
        values.put(GONGHAO, entity.getJobNumber());
        values.put(TYPE, entity.getType());
        values.put(BOSSPHONE, entity.getBossPhone());
        // 把数据插入到数据库中
        writeDatabase.insert(TABLE_NAME, null, values);
    }

}
