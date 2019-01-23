package com.bjzhijian.bluetoothseal.intelligentseal.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bjzhijian.bluetoothseal.intelligentseal.db.helper.DbOpenHelper;

/**
 * Created by lenovo on 2018/12/25.
 * 申请用章表
 */

public class ApplyTableMar {

    private Context mContext;
    private static ApplyTableMar applyTableMar = null;
    private SQLiteDatabase writeDatabase = null;

    public static ApplyTableMar getManager(Context context) {
        if (null == applyTableMar) {
            synchronized (ApplyTableMar.class) {
                if (null == applyTableMar) {
                    applyTableMar = new ApplyTableMar(context);
                }
            }
        }
        return applyTableMar;
    }

    // 打开数据库
    private ApplyTableMar(Context context) {
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
    private static final String TABLE_NAME = "apply";
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
    public static StringBuffer createApplyTable() {
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
    static StringBuffer dropApplyTable() {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("DROP table " + TABLE_NAME);
        return stringBuilder;
    }

}
