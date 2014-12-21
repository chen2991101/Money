package com.hao.money.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.hao.money.R;
import com.hao.money.entity.Record;
import com.hao.money.service.JlService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 选择历史的activity的适配器
 * Created by Administrator on 2014-11-17.
 */
public class JlAdapter extends BaseSwipeAdapter {
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private JlService service;
    public List<Record> list;

    public JlAdapter(Context context, JlService service) {
        this.context = context;
        this.service = service;
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipeLayout;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        HoldView hold = new HoldView();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_main_jl_item, null);
        hold.tv_money = (TextView) view.findViewById(R.id.tv_money);
        hold.tv_type = (TextView) view.findViewById(R.id.tv_type);
        hold.tv_remark = (TextView) view.findViewById(R.id.tv_remark);
        hold.tv_billDate = (TextView) view.findViewById(R.id.tv_billDate);
        hold.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        hold.swipeLayout = (SwipeLayout) view.findViewById(R.id.swipeLayout);
        hold.tv_position = (TextView) view.findViewById(R.id.tv_position);//行号
        view.setTag(hold);
        return view;
    }

    @Override
    public void fillValues(int i, View view) {
        HoldView hold = (HoldView) view.getTag();
        Record obj = list.get(i);
        hold.tv_money.setText(obj.getMoney().setScale(2, 4).toString());
        hold.tv_type.setText(obj.isType() ? "支出" : "收入");
        hold.tv_remark.setText(obj.getRemark());
        hold.tv_billDate.setText(dateFormat.format(new Date(obj.getBillDate())));
        hold.tv_position.setText(i + "");
        hold.iv_delete.setOnClickListener(new Delete(i));
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Record getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class HoldView {
        TextView tv_money, tv_type, tv_remark, tv_billDate, tv_position;
        ImageView iv_delete;
        SwipeLayout swipeLayout;
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


    /**
     * 刷新数据
     */
    public void refresh(List<Record> l) {
        list = l;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     *
     * @return
     */
    public List<Record> appendArray(List<Record> l) {
        list.addAll(l);
        notifyDataSetChanged();
        return list;
    }
}
