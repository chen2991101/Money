package com.hao.money.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.dao.HistoryDao;
import com.hao.money.util.Prompt;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.json.JSONArray;

/**
 * 选择历史的service
 * Created by hao on 2014/11/17.
 */
@EBean
public class SelectHistoryService {
    private JSONArray array;
    private SelectHistoryView ife;
    private SelectHistoryAdapter adapter;

    public void setIfe(SelectHistoryView ife) {
        this.ife = ife;
    }

    /**
     * 查询历史记录
     *
     * @param type
     */
    @Background
    public void findData(final boolean type, final Activity activity) {
        HistoryDao historyDao = new HistoryDao();
        array = historyDao.findAllOrderByCount(activity, type);//获取需要展示的数据
        adapter = new SelectHistoryAdapter(activity, array, SelectHistoryService.this);
        ife.setAdapter(adapter);
    }

    /**
     * 返回记账页面
     */
    public void back(int i, Activity activity) {
        Intent intent = new Intent();
        intent.putExtra("remark", array.optJSONObject(i).optString("name"));//设置选中的内容
        activity.setResult(1, intent);
        activity.finish();
    }

    /**
     * 删除历史
     *
     * @param i
     * @param context
     */
    public void deleteHistory(int i, Context context) {
        String id = array.optJSONObject(i).optString("id");
        HistoryDao historyDao = new HistoryDao();
        int count = historyDao.deleteById(context, id);
        if (count == 1) {
            //删除成功，更新数据
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < array.length(); j++) {
                if (j == i) {
                    continue;
                }
                jsonArray.put(array.optJSONObject(j));
            }
            array = jsonArray;
            adapter.refresh(array);
            Prompt.showToast(context, "删除成功");
        }
    }
}
