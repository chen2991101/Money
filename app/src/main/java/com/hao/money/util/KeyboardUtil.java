package com.hao.money.util;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by hao on 2014/11/5.
 */
public class KeyboardUtil {

    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(et_initMoney, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 打开键盘
     *
     * @param activity
     * @param editText
     */
    public static void openKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }
}
