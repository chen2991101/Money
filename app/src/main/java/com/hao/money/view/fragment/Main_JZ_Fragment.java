package com.hao.money.view.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.hao.money.R;
import com.hao.money.view.activity.SelectHistoryActivity;
import com.hao.money.dao.HistoryDao;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.Inflater;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
public class Main_JZ_Fragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private EditText et_money, et_time, et_date, et_remark;
    private Button bt_config, bt_history;
    private Calendar calendar;//日期
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间转换器

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_jz, null);
        init();
        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        Util.setTitle("记账", view);//设置标题

        calendar = Calendar.getInstance();//初始化日期

        et_money = (EditText) view.findViewById(R.id.et_money);//记账的金额
        et_remark = (EditText) view.findViewById(R.id.et_remark);//用途

        et_date = (EditText) view.findViewById(R.id.et_date);//显示的日期
        et_date.setOnClickListener(this);

        et_time = (EditText) view.findViewById(R.id.et_time);//时间选择
        et_time.setOnClickListener(this);

        bt_config = (Button) view.findViewById(R.id.bt_config);
        bt_config.setOnClickListener(this);

        //选择用途历史按钮
        bt_history = (Button) view.findViewById(R.id.bt_history);
        bt_history.setOnClickListener(this);

        initDateTime();//初始化日期和时间
    }

    /**
     * 初始化日期和时间
     */
    private void initDateTime() {
        String dateTime = dateFormat.format(calendar.getTime());//格式化时间
        String[] timeArray = dateTime.split(" ");//把日期和时间分开

        et_date.setText(timeArray[0]);//设置日期
        et_time.setText(timeArray[1]);//设置时间
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                selectDate();//选择日期
                break;
            case R.id.et_time:
                selectTime();//选择时间
                break;
            case R.id.bt_config:
                config();
                break;
            case R.id.bt_history:
                startActivityForResult(new Intent(getActivity(), SelectHistoryActivity.class), 1);//跳转到历史记录
                break;
        }

    }

    /**
     * 点击确认
     */
    private void config() {
        String money = et_money.getText().toString().trim();//输入的金额
        String remark = et_remark.getText().toString().trim();//备注

        //验证金额
        if (TextUtils.isEmpty(money) && !TestUtil.testMoney(money)) {
            Prompt.showToast(getActivity(), "请输入正确的金额");
            return;
        }

        if (TextUtils.isEmpty(remark)) {
            Prompt.showToast(getActivity(), "请输入用途");
            return;
        }

        HistoryDao.add(getActivity(), remark);

    }

    /**
     * 选择时间
     */
    private void selectTime() {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
     * 选择日期
     */
    private void selectDate() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
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
