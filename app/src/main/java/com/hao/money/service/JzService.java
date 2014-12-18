package com.hao.money.service;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.hao.money.dao.HistoryDao;
import com.hao.money.dao.InfoDao;
import com.hao.money.dao.Info_;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.util.Util;
import com.hao.money.view.activity.MainActivity;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 记账页面的service
 * Created by hao on 2014/11/17.
 */
@EBean
public class JzService {
    @RootContext
    Context context;
    @Bean
    InfoDao infoDao;
    private JzView ife;
    @Pref
    Info_ info;

    public void setIfe(JzView ife) {
        this.ife = ife;
    }

    /**
     * 验证数据
     *
     * @param money
     * @param remark
     * @return
     */
    public boolean valid(String money, String remark) {
        if (TextUtils.isEmpty(money) || !TestUtil.testMoney(money)) {
            Prompt.showToast(context, "请输入正确的金额");
            return false;
        }
        if (TextUtils.isEmpty(remark)) {
            Prompt.showToast(context, "请输入用途");
            return false;
        }
        return true;
    }

    /**
     * 记账
     *
     * @param money
     * @param remark
     * @param isSelect
     * @param type
     * @param calendar
     */
    public void jz(String money, String remark, boolean isSelect, boolean type, Calendar calendar, String address) {
        boolean b = valid(money, remark);
        if (b) {
            saveData(money, remark, isSelect, type, calendar, address);
        } else {
            Prompt.hideDialog();
        }
    }

    /**
     * 选择日期
     */
    public void selectDate(final Calendar calendar, Context context) {
        DatePickerDialog dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);//设置新的日期
                        ife.getDate().setText(year + "-" + formatNumber(month + 1) + "-" + formatNumber(day));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    /**
     * 选择时间
     */
    public void selectTime(final Calendar calendar, Context context) {
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //设置时间
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                ife.getTime().setText(formatNumber(hourOfDay) + ":" + formatNumber(minute));
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        dialog.show();
    }

    /**
     * 位数不够的话前面补0
     *
     * @param number 需要格式化的数字
     * @return
     */
    private String formatNumber(int number) {
        String str = number + "";
        if (str.length() == 1) {
            return "0" + str;
        }
        return str;
    }


    /**
     * 记账
     *
     * @param money
     * @param remark
     * @param isSelect
     * @param type
     * @param calendar
     */
    @Background
    public void saveData(String money, String remark, boolean isSelect, boolean type, Calendar calendar, String address) {
        HistoryDao historyDao = new HistoryDao();
        historyDao.add(context, remark, isSelect, type);//保存到你是记录中
        BigDecimal m = new BigDecimal(money);
        long id = infoDao.add(type, m, remark, calendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis(), address);
        if (id != -1) {
            ife.sucessMethod();
            info.sumMoney().put(Util.updateSumMoney(m, new BigDecimal(info.sumMoney().get()), !type));
            MainActivity.refreshMain = true;
            MainActivity.refreshJl = true;
        }
    }
}
