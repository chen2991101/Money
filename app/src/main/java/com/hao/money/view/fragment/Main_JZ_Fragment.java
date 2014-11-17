package com.hao.money.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.hao.money.R;
import com.hao.money.service.JzService;
import com.hao.money.util.Prompt;
import com.hao.money.util.Util;
import com.hao.money.view.activity.SelectHistoryActivity;

import java.util.Calendar;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
public class Main_JZ_Fragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private EditText et_money, et_time, et_date, et_remark;
    private Button bt_config, bt_history;
    private Calendar calendar;//日期
    private boolean isSelect = false, isOut = true;
    private JzService jzService;
    private RadioGroup rb_type;//消费的类型

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
        jzService = new JzService(getActivity());
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

        //消费的类型
        rb_type = (RadioGroup) view.findViewById(R.id.rb_type);
        rb_type.setOnCheckedChangeListener(this);

        jzService.initDateTime(calendar, et_date, et_time);//初始化日期和时间
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                jzService.selectDate(calendar, et_date);//选择日期
                break;
            case R.id.et_time:
                jzService.selectDate(calendar, et_time);//选择时间
                break;
            case R.id.bt_config:
                jzService.jz(calendar, et_money, et_remark, isSelect);//记账
                isSelect = false;
                break;
            case R.id.bt_history:
                startActivityForResult(new Intent(getActivity(), SelectHistoryActivity.class), 1);//跳转到历史记录
                break;
        }
    }

    /**
     * 选择历史了后设置备注
     *
     * @param remark
     */
    public void setRemark(String remark) {
        et_remark.setText(remark);
        isSelect = true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        isOut = checkedId == R.id.rb_out;
        Prompt.showToast(getActivity(), isOut + "");
    }

}
