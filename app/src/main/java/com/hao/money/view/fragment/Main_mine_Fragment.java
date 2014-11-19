package com.hao.money.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 首页统计的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
@EFragment(R.layout.fragment_main_mine)
public class Main_mine_Fragment extends BaseFragment {
    @ViewById
    TextView tv_myMoney, tv_title;
    private float money;//金额

    public void setMoney(float money) {
        this.money = money;
    }

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        tv_title.setText("我的");//设置标题
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
