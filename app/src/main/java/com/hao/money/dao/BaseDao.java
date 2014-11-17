package com.hao.money.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2014-11-17.
 */
public class BaseDao {
    protected DatabaseHelper helper;
    protected SQLiteDatabase database;

    /**
     * 打开网络链接
     *
     * @param context
     */
    protected void open(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    /**
     * 关闭链接
     */
    protected void close() {
        database.close();
        helper.close();
    }
}
