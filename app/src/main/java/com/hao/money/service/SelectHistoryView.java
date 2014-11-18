package com.hao.money.service;

import android.content.Intent;

import com.hao.money.adapter.SelectHistoryAdapter;

/**
 * 选择你是的借口
 * Created by hao on 2014/11/18.
 */
public interface SelectHistoryView {

    /**
     * 设置数据集
     *
     * @param adapter
     */
    void setAdapter(SelectHistoryAdapter adapter);

}
