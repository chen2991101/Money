package com.hao.money.dao;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * 保存一些程序的信息
 * Created by hao on 2014/11/19.
 */
@SharedPref
public interface Info {

    @DefaultString("0.00")
    String sumMoney();

}
