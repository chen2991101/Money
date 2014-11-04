package com.hao.money.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.hao.money.R;

/**
 * 提示的工具类
 * Created by hao on 2014/11/4.
 */
public class Prompt {

    private static Dialog dialog;//展示的弹窗

    /**
     * 显示自定义的dialog
     *
     * @param context
     * @param view
     */
    public static void showDialog(Context context, View view) {
        dialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        dialog.setContentView(view);
        dialog.show();
    }
}
