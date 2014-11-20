package com.hao.money.adapter;

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

/**
 * 选择历史的activity的适配器
 * Created by Administrator on 2014-11-17.
 */
public class SelectHistoryAdapter extends MyAdapter {
    private SelectHistoryService service;

    public SelectHistoryAdapter(Context context, JSONArray array, SelectHistoryService service) {
        super(context, array);
        this.service = service;
    }

    @Override
    protected View view(int i, View view, ViewGroup viewGroup) {
        HoldView hold;
        if (view == null) {
            hold = new HoldView();
            view = LayoutInflater.from(context).inflate(R.layout.activity_selecthistory_item, null);
            hold.tv_name = (TextView) view.findViewById(R.id.tv_name);//名称
            hold.tv_count = (TextView) view.findViewById(R.id.tv_count);//次数
            hold.bt_delete = (Button) view.findViewById(R.id.bt_delete);//删除按钮
            view.setTag(hold);
        } else {
            hold = (HoldView) view.getTag();
        }

        JSONObject obj = array.optJSONObject(i);
        hold.tv_name.setText(obj.optString("name"));
        hold.tv_count.setText(obj.optString("count"));
        hold.bt_delete.setOnClickListener(new Click(i));
        return view;
    }

    private class HoldView {
        TextView tv_name, tv_count;
        Button bt_delete;
    }

    private class Click implements View.OnClickListener {
        private int i;

        private Click(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(context).setTitle("确认删除吗？")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.deleteHistory(i, context);//删除历史
                        }
                    })
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();
        }
    }
}
