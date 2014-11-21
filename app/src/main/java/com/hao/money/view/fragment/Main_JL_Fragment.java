package com.hao.money.view.fragment;

import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hao.money.R;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.service.JlService;
import com.hao.money.service.JlView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 首页记录的fragment
 * Created by hao on 2014/11/2.
 */
@EFragment(R.layout.fragment_main_jl)
public class Main_JL_Fragment extends BaseFragment implements JlView, PullToRefreshBase.OnRefreshListener2 {
    @ViewById
    TextView tv_title;
    @ViewById
    PullToRefreshListView lv_list;
    @Bean
    JlService service;

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        tv_title.setText("记录");
        service.setIfe(this);//设置借口实现
        service.setAdapter();//设置适配器
        lv_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//lisetview只能下拉
        lv_list.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        service.findPage(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        service.findNextPage();
    }

    @Override
    public void setAdapter(JlAdapter adapter) {
        lv_list.setAdapter(adapter);
    }


    /**
     * 取消加载状态
     */
    @Override
    public void cancelLoading(PullToRefreshBase.Mode mode) {
        lv_list.onRefreshComplete();
        lv_list.setMode(mode);
    }
}
