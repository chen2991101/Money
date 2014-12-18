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
import com.hao.money.service.JlService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 选择历史的activity的适配器
 * Created by Administrator on 2014-11-17.
 */
public class JlAdapter extends BaseSwipeAdapter {
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private JlService service;
    private JSONArray array;

    public JlAdapter(Context context, JlService service) {
        this.context = context;
        this.service = service;
    }

    public JSONArray getArray() {
        return array;
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
        JSONObject obj = array.optJSONObject(i);
        hold.tv_money.setText(obj.optString("money"));
        hold.tv_type.setText(obj.optBoolean("type") ? "支出" : "收入");
        hold.tv_remark.setText(obj.optString("remark"));
        hold.tv_billDate.setText(dateFormat.format(new Date(obj.optLong("billDate"))));
        hold.tv_position.setText(i + "");
        hold.iv_delete.setOnClickListener(new Delete(i));
    }

    @Override
    public int getCount() {
        return array == null ? 0 : array.length();
    }

    @Override
    public Object getItem(int position) {
        return array.optJSONObject(position);
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
