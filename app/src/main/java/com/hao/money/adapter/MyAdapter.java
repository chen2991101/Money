package com.hao.money.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 自己实现的adapter,做一些重复的工作
 * Created by Administrator on 2014-11-17.
 */
public abstract class MyAdapter<T> extends BaseAdapter {
    public List<T> list;
    protected Context context;

    public MyAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view(i, view, viewGroup);
    }

    protected abstract View view(int i, View view, ViewGroup viewGroup);//自定义的数据

    /**
     * 刷新数据
     */
    public void refresh(List list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
