package com.hao.money.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 记账信息的表单
 * Created by hao on 2014/11/13.
 */
public class InfoDao extends BaseDao {
    private static final String tableName = "tb_info";

    /**
     * 添加记账信息
     *
     * @param context
     */
    public long add(Context context, boolean type, float money, String remark, long billDate, long createDate) {
        open(context);
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("money", money);
        values.put("remark", remark);
        values.put("billDate", billDate);
        values.put("createDate", createDate);
        long id = database.insert(tableName, null, values);
        close();
        return id;
    }
}
