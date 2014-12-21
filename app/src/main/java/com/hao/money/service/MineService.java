package com.hao.money.service;

import android.content.Context;

import com.hao.money.dao.RecordDao;
import com.hao.money.dao.Info_;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.math.BigDecimal;
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
    RecordDao recordDao;
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
        if (isFindAll) {
            Calendar seven = beforeTime(-7);//7天之前的时间
            Calendar thirty = beforeTime(-30);//30天之前的时间
            BigDecimal seven_out = recordDao.findSumMoney(seven.getTimeInMillis(), true);
            BigDecimal seven_in = recordDao.findSumMoney(seven.getTimeInMillis(), false);
            BigDecimal thirty_out = recordDao.findSumMoney(thirty.getTimeInMillis(), true);
            BigDecimal thirty_in = recordDao.findSumMoney(thirty.getTimeInMillis(), false);
            ife.setMoney(info.sumMoney().get(), seven_out.toString(), seven_in.toString(), thirty_out.toString(), thirty_in.toString());
        } else {
            ife.setOnlyMoney(info.sumMoney().get());//只设置金额
        }
    }


    public void setMoney(String str, boolean isFindAll) {
        if (TestUtil.testMoney(str)) {
            //把用户输入的金额写入到xml文件中
            info.sumMoney().put(new BigDecimal(str).setScale(2, 4).toString());
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
     * @param day 开始计算的时间
     * @return 时间
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
