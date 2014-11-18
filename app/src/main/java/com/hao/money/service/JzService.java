package com.hao.money.service;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.hao.money.dao.HistoryDao;
import com.hao.money.dao.InfoDao;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.view.activity.MainActivity;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 记账页面的service
 * Created by hao on 2014/11/17.
 */
public class JzService {
    private Handler handler = new Handler();
    private JzView ife;

    public JzService(JzView ife) {
        this.ife = ife;
    }

    /**
     * 记账
     *
     * @param calendar
     */
    public void jz(final Calendar calendar, final boolean isSelect, final boolean type, final Activity activity) {
        final String money = ife.getMoney();//输入的金额
        final String remark = ife.getRemark();//备注
        //验证金额
        if (TextUtils.isEmpty(money) && !TestUtil.testMoney(money)) {
            ife.showToast("请输入正确的金额");
            return;
        }
        if (TextUtils.isEmpty(remark)) {
            ife.showToast("请输入用途");
            return;
        }
        ife.closeKeyboard();
        ife.showLoad("正在保存数据");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HistoryDao historyDao = new HistoryDao();
                historyDao.add(activity, remark, isSelect, type);//保存到你是记录中
                ife.addHistory(remark);
                InfoDao infoDao = new InfoDao();
                float m = Float.parseFloat(money);
                long id = infoDao.add(activity, type, m, remark, calendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
                if (id != -1) {
                    saveMoney(activity, m, type);//更新持久化的金额
                    Prompt.showToast(activity, "添加成功");
                }
                Prompt.hideDialog();
            }
        }, 0);
    }

    /**
     * 计算持久化的金额
     */
    private void saveMoney(Activity activity, float m, boolean type) {
        SharedPreferences info = activity.getSharedPreferences("info", 0);
        float oldMoney = info.getFloat(MainActivity.SUMMONEY, 0);
        if (type) {
            //支出
            oldMoney -= m;
        } else {
            oldMoney += m;
        }
        SharedPreferences.Editor editor = info.edit();
        editor.putFloat(MainActivity.SUMMONEY, oldMoney).commit();
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
    private void selectTime(final Calendar calendar, Context context) {
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
     * 初始化日期和时间
     */
    public void initDateTime(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间转换器
        String dateTime = dateFormat.format(calendar.getTime());//格式化时间
        String[] timeArray = dateTime.split(" ");//把日期和时间分开

        ife.getDate().setText(timeArray[0]);//设置日期
        ife.getTime().setText(timeArray[1]);//设置时间
    }

}
