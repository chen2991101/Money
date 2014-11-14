package com.hao.money.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用途历史的dao
 * Created by hao on 2014/11/13.
 */
public class HistoryDao {

    private static final String tableName = "tb_history";

    /**
     * 添加历史
     *
     * @param remark
     */
    public static void add(Context context, String remark) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues content = new ContentValues();
        if (getCountByName(context, remark) == 0) {
            //如果还没有这个历史就添加
            content.put("name", remark);
            content.put("count", 1);
            database.insert(tableName, null, content);
        } else {
            //如果有直接修改次数
            database.execSQL("update " + tableName + " set count=count+1 where name='" + remark + "'");
        }
        database.close();
        helper.close();
    }


    /**
     * 根据名称查询是否存在历史
     *
     * @param context
     * @param name
     */
    public static int getCountByName(Context context, String name) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        int count = 0;
        Cursor cursor = database.query(tableName, new String[]{"count(id)"}, "name=?", new String[]{name}, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            count = cursor.getInt(0);
        }
        database.close();
        helper.close();
        return count;
    }


    /**
     * 根据使用的次数倒序查询所有的历史记录
     *
     * @param context
     * @return
     */
    public static JSONArray findAllOrderByCount(Context context) {
        JSONArray array = new JSONArray();//需要返回的jaonArray
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(tableName, new String[]{"*"}, null, null, null, null, "count desc");
        while (cursor.moveToNext()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", cursor.getString(cursor.getColumnIndex("name")));//设置名称
                obj.put("count", cursor.getString(cursor.getColumnIndex("count")));//设置使用次数
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
        database.close();
        helper.close();
        return array;
    }

}
