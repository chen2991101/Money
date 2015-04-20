package com.hao.money.view.activity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.hao.money.R;
import com.hao.money.adapter.MyPagerAdapter;
import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.service.SelectHistoryService;
import com.hao.money.service.SelectHistoryView;
import com.hao.money.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * 选择历史用途的activity
 */
@EActivity(R.layout.activity_selecthistory)
public class SelectHistoryActivity extends Activity implements SelectHistoryView {
    @ViewById
    ListView lv_list;
    @Extra
    boolean type;
    @Bean
    SelectHistoryService service;

    /**
     * 初始化方法
     */
    @AfterViews
    public void init() {
        Util.setHead(this, "请选择历史");
        service.setIfe(this);
        service.findData(type, this);//获取历史数据

        ViewPager viewPager = new ViewPager(this);
        viewPager.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(this, 150)));
        viewPager.setAdapter(new MyPagerAdapter(this));
        lv_list.addHeaderView(viewPager);

    }

    @ItemClick(R.id.lv_list)
    public void itemClick(int i) {
        //选中后返回记账页面
        service.back(i, this);
    }

    @UiThread
    public void setAdapter(SelectHistoryAdapter adapter) {
        lv_list.setAdapter(adapter);
    }
}
