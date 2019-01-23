package com.bjzhijian.bluetoothseal.intelligentseal.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bjzhijian.bluetoothseal.intelligentseal.db.helper.DbOpenHelper;

/**
 * Created by lenovo on 2018/12/25.
 * 待上传的照片表
 */

public class PickerTableMar {

    private Context mContext;
    private static PickerTableMar pickerTableMar = null;
    private SQLiteDatabase writeDatabase = null;

    public static PickerTableMar getManager(Context context) {
        if (null == pickerTableMar) {
            synchronized (PickerTableMar.class) {
                if (null == pickerTableMar) {
                    pickerTableMar = new PickerTableMar(context);
                }
            }
        }
        return pickerTableMar;
    }

    // 打开数据库
    private PickerTableMar(Context context) {
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
    private static final String TABLE_NAME = "picker";
    // 表字段
    private static final String ID = "_id";
    private static final String MAC = "mac";
    private static final String KEY = "aesKey";
    private static final String IV = "aesIv";
    private static final String BOSSPHONE = "bossPhone";
    private static final String NAME = "name";
    private static final String REMARK = "remark";
    private static final String TYPE = "type";
    private static final String TIME = "time";

    // 创建自定义秘钥表
    public static StringBuffer createPickerTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("CREATE table IF NOT EXISTS " + TABLE_NAME + "( ");
        stringBuilder.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(MAC + " TEXT,");
        stringBuilder.append(KEY + " TEXT,");
        stringBuilder.append(IV + " TEXT,");
        stringBuilder.append(BOSSPHONE + " TEXT,");
        stringBuilder.append(NAME + " TEXT,");
        stringBuilder.append(REMARK + " TEXT,");
        stringBuilder.append(TYPE + " INTEGER,");
        stringBuilder.append(TIME + " TEXT");
        stringBuilder.append(" )");
        return stringBuilder;
    }

    // 删除用户表
    static StringBuffer dropPickerTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("DROP table " + TABLE_NAME);
        return stringBuilder;
    }

}
