package com.bjzhijian.bluetoothseal.intelligentseal.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bjzhijian.bluetoothseal.intelligentseal.db.helper.DbOpenHelper;

/**
 * Created by lenovo on 2018/12/25.
 * 印章设备表
 */

public class SealTableMar {

    private Context mContext;
    private static SealTableMar sealTableMar = null;
    private SQLiteDatabase writeDatabase = null;

    public static SealTableMar getManager(Context context) {
        if (null == sealTableMar) {
            synchronized (SealTableMar.class) {
                if (null == sealTableMar) {
                    sealTableMar = new SealTableMar(context);
                }
            }
        }
        return sealTableMar;
    }

    // 打开数据库
    private SealTableMar(Context context) {
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

    // 设备表名
    private static final String TABLE_NAME = "seal";
    // 表字段
    private static final String ID = "_id";
    private static final String MAC = "mac";
    private static final String KEY = "aesKey";
    private static final String IV = "aesIv";
    private static final String BOSSPHONE = "bossPhone";
    private static final String MANAGERPHONE = "managerPhone";
    private static final String MANAGERNUMBER = "managerNumber";// 管理员数量
    private static final String ELECTRICITY = "electricity";// 电量
    private static final String NAME = "name";
    private static final String REMARK = "remark";
    private static final String TYPE = "type";
    private static final String TIME = "time";

    // 创建自定义秘钥表
    public static StringBuffer createSealTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("CREATE table IF NOT EXISTS " + TABLE_NAME + "( ");
        stringBuilder.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(MAC + " TEXT,");
        stringBuilder.append(KEY + " TEXT,");
        stringBuilder.append(IV + " TEXT,");
        stringBuilder.append(BOSSPHONE + " TEXT,");
        stringBuilder.append(MANAGERPHONE + " TEXT,");
        stringBuilder.append(MANAGERNUMBER + " INTEGER,");
        stringBuilder.append(ELECTRICITY + " INTEGER,");
        stringBuilder.append(NAME + " TEXT,");
        stringBuilder.append(REMARK + " TEXT,");
        stringBuilder.append(TYPE + " INTEGER,");
        stringBuilder.append(TIME + " TEXT");
        stringBuilder.append(" )");
        return stringBuilder;
    }

    // 删除用户表
    static StringBuffer dropSealTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("DROP table " + TABLE_NAME);
        return stringBuilder;
    }

}
