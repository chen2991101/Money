package com.hao.money.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自己实现的异步请求的回调
 */
public abstract class ResponseHandler extends AsyncHttpResponseHandler {
    protected Context context;

    public ResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        try {
            JSONObject jsonObject = new JSONObject(new String(bytes));
            if (jsonObject.optBoolean("success")) {
                successMethod(jsonObject);
            } else {
                Prompt.showToast(context, "请求失败，" + jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Prompt.showToast(context, "请求失败，请稍后重试");
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        Prompt.showToast(context, "请求失败，请检查网络");
    }

    public abstract void successMethod(JSONObject jsonObject);//请求成功执行的方法
}
