package com.hao.money.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hao.money.R;
import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.dao.HistoryDao;
import com.hao.money.util.Prompt;
import com.hao.money.util.Util;

import org.json.JSONArray;

/**
 * 选择历史用途的activity
 */
public class SelectHistoryActivity extends Activity implements AdapterView.OnItemClickListener {
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
        lv_list.setOnItemClickListener(this);
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
                HistoryDao historyDao = new HistoryDao();
                array = historyDao.findAllOrderByCount(SelectHistoryActivity.this);//获取需要展示的数据
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //选中后返回记账页面
        Intent intent = new Intent();
        intent.putExtra("remark", array.optJSONObject(i).optString("name"));//设置选中的内容
        setResult(1, intent);
        finish();
    }
}
