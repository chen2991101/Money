package com.hao.money.service;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.hao.money.R;
import com.hao.money.dao.InfoDao;
import com.hao.money.dao.Info_;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.util.Util;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Calendar;

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
            findMoney(true);
        } else {
            ife.showSetMoney("初始化金额", true);
        }
    }

    /**
     * 重新设置金额
     */
    public void resetMoney() {
        ife.showSetMoney("重置金额", false);
    }

    /**
     * 查询设置金额
     *
     * @param isFindAll 时候需要查询所有的记录
     */
    @Background
    public void findMoney(boolean isFindAll) {
        String money = Util.df.format(info.sumMoney().get());
        if (isFindAll) {
            Calendar seven = beforeTime(-7);//7天之前的时间
            Calendar thirty = beforeTime(-30);//30天之前的时间
            float seven_out = infoDao.findSumMoney(seven.getTimeInMillis(), true);
            float seven_in = infoDao.findSumMoney(seven.getTimeInMillis(), false);
            float thirty_out = infoDao.findSumMoney(thirty.getTimeInMillis(), true);
            float thirty_in = infoDao.findSumMoney(thirty.getTimeInMillis(), false);
            ife.setMoney(money, Util.df.format(seven_out), Util.df.format(seven_in), Util.df.format(thirty_out), Util.df.format(thirty_in));
        } else {
            ife.setOnlyMoney(money);//只设置金额
        }
    }


    public void setMoney(String str, boolean isFindAll) {
        if (TestUtil.testMoney(str)) {
            //把用户输入的金额写入到xml文件中
            info.sumMoney().put(Float.parseFloat(str));
            findMoney(isFindAll);
            Prompt.hideView();
        } else {
            Prompt.showToast(context, "请正确输入金额");
        }
    }

    /**
     * 刷新金额
     */
    public void refreashMoney() {
        findMoney(true);
    }


    /**
     * 计算几天之前的时间
     *
     * @param day
     * @return
     */
    private Calendar beforeTime(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }
}
