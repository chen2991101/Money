package com.hao.money.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hao.money.entity.History;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用途历史的dao
 * Created by hao on 2014/11/13.
 */
@EBean
public class HistoryDao {
    @OrmLiteDao(helper = BaseHelper.class, model = History.class)
    Dao<History, Integer> historyDao;

    private static final String tableName = "tb_history";

    /**
     * 添加历史
     */
    public void add(Context context, boolean isSelect, History history) {
        try {
            ContentValues content = new ContentValues();
            if (!isSelect && getHistory(history.getName(), history.isType()) == null) {
                //如果还没有这个历史就添加
                history.setCount(1);
                historyDao.create(history);
            } else {
                //如果有直接修改次数
                history.setCount(history.getCount() + 1);
                historyDao.update(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据名称查询是否存在历史
     *
     * @param context
     * @param name
     */
    public long getCountByName(Context context, String name, boolean type) {
        return count(name, type);
    }

    /**
     * 根据名字获取使用的次数
     *
     * @param name
     * @return
     */
    private long count(String name, boolean type) {
        try {
            return historyDao.queryBuilder().where().eq("name", name).and().eq("type", type).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private History getHistory(String name, boolean type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("type", type);
        List<History> list = new ArrayList<History>();
        try {
            list = historyDao.queryForFieldValues(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }


    /**
     * 根据使用的次数倒序查询所有的历史记录
     *
     * @return
     */
    public List<History> findAllOrderByCount(boolean type) {
        JSONArray array = new JSONArray();//需要返回的jaonArray
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        try {
            return historyDao.queryForFieldValues(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据id删除历史记录
     *
     * @param id
     */
    public int deleteById(Integer id) {
        try {
            return historyDao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
