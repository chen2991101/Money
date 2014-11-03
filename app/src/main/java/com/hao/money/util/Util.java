package com.hao.money.util;

import android.view.View;
import android.widget.TextView;

import com.hao.money.R;

/**
 * 单例工具类
 * Created by hao on 2014/11/2.
 */
public class Util {
    private static Util util;

    private Util() {
    }

    /**
     * 工具方法获取实例
     *
     * @return
     */
    public static Util getInstance() {
        return util == null ? new Util() : util;
    }

    /**
     * 设置标题
     *
     * @param str  标题名称
     * @param view
     */
    public void setTitle(String str, View view) {
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(str);
    }
}
