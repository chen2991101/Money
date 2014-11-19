package com.hao.money.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.dao.HistoryDao;
import com.hao.money.dao.InfoDao;
import com.hao.money.dao.Info_;
import com.hao.money.service.JzService;
import com.hao.money.service.JzView;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;
import com.hao.money.view.activity.SelectHistoryActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
@EFragment(R.layout.fragment_main_jz)
public class Main_JZ_Fragment extends BaseFragment implements JzView {
    @ViewById
    EditText et_money, et_time, et_date, et_remark;
    @ViewById
    TextView tv_title;
    @ViewById
    Button bt_config, bt_history;
    @ViewById
    RadioButton rb_out, rb_in;
    @Pref
    Info_ info;
    private Calendar calendar;//日期
    private boolean isSelect = false, type = true;//true为支出 false为收入
    private JzService jzService;

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        jzService = new JzService(this);
        tv_title.setText("记账");

        calendar = Calendar.getInstance();//初始化日期

        initDateTime(calendar);//初始化日期和时间
    }

    @Click({R.id.et_date, R.id.et_time, R.id.bt_config, R.id.bt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                jzService.selectDate(calendar, getActivity());//选择日期
                break;
            case R.id.et_time:
                jzService.selectTime(calendar, getActivity());//选择时间
                break;
            case R.id.bt_config:
                jz();
                break;
            case R.id.bt_history:
                Intent intent = new Intent(getActivity(), SelectHistoryActivity_.class);
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

    @CheckedChange({R.id.rb_in, R.id.rb_out})
    public void CheckedChanged(CompoundButton button, boolean isSelect) {
        if (isSelect) {
            type = button.getId() == R.id.rb_out;
        }
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
    public EditText getDate() {
        return et_date;
    }

    @Override
    public EditText getTime() {
        return et_time;
    }


    /**
     * 初始化日期和时间(因为mainactivity可能要调用，所以不抽取到service中)
     */
    public void initDateTime(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间转换器
        String dateTime = dateFormat.format(calendar.getTime());//格式化时间
        String[] timeArray = dateTime.split(" ");//把日期和时间分开

        et_date.setText(timeArray[0]);//设置日期
        et_time.setText(timeArray[1]);//设置时间
    }


    /**
     * 记账的逻辑
     */
    public void jz() {
        String money = et_money.getText().toString().trim();
        String remark = et_remark.getText().toString().trim();
        boolean b = jzService.valid(money, remark);
        if (b) {
            saveData(money, remark);
            isSelect = false;
        } else {
            Prompt.hideDialog();
        }
    }

    @Background
    public void saveData(String money, String remark) {
        HistoryDao historyDao = new HistoryDao();
        historyDao.add(getActivity(), remark, isSelect, type);//保存到你是记录中
        InfoDao infoDao = new InfoDao();
        float m = Float.parseFloat(money);
        long id = infoDao.add(getActivity(), type, m, remark, calendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
        if (id != -1) {
            addSuccess();
            jzService.saveMoney(m, type, info.sumMoney().get());//更新持久化的金额
        }
    }

    @UiThread
    public void addSuccess() {
        //保存完毕后清空金额和用途
        KeyboardUtil.closeKeyboard(getActivity());
        Prompt.showToast(getActivity(), "保存成功");
        et_money.setText("");
        et_remark.setText("");
        Prompt.hideDialog();
    }

    @Override
    public void updateMoney(float money) {
        info.sumMoney().put(money);
    }
}
