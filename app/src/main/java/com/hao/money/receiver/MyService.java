package com.hao.money.receiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.hao.money.util.Util;

import org.androidannotations.annotations.EService;

/**
 * 我的定位服务
 * Created by hao on 2015/1/23.
 */
@EService
public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new MyReceiver_(), filter);
        Util.log("注册广播");
    }
}
