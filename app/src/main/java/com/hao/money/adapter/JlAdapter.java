package com.hao.money.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.service.SelectHistoryService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 选择历史的activity的适配器
 * Created by Administrator on 2014-11-17.
 */
public class JlAdapter extends MyAdapter {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public JlAdapter(Context context, JSONArray array) {
        super(context, array);
    }

    @Override
    protected View view(int i, View view, ViewGroup viewGroup) {
        HoldView hold;
        if (view == null) {
            hold = new HoldView();
            view = LayoutInflater.from(context).inflate(R.layout.fragment_main_jl_item, null);
            hold.tv_money = (TextView) view.findViewById(R.id.tv_money);
            hold.tv_type = (TextView) view.findViewById(R.id.tv_type);
            hold.tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            hold.tv_billDate = (TextView) view.findViewById(R.id.tv_billDate);
            view.setTag(hold);
        } else {
            hold = (HoldView) view.getTag();
        }
        JSONObject obj = array.optJSONObject(i);
        hold.tv_money.setText(obj.optString("money"));
        hold.tv_type.setText(obj.optBoolean("type") ? "支出" : "收入");
        hold.tv_remark.setText(obj.optString("remark"));
        hold.tv_billDate.setText(dateFormat.format(new Date(obj.optLong("billDate"))));
        return view;
    }

    private class HoldView {
        TextView tv_money, tv_type, tv_remark, tv_billDate;
    }
}
