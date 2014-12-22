package com.hao.money.dao;

import com.hao.money.entity.History;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用途历史的dao
 * Created by hao on 2014/11/13.
 */
@EBean
public class HistoryDao {
    @OrmLiteDao(helper = BaseHelper.class, model = History.class)
    Dao<History, Integer> historyDao;

    /**
     * 添加历史
     */
    public void add(boolean isSelect, History history) {
        history = getHistory(history.getName(), history.isType());
        try {
            if (!isSelect && history == null) {
                //如果还没有这个历史就添加
                history.setCount(1);
                history.setCreateDate(System.currentTimeMillis());
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
