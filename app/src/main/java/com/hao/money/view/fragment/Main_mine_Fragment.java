package com.hao.money.view.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.service.MineService;
import com.hao.money.service.MineView;
import com.hao.money.util.Prompt;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * 首页统计的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
@EFragment(R.layout.fragment_main_mine)
public class Main_mine_Fragment extends BaseFragment implements MineView, View.OnClickListener {
    @ViewById
    TextView tv_myMoney, tv_title, tv_sevenOut, tv_sevenIn, tv_thirtyOut, tv_thirtyIn;
    @Bean
    MineService service;
    private EditText et_setMoney;
    private Button bt_setMoney;

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        tv_title.setText("我的");//设置标题
        service.setIfe(this);
        service.initMoney();
    }

    @Override
    @UiThread
    public void setMoney(String money, String sevenOut, String sevenIn, String thirtyOut, String thirtyIn) {
        tv_myMoney.setText(money);
        tv_sevenOut.setText(sevenOut);
        tv_sevenIn.setText(sevenIn);
        tv_thirtyOut.setText(thirtyOut);
        tv_thirtyIn.setText(thirtyIn);
    }

    @Override
    public void showSetMoney() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_setmoney, null);
        et_setMoney = (EditText) view.findViewById(R.id.et_setMoney);//初始化金额数量
        bt_setMoney = (Button) view.findViewById(R.id.bt_setMoney);//确认初始化金额按钮
        bt_setMoney.setOnClickListener(this);//设置点击事件
        Prompt.showView(getActivity(), view);
    }

    @Override
    public void onClick(View v) {
        service.setMoney(et_setMoney.getText().toString().trim());
    }

    /**
     * 刷新金额
     */
    public void refreashMoney() {
        service.refreashMoney();
    }
}
