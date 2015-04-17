package com.hao.money.view.fragment;

import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hao.money.R;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.service.JlService;
import com.hao.money.service.JlView;
import com.hao.money.view.MyApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
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
    @App
    MyApplication app;
    private boolean isComplete = false;

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        tv_title.setText("记录");

        service.setPullText(lv_list.getLoadingLayoutProxy(false, true), lv_list.getLoadingLayoutProxy(true, false));

        service.setIfe(this);//设置借口实现
        service.setAdapter();//设置适配器
        lv_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//lisetview只能下拉
        lv_list.setOnRefreshListener(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isComplete) {
            return;
        }
        if (isVisibleToUser) {

            bkRefresh();
        }
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        service.refreshArray();
        System.out.println("start");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onpause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("stop");
        service.saveArray();
    }*/


    @Background
    public void bkRefresh() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uiRefresh();
    }

    @UiThread
    public void uiRefresh() {
        lv_list.setRefreshing();
        isComplete = true;
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        service.findPage(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        // 设置刷新文本说明(刷新过程中)
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

        service.setPullText(lv_list.getLoadingLayoutProxy(false, true), lv_list.getLoadingLayoutProxy(true, false));
    }

    /**
     * 刷新纪录
     */
    public void refreash() {
        lv_list.setRefreshing();
    }
}
