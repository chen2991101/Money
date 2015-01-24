package com.hao.money.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hao.money.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.math.BigDecimal;

/**
 * 请求地址的工具类
 * Created by hao on 2014/11/2.
 */
public class UrlUtil {
    private static String url = "http://192.168.1.101";//基本的地址

    //上传位置
    public static String upLoadAddress = url + "/uploadAddress.do";
}
