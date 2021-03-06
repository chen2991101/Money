package com.hao.money.service;

import android.widget.EditText;

/**
 * "我的"的接口
 * Created by hao on 2014/11/18.
 */
public interface MineView {

    /**
     * 设置金额
     *
     * @param money
     */
    void setMoney(String money, String sevenOut, String sevenIn, String thirtyOut, String thirtyIn);

    /**
     * 只设置金额
     *
     * @param money
     */
    void setOnlyMoney(String money);

    /**
     * 设置金额
     */
    void showSetMoney(String msg, boolean isFindAll);

}
