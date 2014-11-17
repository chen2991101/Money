package com.hao.money.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hao.money.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 选择历史的activity的适配器
 * Created by Administrator on 2014-11-17.
 */
public class SelectHistoryAdapter extends MyAdapter {

    public SelectHistoryAdapter(Activity activity, JSONArray array) {
        super(activity, array);
    }

    @Override
    protected View view(int i, View view, ViewGroup viewGroup) {
        HoldView hold;
        if (view == null) {
            hold = new HoldView();
            view = activity.getLayoutInflater().inflate(R.layout.activity_selecthistory_item, null);
            hold.tv_name = (TextView) view.findViewById(R.id.tv_name);//名称
            hold.tv_count = (TextView) view.findViewById(R.id.tv_count);//次数
            view.setTag(view);
        } else {
            hold = (HoldView) view.getTag();
        }

        JSONObject obj = array.optJSONObject(i);
        hold.tv_name.setText(obj.optString("name"));
        hold.tv_count.setText(obj.optString("count"));
        return view;
    }

    private class HoldView {
        TextView tv_name, tv_count;
    }
}
