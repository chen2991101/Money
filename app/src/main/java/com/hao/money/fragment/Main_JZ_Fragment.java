package com.hao.money.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hao.money.R;
import com.hao.money.util.Util;

import java.util.Calendar;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
public class Main_JZ_Fragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private EditText et_money, et_time, et_date;

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
        et_money = (EditText) view.findViewById(R.id.et_money);//记账的金额

        et_date = (EditText) view.findViewById(R.id.et_date);//显示的日期
        et_date.setOnClickListener(this);

        et_time = (EditText) view.findViewById(R.id.et_time);//时间选择
        et_time.setOnClickListener(this);

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
        }

    }

    /**
     * 选择时间
     */
    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                et_time.setText(hourOfDay + ":" + minute);
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
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        et_date.setText(year + "-" + month + "-" + day);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }
}
