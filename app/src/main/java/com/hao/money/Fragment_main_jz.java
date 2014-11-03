package com.hao.money;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hao.money.util.Util;

/**
 * 首页记账的fragment
 * Created by hao on 2014/11/2.
 */
public class Fragment_main_jz extends BaseFragment {
    private View view;

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
        Util.getInstance().setTitle("记账", view);//设置标题
    }
}
