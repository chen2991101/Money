package com.hao.money.service;

import android.content.Context;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.dao.InfoDao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 记录的service
 * Created by hao on 2014/11/20.
 */
@EBean
public class JlService {
    @RootContext
    Context context;
    @Bean
    InfoDao infoDao;
    private int pageSize = 5;//每页条数
    private JlAdapter adapter;
    private JSONArray array;
    private JlView ife;

    public void setIfe(JlView ife) {
        this.ife = ife;
    }

    /**
     * 获取数据
     *
     * @param pageNo 页数
     */
    public void findPage(int pageNo) {
        JSONObject pager = infoDao.findPage(pageNo, pageSize);
        array = pager.optJSONArray("list");
        adapter.refresh(array);

        PullToRefreshBase.Mode mode = PullToRefreshBase.Mode.PULL_FROM_START;//只支持下拉
        if (pager.optInt("totalPage") < pageNo) {
            mode = PullToRefreshBase.Mode.BOTH;//如果当前加载的不是最后一页的话可以向下滑动
        }
        ife.cancelLoading(mode);//关闭刷新
    }

    public void setAdapter() {
        adapter = new JlAdapter(context, array);
        ife.setAdapter(adapter);
        findPage(1);//查询第一页的内容
    }
}
