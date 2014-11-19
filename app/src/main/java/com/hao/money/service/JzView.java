package com.hao.money.service;

import android.widget.EditText;

/**
 * 记账的接口
 * Created by hao on 2014/11/18.
 */
public interface JzView {

    void showToast(String text);

    void showLoad(String text);

    /**
     * 关闭键盘
     */
    void closeKeyboard();

    /**
     * 获取日期
     *
     * @return
     */
    EditText getDate();

    /**
     * 获取时间
     *
     * @return
     */
    EditText getTime();

    /**
     * 持久化金额
     *
     * @param money
     */
    void updateMoney(float money);

}
