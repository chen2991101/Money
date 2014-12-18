package com.hao.money.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.dao.InfoDao;
import com.hao.money.dao.Info_;
import com.hao.money.util.Prompt;
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

import java.math.BigDecimal;
import java.util.List;

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
    private JlView ife;
    private int currentPage;
    private boolean scrollable = true;//是否可以滑动

    public void setIfe(JlView ife) {
        this.ife = ife;
    }


    /**
     * 删除记录
     *
     * @param position
     */
    public void deleteItem(final int position) {
        new AlertDialog.Builder(context).setTitle("确认删除吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消当前打开的按钮
                        List<SwipeLayout> list = adapter.getOpenLayouts();
                        for (SwipeLayout swipeLayout : list) {
                            if (swipeLayout.getOpenStatus().equals(SwipeLayout.Status.Open)) {
                                swipeLayout.close(false);
                            }
                        }

                        Prompt.showLoad(context, "正在删除数据");
                        String res = delete(position);
                        Prompt.hideDialog();
                        if (TextUtils.isEmpty(res)) {
                            //成功
                            JSONArray data = new JSONArray();
                            JSONArray adapterArray = adapter.getArray();
                            for (int i = 0; i < adapterArray.length(); i++) {
                                if (i == position) {
                                    continue;
                                }
                                data.put(adapterArray.optJSONObject(i));
                            }
                            adapter.refresh(data);
                            Prompt.showToast(context, "删除成功");
                        } else {
                            //失败
                            Prompt.showToast(context, res);
                        }
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    /**
     * 开始删除记录
     *
     * @param position
     */
    public String delete(int position) {
        JSONObject obj = adapter.getArray().optJSONObject(position);
        if (obj == null) {
            return "没有对应的数据";
        }
        int i = infoDao.deleteById(obj.optString("id"));
        if (i == 0) {
            return "删除失败";
        }

        info.sumMoney().put(Util.updateSumMoney(new BigDecimal(obj.optString("money")), new BigDecimal(info.sumMoney().get()), obj.optBoolean("type")));
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
        adapter = new JlAdapter(context, this);
        adapter.setMode(SwipeItemMangerImpl.Mode.Single);//单列
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
            adapter.refresh(pager.optJSONArray("list"));
        } else {
            adapter.appendArray(pager.optJSONArray("list"));
        }

        ife.cancelLoading(mode);//关闭刷新
    }

    /**
     * 滑动事件
     *
     * @return
     */
    public boolean touchListener(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            List<SwipeLayout> list = adapter.getOpenLayouts();
            for (SwipeLayout swipeLayout : list) {
                if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                    scrollable = false;
                    swipeLayout.close();
                    return true;
                }
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            scrollable = true;
            return false;
        } else {
            return !scrollable;
        }
        return false;
    }
}
