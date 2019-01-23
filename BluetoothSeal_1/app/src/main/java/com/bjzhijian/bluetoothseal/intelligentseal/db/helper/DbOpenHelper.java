package com.bjzhijian.bluetoothseal.intelligentseal.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bjzhijian.bluetoothseal.intelligentseal.db.SealTableMar;
import com.bjzhijian.bluetoothseal.intelligentseal.db.UserTableMar;
import com.fastwork.library.mutils.MFilerUtil;

/**
 * Created by lenovo on 2018/12/3.
 * 数据库
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    // 数据库目录
    private static String DB_NAME = MFilerUtil.gainSDCardPath() + MFilerUtil.dbFileName + "zhijian.db";
    //    private static final String DB_NAME = "zhijian.db";
    // 数据路版本
    private static int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTableMar.createUserTable().toString());
        db.execSQL(SealTableMar.createSealTable().toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
