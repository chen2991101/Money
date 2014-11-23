package com.hao.money.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hao.money.R;
import com.hao.money.adapter.JlAdapter;
import com.hao.money.service.JlService;
import com.hao.money.service.JlView;
import com.hao.money.util.Prompt;
import com.hao.money.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 首页记录的fragment
 * Created by hao on 2014/11/2.
 */
@EFragment(R.layout.fragment_main_jl)
public class Main_JL_Fragment extends BaseFragment implements JlView, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemLongClickListener {
    @ViewById
    TextView tv_title;
    @ViewById
    PullToRefreshListView lv_list;
    @Bean
    JlService service;
    private ListView listView;

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
        listView = lv_list.getRefreshableView();
        listView.setOnItemLongClickListener(this);//长按删除记录
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(getActivity()).setTitle("确认删除吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Prompt.showLoad(getActivity(), "正在删除数据");
                        String res = service.delete(position);
                        Prompt.hideDialog();
                        if (TextUtils.isEmpty(res)) {
                            //成功
                            service.findPage(1);
                            Prompt.showToast(getActivity(),"删除成功");
                        } else {
                            //失败
                            Prompt.showToast(getActivity(), res);
                        }
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
        return true;
    }

    /**
     * 刷新纪录
     */
    public void refreash() {
        service.findPage(1);
    }
}
