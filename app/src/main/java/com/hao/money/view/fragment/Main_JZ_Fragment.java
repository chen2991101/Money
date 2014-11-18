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
import com.hao.money.dao.HistoryDao;
import com.hao.money.service.JzService;
import com.hao.money.service.JzView;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;
import com.hao.money.util.Util;
import com.hao.money.view.activity.SelectHistoryActivity;

import java.util.Calendar;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
public class Main_JZ_Fragment extends BaseFragment implements JzView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private EditText et_money, et_time, et_date, et_remark;
    private Button bt_config, bt_history;
    private Calendar calendar;//日期
    private boolean isSelect = false, type = true;//true为支出 false为收入
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
        jzService = new JzService(this);
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

        jzService.initDateTime(calendar);//初始化日期和时间
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                jzService.selectDate(calendar, getActivity());//选择日期
                break;
            case R.id.et_time:
                jzService.selectDate(calendar, getActivity());//选择时间
                break;
            case R.id.bt_config:
                jzService.jz(calendar,isSelect,type,getActivity());//记账
                isSelect = false;
                break;
            case R.id.bt_history:
                Intent intent = new Intent(getActivity(), SelectHistoryActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, 1);//跳转到历史记录
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
        type = checkedId == R.id.rb_out;
    }

    @Override
    public void showToast(String text) {
        Prompt.showToast(getActivity(), text);
    }

    @Override
    public void showLoad(String text) {
        Prompt.showLoad(getActivity(), text);
    }

    @Override
    public void closeKeyboard() {
        KeyboardUtil.closeKeyboard(getActivity());
    }

    @Override
    public String getMoney() {
        return et_money.getText().toString().trim();
    }

    @Override
    public String getRemark() {
        return et_remark.getText().toString().trim();
    }

    @Override
    public EditText getDate() {
        return et_date;
    }

    @Override
    public EditText getTime() {
        return et_time;
    }

    @Override
    public void addHistory(String remark) {
        //保存完毕后清空金额和用途
        et_money.setText("");
        et_remark.setText("");
    }
}
