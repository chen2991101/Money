package com.hao.money.dao;

import com.hao.money.entity.Record;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记账信息的表单
 * Created by hao on 2014/11/13.
 */
@EBean
public class RecordDao {

    @OrmLiteDao(helper = BaseHelper.class, model = Record.class)
    Dao<Record, Integer> dao;


    /**
     * 添加记账信息
     */
    public int add(Record record) {
        try {
            record = dao.createIfNotExists(record);
            return record.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 分页查询数据
     *
     * @param pageNo   页数
     * @param pageSize 每页条数
     * @return 查询的记录
     */
    public Map<String, Object> findPage(int pageNo, int pageSize) {
        Map pager = new HashMap<String, Object>();
        try {
            long sumCount = findCount();//总条数
            pager.put("total", sumCount);//设置总条数
            pager.put("totalPage", sumCount / pageSize + (sumCount % pageSize > 0 ? 1 : 0));//设置总条数
            pager.put("list", findAll(pageNo, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pager;
    }


    /**
     * 删除记录
     *
     * @param id 需要删除的id
     * @return 影响的条数
     */
    public int deleteById(Integer id) {
        try {
            return dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据时间查询总记录
     *
     * @param time 时间
     * @param type 类型
     * @return 金额
     */
    public BigDecimal findSumMoney(long time, boolean type) {
        String[] res = null;
        try {
            res = dao.queryRaw("select sum(money) from tb_record where billDate>=" + time + " and type=" + (type ? "1" : "0")).getResults().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new BigDecimal(res[0] == null ? "0" : res[0]).setScale(2, 4);
    }

    /**
     * 查询数据的总条数
     *
     * @return 总条数
     */

    private long findCount() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询所有数据
     *
     * @return 查询的数据
     */
    private List<Record> findAll(int pageNo, int pageSize) {
        try {
            return dao.queryBuilder().orderBy("billDate", false).orderBy("createDate", false).offset((pageNo - 1) * pageSize).limit(pageSize).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
