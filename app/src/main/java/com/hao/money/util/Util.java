package com.hao.money.util;

import android.view.View;
import android.widget.TextView;

import com.hao.money.R;

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
}
