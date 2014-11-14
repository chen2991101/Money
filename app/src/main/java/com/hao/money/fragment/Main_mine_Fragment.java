package com.hao.money.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.util.Util;

/**
 * 首页统计的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
public class Main_mine_Fragment extends BaseFragment {
    private View view;
    private TextView tv_myMoney;
    private float money;//金额


    public Main_mine_Fragment(float money) {
        this.money = money;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_mine, null);
        init();
        return view;
    }


    /**
     * 初始化
     */
    private void init() {
        Util.setTitle("我的", view);//设置标题
        tv_myMoney = (TextView) view.findViewById(R.id.tv_myMoney);//我的金额
        tv_myMoney.setText(money + "");//设置我的身价
    }

    /**
     * 刷新我的金额
     *
     * @param money 金额
     */
    public void refreashMoney(float money) {
        tv_myMoney.setText(money + "");
    }
}
