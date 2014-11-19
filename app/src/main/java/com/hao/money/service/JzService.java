package com.hao.money.service;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.hao.money.util.TestUtil;
import com.hao.money.view.activity.MainActivity;

import java.util.Calendar;

/**
 * 记账页面的service
 * Created by hao on 2014/11/17.
 */
public class JzService {
    private JzView ife;

    public JzService(JzView ife) {
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
        if (TextUtils.isEmpty(money) && !TestUtil.testMoney(money)) {
            ife.showToast("请输入正确的金额");
            return false;
        }
        if (TextUtils.isEmpty(remark)) {
            ife.showToast("请输入用途");
            return false;
        }
        return true;
    }

    /**
     * 计算持久化的金额
     */
    public void saveMoney(float m, boolean type, float oldMoney) {
        if (type) {
            //支出
            oldMoney -= m;
        } else {
            oldMoney += m;
        }
        ife.updateMoney(oldMoney);
        MainActivity.refreshMoeny = true;
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
}
