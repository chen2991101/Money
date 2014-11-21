package com.hao.money.service;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hao.money.adapter.JlAdapter;

/**
 * 记录的接口
 * Created by hao on 2014/11/18.
 */
public interface JlView {
    //设置适配器
    void setAdapter(JlAdapter adapter);

    /**
     * 取消记载状态
     */
    void cancelLoading(PullToRefreshBase.Mode mode);
}
