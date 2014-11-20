package com.hao.money.service;

import android.widget.EditText;

/**
 * 记账的接口
 * Created by hao on 2014/11/18.
 */
public interface JzView {

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
     * 保存成功的操作
     */
    void sucessMethod();

}
