package com.hao.money.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hao.money.R;
import com.hao.money.service.JzService;
import com.hao.money.service.JzView;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;
import com.hao.money.util.Util;
import com.hao.money.view.MyApplication;
import com.hao.money.view.activity.MainActivity;
import com.hao.money.view.activity.SelectHistoryActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
@EFragment(R.layout.fragment_main_jz)
public class Main_JZ_Fragment extends BaseFragment implements JzView {
    @App
    MyApplication application;
    @ViewById
    EditText et_money, et_time, et_date, et_remark;
    @ViewById
    TextView tv_title, tv_address;
    @ViewById
    Button bt_config, bt_history;
    @ViewById
    RadioButton rb_out, rb_in;
    @Bean
    JzService jzService;
    private Calendar calendar;//日期
    private boolean isSelect = false, type = true;//true为支出 false为收入


    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        tv_title.setText("记账");
        jzService.setIfe(this);
        initDateTime();//初始化日期和时间

        //初始化的时候初始位置
        application.mLocationClient.registerLocationListener(new JzAddressListener());//设置回调
        application.mLocationClient.start();
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
                String money = et_money.getText().toString().trim();
                String remark = et_remark.getText().toString().trim();
                String address = tv_address.getText().toString().trim();
                jzService.jz(money, remark, isSelect, type, calendar, address);
                isSelect = false;
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
    public void initDateTime() {
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间转换器
        String dateTime = dateFormat.format(calendar.getTime());//格式化时间
        String[] timeArray = dateTime.split(" ");//把日期和时间分开

        et_date.setText(timeArray[0]);//设置日期
        et_time.setText(timeArray[1]);//设置时间
    }

    @Override
    @UiThread
    public void sucessMethod() {
        //保存完毕后清空金额和用途
        KeyboardUtil.closeKeyboard(getActivity());
        Prompt.showToast(getActivity(), "保存成功");
        et_money.setText("");
        et_remark.setText("");
        Prompt.hideDialog();
    }

    /**
     * 定位回调事件
     */
    private class JzAddressListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            tv_address.setText(location.getAddrStr());
            application.mLocationClient.stop();//取消定位服务
        }
    }

}
