package com.hao.money.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.dao.Info_;
import com.hao.money.dao.RecordDao;
import com.hao.money.entity.Record;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 记录的service
 * Created by hao on 2014/11/20.
 */
@EBean
public class JlService {
    @RootContext
    Context context;
    @Bean
    RecordDao recordDao;
    @Pref
    Info_ info;
    private int pageSize = 15;//每页条数
    private JlAdapter adapter;
    private JlView ife;
    private int currentPage;

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
                            List<Record> data = new ArrayList<Record>();
                            List<Record> l = adapter.list;
                            for (int i = 0; i < l.size(); i++) {
                                if (i == position) {
                                    continue;
                                }
                                data.add(l.get(i));
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
        Record obj = adapter.getItem(position);
        if (obj == null) {
            return "没有对应的数据";
        }
        int i = recordDao.deleteById(obj.getId());
        if (i == 0) {
            return "删除失败";
        }

        info.sumMoney().put(Util.updateSumMoney(obj.getMoney(), new BigDecimal(info.sumMoney().get()), obj.isType()));
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
        Map<String, Object> map = recordDao.findPage(pageNo, pageSize);
        PullToRefreshBase.Mode mode = PullToRefreshBase.Mode.PULL_FROM_START;//只支持下拉
        if ((Long) map.get("totalPage") > pageNo) {
            mode = PullToRefreshBase.Mode.BOTH;//如果当前加载的不是最后一页的话可以向下滑动
        }
        updateAdapter(map, pageNo, mode);
    }


    public void findNextPage() {
        findPage(currentPage + 1);
    }

    public void setAdapter() {
        adapter = new JlAdapter(context, this);
        adapter.setMode(SwipeItemMangerImpl.Mode.Single);//单列
        ife.setAdapter(adapter);
    }

    /**
     * ui更新列表
     *
     * @param pager
     * @param pageNo
     * @param mode
     */
    @UiThread
    public void updateAdapter(Map<String, Object> pager, int pageNo, PullToRefreshBase.Mode mode) {
        if (pageNo == 1) {
            //如果是第一页，直接赋值
            adapter.refresh((List<Record>) pager.get("list"));
        } else {
            adapter.appendArray((List<Record>) pager.get("list"));
        }

        ife.cancelLoading(mode);//关闭刷新
    }


    /**
     * 设置上下拉的提示信息
     */
    public void setPullText(ILoadingLayout up, ILoadingLayout down) {
        up.setPullLabel("上拉加载...");
        up.setRefreshingLabel("正在加载...");
        up.setReleaseLabel("松开加载更多...");

        down.setPullLabel("下拉加载...");
        down.setRefreshingLabel("正在加载...");
        down.setReleaseLabel("松开加载更多...");
    }

}
