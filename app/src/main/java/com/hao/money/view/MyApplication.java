package com.hao.money.view;

import android.app.Application;
import android.graphics.Bitmap;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.hao.money.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
        initImage();
    }


    /**
     * 设置图片的配置
     *
     * @return
     */
    public DisplayImageOptions getImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
//如Bitmap.Config.ARGB_8888
                .showImageOnLoading(R.drawable.ic_launcher)   //默认图片
                .showImageForEmptyUri(R.drawable.ic_launcher)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.drawable.ic_launcher)// 加载失败显示的图片
                .displayer(new RoundedBitmapDisplayer(100))  //圆角，不需要请删除
                .build();
        return options;
    }


    private void initImage() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
//如Bitmap.Config.ARGB_8888
                .showImageOnLoading(R.drawable.ic_launcher)   //默认图片
                .showImageForEmptyUri(R.drawable.ic_launcher)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.drawable.ic_launcher)// 加载失败显示的图片
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)// 缓存在内存的图片的宽和高度
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
                .discCacheSize(50 * 1024 * 1024).  //缓存到文件的最大数据
                        discCacheFileCount(1000)            //文件数量
                .defaultDisplayImageOptions(options).  //上面的options对象，一些属性配置
                        build();
        ImageLoader.getInstance().init(config); //初始化
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
