package com.hao.money.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.hao.money.R;
import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.dao.HistoryDao;
import com.hao.money.util.Util;

import org.json.JSONArray;

/**
 * 选择历史用途的activity
 */
public class SelectHistoryActivity extends Activity {
    private ListView lv_list;
    private JSONArray array;//需要展示的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecthistory);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        Util.setHead(this, "请选择历史");
        lv_list = (ListView) findViewById(R.id.lv_list);
        findData();//初始化查询数据
    }

    /**
     * 异步操作sqlite获取数据
     */
    private void findData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                array = HistoryDao.findAllOrderByCount(SelectHistoryActivity.this);//获取需要展示的数据
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    /**
     * 用来更新页面的handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv_list.setAdapter(new SelectHistoryAdapter(SelectHistoryActivity.this, array));//设置数据集合
        }
    };
}
