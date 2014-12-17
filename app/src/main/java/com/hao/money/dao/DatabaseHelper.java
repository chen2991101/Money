package com.hao.money.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * Created by Administrator on 2014-11-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydata.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用途历史表
        String sql = "CREATE TABLE tb_history (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, count INTEGER,type boolean)";
        db.execSQL(sql);
        sql = "CREATE TABLE tb_info (id INTEGER PRIMARY KEY AUTOINCREMENT,type boolean, money float,address VARCHAR, remark VARCHAR,billDate long,createDate long)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
    }
}
