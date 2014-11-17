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
public class HistoryDao extends BaseDao {

    private static final String tableName = "tb_history";

    /**
     * 添加历史
     *
     * @param remark
     */
    public void add(Context context, String remark, boolean isSelect, boolean type) {
        open(context);
        ContentValues content = new ContentValues();
        if (!isSelect && count(remark, type) == 0) {
            //如果还没有这个历史就添加
            content.put("name", remark);
            content.put("count", 1);
            content.put("type", type);
            database.insert(tableName, null, content);
        } else {
            //如果有直接修改次数
            database.execSQL("update " + tableName + " set count=count+1 where name='" + remark + "'");
        }
        close();
    }


    /**
     * 根据名称查询是否存在历史
     *
     * @param context
     * @param name
     */
    public int getCountByName(Context context, String name, boolean type) {
        open(context);
        int count = count(name, type);
        close();
        return count;
    }

    /**
     * 根据名字获取使用的次数
     *
     * @param name
     * @return
     */
    private int count(String name, boolean type) {
        int count = 0;
        Cursor cursor = database.query(tableName, new String[]{"count(id)"}, "name=? and type=?", new String[]{name, type + ""}, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            count = cursor.getInt(0);
        }
        return count;
    }


    /**
     * 根据使用的次数倒序查询所有的历史记录
     *
     * @param context
     * @return
     */
    public JSONArray findAllOrderByCount(Context context, boolean type) {
        JSONArray array = new JSONArray();//需要返回的jaonArray
        open(context);
        Cursor cursor = database.query(tableName, new String[]{"*"}, "type=?", new String[]{type ? "1" : "0"}, null, null, "count desc");
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
        close();
        return array;
    }
}
