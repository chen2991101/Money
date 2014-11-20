package com.hao.money.view.fragment;

import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 首页记录的fragment
 * Created by hao on 2014/11/2.
 */
@EFragment(R.layout.fragment_main_jl)
public class Main_JL_Fragment extends BaseFragment {
    @ViewById
    TextView tv_title;

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        tv_title.setText("记录");
    }
}
