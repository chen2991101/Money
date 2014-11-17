package com.hao.money.service;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.hao.money.dao.HistoryDao;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 记账页面的service
 * Created by hao on 2014/11/17.
 */
public class JzService {
    private Activity activity;

    private Handler handler = new Handler();

    public JzService(Activity activity) {
        this.activity = activity;
    }

    /**
     * 记账
     *
     * @param calendar
     * @param et_money
     * @param et_remark
     * @param isSelect
     */
    public void jz(Calendar calendar, final EditText et_money, final EditText et_remark, final boolean isSelect) {
        String money = et_money.getText().toString().trim();//输入的金额
        final String remark = et_remark.getText().toString().trim();//备注
        //验证金额
        if (TextUtils.isEmpty(money) && !TestUtil.testMoney(money)) {
            Prompt.showToast(activity, "请输入正确的金额");
            return;
        }
        if (TextUtils.isEmpty(remark)) {
            Prompt.showToast(activity, "请输入用途");
            return;
        }

        KeyboardUtil.closeKeyboard(activity);
        Prompt.showLoad(activity, "正在保存数据");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                add(remark, isSelect, et_money, et_remark);
            }
        }, 0);
    }

    /**
     * 添加记录
     *
     * @param remark
     * @param isSelect
     * @param et_money
     * @param et_remark
     */
    private void add(String remark, boolean isSelect, EditText et_money, EditText et_remark) {
        HistoryDao historyDao = new HistoryDao();
        historyDao.add(activity, remark, isSelect);//保存到你是记录中
        //保存完毕后清空金额和用途
        et_money.setText("");
        et_remark.setText("");
        Prompt.hideDialog();
    }


    private void add() {

    }

    /**
     * 选择日期
     */
    public void selectDate(final Calendar calendar, final EditText et_date) {
        DatePickerDialog dialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);//设置新的日期
                        et_date.setText(year + "-" + formatNumber(month + 1) + "-" + formatNumber(day));
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
    private void selectTime(final Calendar calendar, final EditText et_time) {
        TimePickerDialog dialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //设置时间
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                et_time.setText(formatNumber(hourOfDay) + ":" + formatNumber(minute));
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
    public void initDateTime(Calendar calendar, EditText et_date, EditText et_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间转换器
        String dateTime = dateFormat.format(calendar.getTime());//格式化时间
        String[] timeArray = dateTime.split(" ");//把日期和时间分开

        et_date.setText(timeArray[0]);//设置日期
        et_time.setText(timeArray[1]);//设置时间
    }

}
