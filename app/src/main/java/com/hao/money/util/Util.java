package com.hao.money.util;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.hao.money.R;
import com.hao.money.view.activity.MainActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 单例工具类
 * Created by hao on 2014/11/2.
 */
public class Util {

    /**
     * 设置标题
     *
     * @param str  标题名称
     * @param view
     */
    public static void setTitle(String str, View view) {
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(str);
    }


    /**
     * 设置带返回按钮的标题
     *
     * @param activity
     * @param titleText
     */
    public static void setHead(final Activity activity, String titleText) {
        TextView back = (TextView) activity.findViewById(R.id.tv_back);
        TextView tv = (TextView) activity.findViewById(R.id.tv_title);
        tv.setText(titleText);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }


    /**
     * 计算sumMoney的值
     *
     * @param m
     * @param oldMoney
     * @param isAdd
     */
    public static String updateSumMoney(BigDecimal m, BigDecimal oldMoney, boolean isAdd) {
        if (isAdd) {
            oldMoney = oldMoney.add(m);
        } else {
            oldMoney = oldMoney.subtract(m);
        }
        return oldMoney.setScale(2, 4).toString();
    }
}
