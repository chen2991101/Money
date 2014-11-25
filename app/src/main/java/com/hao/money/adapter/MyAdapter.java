package com.hao.money.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;

/**
 * 自己实现的adapter,做一些重复的工作
 * Created by Administrator on 2014-11-17.
 */
public abstract class MyAdapter extends BaseAdapter {
    protected JSONArray array;
    protected Context context;

    public MyAdapter(Context context, JSONArray array) {
        this.array = array;
        this.context = context;
    }

    public JSONArray getArray() {
        return array;
    }

    @Override
    public int getCount() {
        return array == null ? 0 : array.length();
    }

    @Override
    public Object getItem(int i) {
        return array.optJSONObject(i);
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
     *
     * @param jsonArray
     */
    public void refresh(JSONArray jsonArray) {
        array = jsonArray;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     *
     * @param jsonArray
     * @return
     */
    public JSONArray appendArray(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            array.put(jsonArray.optJSONObject(i));
        }
        notifyDataSetChanged();
        return array;
    }
}
