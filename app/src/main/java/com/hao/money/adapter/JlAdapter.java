package com.hao.money.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.service.JlService;
import com.hao.money.util.Util;

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
    private JlService service;

    public JlAdapter(Context context, JSONArray array, JlService service) {
        super(context, array);
        this.service = service;
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
            hold.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            view.setTag(hold);
        } else {
            hold = (HoldView) view.getTag();
        }
        JSONObject obj = array.optJSONObject(i);
        hold.tv_money.setText(Util.df.format(obj.optDouble("money")));
        hold.tv_type.setText(obj.optBoolean("type") ? "支出" : "收入");
        hold.tv_remark.setText(obj.optString("remark"));
        hold.tv_billDate.setText(dateFormat.format(new Date(obj.optLong("billDate"))));
        hold.iv_delete.setOnClickListener(new Delete(i));
        return view;
    }

    private class HoldView {
        TextView tv_money, tv_type, tv_remark, tv_billDate;
        ImageView iv_delete;
    }

    private class Delete implements View.OnClickListener {
        private int position;

        public Delete(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            service.deleteItem(position);
        }
    }
}
