package com.hao.money.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.dao.HistoryDao;
import com.hao.money.entity.History;
import com.hao.money.util.Prompt;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择历史的service
 * Created by hao on 2014/11/17.
 */
@EBean
public class SelectHistoryService {
    @Bean
    HistoryDao historyDao;
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
        List<History> list = historyDao.findAllOrderByCount(type);//获取需要展示的数据
        adapter = new SelectHistoryAdapter(activity, list, SelectHistoryService.this);
        ife.setAdapter(adapter);
    }

    /**
     * 返回记账页面
     */
    public void back(int i, Activity activity) {
        Intent intent = new Intent();
        intent.putExtra("remark", adapter.list.get(i).getName());//设置选中的内容
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
        int id = adapter.list.get(i).getId();
        int count = historyDao.deleteById(id);
        if (count == 1) {
            //删除成功，更新数据
            List l = new ArrayList<History>();
            List original = adapter.list;
            for (int j = 0; j < original.size(); j++) {
                if (j == i) {
                    continue;
                }
                l.add(original.get(i));
            }
            adapter.refresh(l);
            Prompt.showToast(context, "删除成功");
        }
    }
}
