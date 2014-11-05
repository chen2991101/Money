package com.hao.money.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
     * @param cancelable
     */
    public static void showDialog(Context context, View view, boolean cancelable) {
        dialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    /**
     * 关闭对话框
     */
    public static void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 显示提示框
     *
     * @param context
     * @param str
     */
    public static void showToast(Context context, String str) {
        Toast.makeText(context, str,
                android.widget.Toast.LENGTH_SHORT).show();
    }
}
