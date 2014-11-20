package com.hao.money.service;

import android.content.Context;
import android.widget.Adapter;

import com.hao.money.adapter.JlAdapter;
import com.hao.money.dao.InfoDao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 记录的service
 * Created by hao on 2014/11/20.
 */
@EBean
public class JlService {
    @RootContext
    Context context;
    @Bean
    InfoDao infoDao;
    private int pageSize = 5;//每页条数
    private JlAdapter adapter;
    private JSONArray array;
    private JlView ife;

    public JlView getIfe() {
        return ife;
    }

    public void setIfe(JlView ife) {
        this.ife = ife;
    }

    public void findPage(int pageNo) {
        JSONObject pager = infoDao.findPage(pageNo, pageSize);
        array = pager.optJSONArray("list");
        adapter.refresh(array);
    }

    public void setAdapter() {
        adapter = new JlAdapter(context, array);
        ife.setAdapter(adapter);
    }
}
