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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

/**
 * 选择历史用途的activity
 */
@EActivity(R.layout.activity_selecthistory)
public class SelectHistoryActivity extends Activity implements SelectHistoryView {
    @ViewById
    ListView lv_list;
    @Extra
    boolean type;
    private SelectHistoryService service;

    /**
     * 初始化方法
     */
    @AfterViews
    public void init() {
        Util.setHead(this, "请选择历史");
        service = new SelectHistoryService(this);
        findData(type, this);//获取历史数据
    }

    @ItemClick(R.id.lv_list)
    public void itemClick(int i) {
        //选中后返回记账页面
        service.back(i, this);
    }

    @UiThread
    public void setAdapter(SelectHistoryAdapter adapter) {
        lv_list.setAdapter(adapter);
    }


    @Background
    public void findData(boolean type, Activity activity) {
        service.findData(type, activity);
    }

}
