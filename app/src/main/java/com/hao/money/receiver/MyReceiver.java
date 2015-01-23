package com.hao.money.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hao.money.util.UrlUtil;
import com.hao.money.util.Util;
import com.hao.money.view.MyApplication;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.SystemService;
import org.apache.http.Header;

/**
 * 自己实现的广播接受者
 * Created by Administrator on 2014-12-23.
 */
@EReceiver
public class MyReceiver extends BroadcastReceiver {
    @SystemService
    ConnectivityManager connectivityManager;
    @SystemService
    TelephonyManager telephonyManager;
    @App
    MyApplication application;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
    }

    /**
     * 网络状态改变的监听事件
     *
     * @param intent 意图
     */
    @ReceiverAction("android.net.conn.CONNECTIVITY_CHANGE")
    public void connectivityReceiver(Intent intent) {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            application.unRegisterListener();//取消所有绑定
            application.addressListener = new AddressListener();
            application.mLocationClient.registerLocationListener(application.addressListener);
            application.mLocationClient.start();
        }
    }


    /**
     * 修改网络链接的广播
     *
     * @param intent
     */
    @ReceiverAction("android.net.conn.CONNECTIVITY_CHANGE")
    public void connectivity(Intent intent) {
        Util.log("修改了网络链接");
    }


    /**
     * 用户解锁的广播
     *
     * @param intent
     */
    @ReceiverAction("android.intent.action.USER_PRESENT")
    public void userPresent(Intent intent) {
        context.startService(new Intent(context, MyService_.class));//添加服务
    }

    /**
     * 定位回调事件
     */
    private class AddressListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                application.mLocationClient.stop();//取消定位服务
                return;
            }
            String address = location.getAddrStr();
            if (address != null) {
                RequestParams params = new RequestParams();
                params.put("address", address);
                params.put("latitude", location.getLatitude());
                params.put("longitude", location.getLongitude());
                params.put("deviceId", telephonyManager.getDeviceId());//手机的唯一编码
                Util.post(UrlUtil.upLoadAddress, params, new uploadHandler());
            }
            application.mLocationClient.stop();//取消定位服务
        }
    }

    /**
     * 上传地址的回调，不做任务处理
     */
    private class uploadHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        }
    }
}
