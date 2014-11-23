package com.hao.money.service;

import android.content.Context;
import android.text.TextUtils;

import com.hao.money.dao.InfoDao;
import com.hao.money.dao.Info_;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.util.Util;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * "我们"页面的service
 * Created by hao on 2014/11/17.
 */
@EBean
public class MineService {
    @RootContext
    Context context;
    @Pref
    Info_ info;
    @Bean
    InfoDao infoDao;
    private MineView ife;

    public void setIfe(MineView ife) {
        this.ife = ife;
    }

    /**
     * 设置金额
     */
    public void initMoney() {
        boolean hasMoney = info.sumMoney().exists();
        if (hasMoney) {
            ife.setMoney(Util.df.format(info.sumMoney().get()));
        } else {
            ife.showSetMoney();
        }
    }

    public void setMoney(String str) {
        if (TestUtil.testMoney(str)) {
            //把用户输入的金额写入到xml文件中
            info.sumMoney().put(Float.parseFloat(str));
            ife.setMoney(Util.df.format(Float.parseFloat(str)));
            Prompt.hideView();
        } else {
            Prompt.showToast(context, "请正确输入金额");
        }
    }

    /**
     * 刷新金额
     */
    public void refreashMoney() {
        ife.setMoney(Util.df.format(info.sumMoney().get()));
    }
}
