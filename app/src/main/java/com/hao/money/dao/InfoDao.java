package com.hao.money.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * 记账信息的表单
 * Created by hao on 2014/11/13.
 */
@EBean
public class InfoDao extends BaseDao {
    @RootContext
    Context context;
    private static final String tableName = "tb_info";

    /**
     * 添加记账信息
     */
    public long add(boolean type, BigDecimal money, String remark, long billDate, long createDate, String address, double latitude, double longitude) {
        open(context);
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("money", money.toString());
        values.put("remark", remark);
        values.put("billDate", billDate);
        values.put("createDate", createDate);
        values.put("address", address);

        values.put("latitude", latitude);
        values.put("longitude", longitude);

        values.put("isUpload", false);//刚添加的还没有上传
        long id = database.insert(tableName, null, values);
        close();
        return id;
    }


    /**
     * 分页查询数据
     *
     * @param pageNo   页数
     * @param pageSize 每页条数
     * @return 查询的记录
     */
    public JSONObject findPage(int pageNo, int pageSize) {
        openRead(context);
        JSONObject pager = new JSONObject();
        try {
            int sumCount = findCount();//总条数
            pager.put("total", sumCount);//设置总条数
            pager.put("totalPage", sumCount / pageSize + (sumCount % pageSize > 0 ? 1 : 0));//设置总条数
            pager.put("list", findAll(pageNo, pageSize));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        close();
        return pager;
    }


    /**
     * 删除记录
     *
     * @param id 需要删除的id
     * @return 影响的条数
     */
    public int deleteById(String id) {
        open(context);
        int i = database.delete(tableName, "id=?", new String[]{id});
        close();
        return i;
    }


    /**
     * 根据时间查询总记录
     *
     * @param time 时间
     * @param type 类型
     * @return 金额
     */
    public BigDecimal findSumMoney(long time, boolean type) {
        openRead(context);
        Cursor cursor = database.query(tableName, new String[]{"sum(money)"}, "billDate>=? and type=?", new String[]{time + "", type ? "1" : "0"}, null, null, null);
        BigDecimal money = BigDecimal.ZERO;
        if (cursor.moveToFirst() && cursor.getString(0) != null) {
            money = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        close();
        return money.setScale(2, 4);
    }

    /**
     * 查询数据的总条数
     *
     * @return 总条数
     */
    private int findCount() {
        int count = 0;
        Cursor cursor = database.query(tableName, new String[]{"count(id)"}, null, null, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * 查询所有数据
     *
     * @return 查询的数据
     */
    private JSONArray findAll(int pageNo, int pageSize) {
        JSONArray array = new JSONArray();
        Cursor cursor = database.query(tableName, new String[]{"*"}, null, null, null, null, "billDate desc , createDate desc", (pageNo - 1) * pageSize + "," + pageSize);
        while (cursor.moveToNext()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", cursor.getString(cursor.getColumnIndex("id")));
                obj.put("money", cursor.getFloat(cursor.getColumnIndex("money")));//金额
                obj.put("type", cursor.getInt(cursor.getColumnIndex("type")) == 1);//类型
                obj.put("remark", cursor.getString(cursor.getColumnIndex("remark")));//备注
                obj.put("billDate", cursor.getLong(cursor.getColumnIndex("billDate")));//生成时间
                obj.put("createDate", cursor.getLong(cursor.getColumnIndex("createDate")));//记录时间
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
        cursor.close();
        return array;
    }

}
