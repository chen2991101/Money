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
import com.hao.money.service.SelectHistoryService;
import com.hao.money.service.SelectHistoryView;
import com.hao.money.util.Prompt;
import com.hao.money.util.Util;

import org.json.JSONArray;

/**
 * 选择历史用途的activity
 */
public class SelectHistoryActivity extends Activity implements SelectHistoryView, AdapterView.OnItemClickListener {
    private ListView lv_list;
    private SelectHistoryService service;

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
        service = new SelectHistoryService(this);
        lv_list = (ListView) findViewById(R.id.lv_list);
        lv_list.setOnItemClickListener(this);
        service.findData(getIntent().getBooleanExtra("type", false), this);//获取历史数据
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //选中后返回记账页面
        service.back(i, this);
    }

    @Override
    public void setAdapter(SelectHistoryAdapter adapter) {
        lv_list.setAdapter(adapter);
    }

}
