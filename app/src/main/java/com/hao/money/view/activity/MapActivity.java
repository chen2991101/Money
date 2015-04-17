package com.hao.money.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hao.money.R;
import com.hao.money.adapter.SelectHistoryAdapter;
import com.hao.money.service.SelectHistoryService;
import com.hao.money.service.SelectHistoryView;
import com.hao.money.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BeforeTextChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Iterator;

/**
 * 选择历史用途的activity
 */
@EActivity(R.layout.activity_map)
public class MapActivity extends Activity {
    @ViewById
    MapView mv_map;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocClient;


    @AfterViews
    public void init() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        Util.setHead(this, "地图");

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mv_map.getMap();
        mv_map.showZoomControls(false);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListener());
        LocationClientOption localLocationClientOption = new LocationClientOption();
        localLocationClientOption.setOpenGps(true);
        localLocationClientOption.setCoorType("bd09ll");
        localLocationClientOption.setScanSpan(999);
        mLocClient.setLocOption(localLocationClientOption);
        mLocClient.start();
    }


    @Override
    protected void onDestroy() {

        mBaiduMap.setMyLocationEnabled(false);

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mv_map.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mv_map.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mv_map.onPause();
        mLocClient.stop();
        super.onPause();
    }

    private double locationLat;
    private double locationLng;
    boolean isFirstLoc = true;

    public void getNearByStore() {

        BitmapDescriptor localBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
        LatLng localLatLng = new LatLng(30.552650545909, 104.07337060826);
        Bundle localBundle = new Bundle();
        MarkerOptions localMarkerOptions =
                new MarkerOptions().position(localLatLng)
                        .icon(localBitmapDescriptor)
                        .zIndex(9).draggable(true).extraInfo(localBundle);
        mBaiduMap.addOverlay(localMarkerOptions);
    }

    public class MyLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation paramBDLocation) {
            System.out.println("***************");
            if ((paramBDLocation == null) || (mv_map == null)) return;

            locationLat = paramBDLocation.getLatitude();
            locationLng = paramBDLocation.getLongitude();
            MyLocationData localMyLocationData = new MyLocationData.Builder().accuracy(paramBDLocation.getRadius()).direction(100.0F).latitude(paramBDLocation.getLatitude()).longitude(paramBDLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(localMyLocationData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng localLatLng = new LatLng(paramBDLocation.getLatitude(), paramBDLocation.getLongitude());
                MapStatusUpdate localMapStatusUpdate1 = MapStatusUpdateFactory.zoomTo(15.0F);
                mBaiduMap.setMapStatus(localMapStatusUpdate1);
                MapStatusUpdate localMapStatusUpdate2 = MapStatusUpdateFactory.newLatLng(localLatLng);
                mBaiduMap.animateMapStatus(localMapStatusUpdate2);
            }
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

                public boolean onMarkerClick(Marker paramMarker) {
                    Bundle localBundle = paramMarker.getExtraInfo();
                    System.out.println("你好吗");
                    return false;

                }
            });
            mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                public void onMapClick(LatLng paramLatLng) {
                }

                public boolean onMapPoiClick(MapPoi paramMapPoi) {
                    return false;
                }
            });

            mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                public void onMapStatusChange(MapStatus paramMapStatus) {
                }

                public void onMapStatusChangeFinish(MapStatus paramMapStatus) {
                    LatLng localLatLng = mBaiduMap.getMapStatus().target;
                    locationLat = localLatLng.latitude;
                    locationLng = localLatLng.longitude;
                }

                public void onMapStatusChangeStart(MapStatus paramMapStatus) {
                }
            });
            getNearByStore();
        }
    }


}