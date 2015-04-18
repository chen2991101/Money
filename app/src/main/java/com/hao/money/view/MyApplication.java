package com.hao.money.view;

import android.app.Application;
import android.content.IntentFilter;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.hao.money.receiver.MyReceiver;
import com.hao.money.receiver.MyReceiver_;

import org.androidannotations.annotations.EApplication;

/**
 * Created by Administrator on 2014-12-16.
 */
@EApplication
public class MyApplication extends Application {
    public LocationClient mLocationClient = null;
    public BDLocationListener addressListener = null, jzAddressListener = null;//主页面的事件

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        //initLocationClient();
    }

    private LocationClient locationClient;

    private void initLocationClient() {
        if (locationClient != null) return;
        locationClient = new LocationClient(this);
        LocationClientOption localLocationClientOption = new LocationClientOption();
        localLocationClientOption.setOpenGps(true);
        localLocationClientOption.setAddrType("all");
        localLocationClientOption.setCoorType("bd09ll");
        localLocationClientOption.setScanSpan(1000);
        locationClient.setLocOption(localLocationClientOption);
    }

    public LocationClient getLocation() {
        if (mLocationClient != null) {
            return mLocationClient;
        }
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式,暂时不要gps定位，
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setOpenGps(false);
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        return mLocationClient;

    }

    /**
     * 取消定位注册事件
     */
    public void unRegisterListener() {
        if (addressListener != null) {
            mLocationClient.unRegisterLocationListener(addressListener);
            addressListener = null;
        }
        if (jzAddressListener != null) {
            mLocationClient.unRegisterLocationListener(jzAddressListener);
            jzAddressListener = null;
        }
    }

}
