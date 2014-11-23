package com.hao.money.service;

import android.content.Context;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.dao.InfoDao;
import com.hao.money.dao.Info_;
import com.hao.money.util.Util;
import com.hao.money.view.activity.MainActivity;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;
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
    @Pref
    Info_ info;
    private int pageSize = 15;//每页条数
    private JlAdapter adapter;
    private JSONArray array;
    private JlView ife;
    private int currentPage;

    public void setIfe(JlView ife) {
        this.ife = ife;
    }

    /**
     * 开始删除记录
     *
     * @param position
     */
    public String delete(int position) {
        JSONObject obj = array.optJSONObject(position - 1);
        if (obj == null) {
            return "没有对应的数据";
        }
        int i = infoDao.deleteById(obj.optString("id"), context);
        if (i == 0) {
            return "删除失败";
        }

        float money = info.sumMoney().get();
        money = Util.updateSumMoney(obj.optString("money"), money + "", obj.optBoolean("type"));
        info.sumMoney().put(money);
        MainActivity.refreshMain = true;
        return null;
    }


    /**
     * 获取数据
     *
     * @param pageNo 页数
     */
    @Background
    public void findPage(int pageNo) {
        currentPage = pageNo;
        JSONObject obj = infoDao.findPage(pageNo, pageSize);
        PullToRefreshBase.Mode mode = PullToRefreshBase.Mode.PULL_FROM_START;//只支持下拉
        if (obj.optInt("totalPage") > pageNo) {
            mode = PullToRefreshBase.Mode.BOTH;//如果当前加载的不是最后一页的话可以向下滑动
        }
        updateAdapter(obj, pageNo, mode);
    }


    public void findNextPage() {
        findPage(currentPage + 1);
    }

    public void setAdapter() {
        adapter = new JlAdapter(context, array);
        ife.setAdapter(adapter);
        findPage(1);//查询第一页的内容
    }

    /**
     * ui更新列表
     *
     * @param pager
     * @param pageNo
     * @param mode
     */
    @UiThread
    public void updateAdapter(JSONObject pager, int pageNo, PullToRefreshBase.Mode mode) {
        if (pageNo == 1) {
            //如果是第一页，直接赋值
            array = pager.optJSONArray("list");
            adapter.refresh(array);
        } else {
            JSONArray jsonArray = pager.optJSONArray("list");
            array = adapter.appendArray(jsonArray);
        }

        ife.cancelLoading(mode);//关闭刷新
    }
}
