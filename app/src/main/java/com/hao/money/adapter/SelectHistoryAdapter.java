package com.hao.money.adapter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.entity.History;
import com.hao.money.service.SelectHistoryService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * 选择历史的activity的适配器
 * Created by Administrator on 2014-11-17.
 */
public class SelectHistoryAdapter extends MyAdapter<History> {
    private SelectHistoryService service;

    public SelectHistoryAdapter(Context context, List list, SelectHistoryService service) {
        super(context, list);
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
            hold.vp_test = (ViewPager) view.findViewById(R.id.vp_test);
            view.setTag(hold);
        } else {
            hold = (HoldView) view.getTag();
        }

        History history = list.get(i);
        hold.tv_name.setText(history.getName());
        hold.tv_count.setText(history.getCount() + "");
        hold.bt_delete.setOnClickListener(new Click(i));
        hold.vp_test.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                System.out.println("********");
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageResource(R.drawable.ic_launcher);
                container.addView(imageView);
                return imageView;
            }
        });

        return view;
    }

    private class HoldView {
        TextView tv_name, tv_count;
        Button bt_delete;
        ViewPager vp_test;
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
