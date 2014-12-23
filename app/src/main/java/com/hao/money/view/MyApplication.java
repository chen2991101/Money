package com.hao.money.view;

import android.app.Application;
import android.content.IntentFilter;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hao.money.receiver.MyReceiver;
import com.hao.money.receiver.MyReceiver_;

import org.androidannotations.annotations.EApplication;

/**
 * Created by Administrator on 2014-12-16.
 */
@EApplication
public class MyApplication extends Application {
    public LocationClient mLocationClient = null;
    public BDLocationListener addressListener = null;//主页面的事件

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式,暂时不要gps定位，
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setOpenGps(false);
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);

        /**
         * 绑定联网改变的事件
         */
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        MyReceiver_ receiver = new MyReceiver_();
        registerReceiver(receiver, filter);
    }
}
