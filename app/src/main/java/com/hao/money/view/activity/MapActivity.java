package com.hao.money.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.BNaviEngineManager;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams;
import com.hao.money.R;
import com.hao.money.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

/**
 * 选择历史用途的activity
 */
@EActivity(R.layout.activity_map)
public class MapActivity extends Activity implements SensorEventListener {
    @ViewById
    MapView mv_map;
    @SystemService
    SensorManager sensorManager;
    private Sensor sensor;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocClient;
    private double angle, lat, lon;
    private RoutePlanSearch mSearch;
    private LatLng toLatLng;


    @AfterViews
    public void init() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        Util.setHead(this, "地图");

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mv_map.getMap();

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mv_map.showZoomControls(false);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListener());
        LocationClientOption localLocationClientOption = new LocationClientOption();
        localLocationClientOption.setOpenGps(true);
        localLocationClientOption.setCoorType("bd09ll");
        localLocationClientOption.setScanSpan(1000);
        localLocationClientOption.setNeedDeviceDirect(true);
        mLocClient.setLocOption(localLocationClientOption);
        mLocClient.start();
        sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);


        //定义Maker坐标点104.69965,31.470188
        LatLng point = new LatLng(31.470188, 104.69965);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_launcher);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);


        BaiduNaviManager.getInstance().
                initEngine(this, getSdcardDir(), mNaviEngineInitListener, "hDlYYUiviASlv61gIna5UEGi", null);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println("你好，我点击了");
                PlanNode from = PlanNode.withLocation(new LatLng(lat, lon));
                toLatLng = marker.getPosition();
                PlanNode to = PlanNode.withLocation(toLatLng);

                mSearch = RoutePlanSearch.newInstance();
                mSearch.setOnGetRoutePlanResultListener(listener);
                mSearch.drivingSearch(new DrivingRoutePlanOption().from(from).to(to));
                return false;
            }
        });
    }


    private class MyTransitRouteOverlay extends DrivingRouteOverlay {
        public MyTransitRouteOverlay(BaiduMap paramBaiduMap) {
            super(paramBaiduMap);
        }

        public BitmapDescriptor getStartMarker() {
            return null;
        }

        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }


    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            //

        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
          /*  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }*/
        }

        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //
            mSearch.destroy();
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                System.out.println(result.error);
                Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }


            BaiduNaviManager.getInstance().launchNavigator(MapActivity.this,
                    lat, lon, "百度大厦",
                    toLatLng.latitude, toLatLng.longitude, "北京天安门",
                    RoutePlanParams.NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,       //算路方式
                    true,                                            //真实导航
                    BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //在离线策略
                    new BaiduNaviManager.OnStartNavigationListener() {                //跳转监听

                        @Override
                        public void onJumpToNavigator(Bundle configParams) {
                            Intent intent = new Intent(MapActivity.this, NavigationActivity.class);
                            intent.putExtras(configParams);
                            startActivity(intent);
                        }

                        @Override
                        public void onJumpToDownloader() {
                        }
                    });
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBaiduMap.setMyLocationEnabled(false);

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mv_map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mv_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mv_map.onPause();
        mLocClient.stop();
    }

    boolean isFirstLoc = true;


    @Override
    public void onSensorChanged(SensorEvent event) {
        angle = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mv_map == null)
                return;

            lat = location.getLatitude();
            lon = location.getLongitude();

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(Math.round(angle)).latitude(lat)
                    .longitude(lon).build();
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {

                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
    }


    private boolean mIsEngineInitSuccess = false;
    private BNaviEngineManager.NaviEngineInitListener mNaviEngineInitListener = new BNaviEngineManager.NaviEngineInitListener() {
        public void engineInitSuccess() {
            //导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航
            mIsEngineInitSuccess = true;
        }

        public void engineInitStart() {
        }

        public void engineInitFail() {
        }
    };

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


}