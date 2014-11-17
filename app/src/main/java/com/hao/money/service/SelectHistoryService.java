package com.hao.money.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.ListView;

import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.dao.HistoryDao;

import org.json.JSONArray;

/**
 * 选择历史的service
 * Created by hao on 2014/11/17.
 */
public class SelectHistoryService {
    private Activity activity;
    private Handler handler = new Handler();
    private JSONArray array;

    public SelectHistoryService(Activity activity) {
        this.activity = activity;
    }

    /**
     * 查询历史记录
     *
     * @param type
     * @param lv_list
     */
    public void findData(final boolean type, final ListView lv_list) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HistoryDao historyDao = new HistoryDao();
                array = historyDao.findAllOrderByCount(activity, type);//获取需要展示的数据
                lv_list.setAdapter(new SelectHistoryAdapter(activity, array));//设置数据集合
            }
        }, 0);
    }

    /**
     * 返回记账页面
     */
    public void back(int i) {
        Intent intent = new Intent();
        intent.putExtra("remark", array.optJSONObject(i).optString("name"));//设置选中的内容
        activity.setResult(1, intent);
        activity.finish();
    }
}
