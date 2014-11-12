package com.hao.money.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.util.Util;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
public class Main_JZ_Fragment extends BaseFragment {
    private View view;
    private TextView tv_date;

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

        tv_date = (TextView) view.findViewById(R.id.tv_date);//显示的日期
        tv_date.setText("2014-11-12 14:35");
    }
}
