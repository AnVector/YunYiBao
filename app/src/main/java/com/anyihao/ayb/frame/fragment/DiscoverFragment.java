package com.anyihao.ayb.frame.fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.MerchantListBean;
import com.anyihao.ayb.bean.MerchantListBean.DataBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.RadarActivity;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends ABaseFragment implements OnMarkerClickListener,
        LocationSource, AMapLocationListener {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.imv_my_location)
    ImageView imvMyLocation;
    Unbinder unbinder;
    private OnLocationChangedListener mListener;
    //声明mlocationClient对象
    private AMapLocationClient mLocationClient;
    private UiSettings mUiSettings;
    private AMap mAmap;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private boolean needCheck = true;
    private static final int RC_LOCATION_PERM = 0x0001;

    protected void initData() {
        titleMid.setText(getString(R.string.service_location));
        toolbar.setNavigationIcon(null);
        initAMap();
        initUiSettings();
        initLocationStyle();
        initLocation();
        getMerchantList();
    }

    private void initAMap() {
        if (mAmap != null) {
            mAmap.clear();
        }
        mAmap = mMapView.getMap();
        //设置定位监听
        mAmap.setLocationSource(this);
    }

    private void initUiSettings() {
        mUiSettings = mAmap.getUiSettings();
        // 是否显示定位按钮
        mUiSettings.setMyLocationButtonEnabled(false);
        //设置地图logo显示位置
        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// 设置地图logo显示在右下方
        //设置地图是否可以手势滑动
        mUiSettings.setScrollGesturesEnabled(true);
        //设置地图是否可以手势缩放大小
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setZoomControlsEnabled(false);
    }

    private void initLocationStyle() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable
                .icon_location));
        myLocationStyle.interval(20 * 1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(android.R.color.transparent);
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeWidth(0);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.showMyLocation(true);
        mAmap.setMyLocationStyle(myLocationStyle);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAmap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                if (isFirstLoc) {// 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                    //设置缩放级别
                    mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation
                            .getLatitude(), aMapLocation.getLongitude())));
                    isFirstLoc = false;
                }
                mListener.onLocationChanged(aMapLocation);
            } else {
                Logger.d("location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    //自定义一个图钉
    private void generateMarkerOptions(List<DataBean> beans) {
        MarkerOptions options;
        for (DataBean bean : beans) {
            options = new MarkerOptions();
            //设置图钉选项
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_dot))//图标
                    .position(new LatLng(bean.getLatitude(), bean.getLongitude()))
                    .draggable(false)
                    .visible(true)
                    .period(60);//设置多少帧刷新一次图片资源
            mAmap.addMarker(options).setObject(bean);
        }
    }

    private void initLocation() {
        //声明mLocationOption对象
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60 * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //给定位客户端对象设置定位参数
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }

    @Override
    protected void initEvent() {
        mAmap.setOnMarkerClickListener(this);
        imvMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AMapLocation aMapLocation = mLocationClient
                        .getLastKnownLocation();
                if (aMapLocation != null) {
                    mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation
                            .getLatitude(), aMapLocation.getLongitude())));
                }
            }
        });
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            initLocation();
        }
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void saveInstanceState(Bundle savedInstanceState) {
        super.saveInstanceState(savedInstanceState);
        if (mMapView == null)
            return;
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        if (actionType == 0) {
            MerchantListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    MerchantListBean.class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                List<DataBean> beans = bean.getData();
                if (beans.size() > 0) {
                    generateMarkerOptions(beans);
                }
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mAmap != null) {
//            jumpPoint(marker);
            if (marker.getObject() instanceof DataBean) {
                DataBean bean = (DataBean) marker.getObject();
                Intent intent = new Intent(mContext, RadarActivity.class);
                intent.putExtra("shopAddr", bean.getShopAddr());
                intent.putExtra("shopName", bean.getShopName());
                intent.putExtra("contact", bean.getContact());
                startActivity(intent);
            }

        }
        return true;
    }

    /**
     * marker点击动画
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mAmap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1000;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstLoc = true;
        permissionsRequest();
        if (mMapView == null)
            return;
        mMapView.onResume();
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    protected void permissionsRequest() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };
        if (EasyPermissions.hasPermissions(mContext, permissions)) {
            if (mLocationClient != null) {
                //启动定位
                mLocationClient.startLocation();
                //将地图移动到定位点
                AMapLocation aMapLocation = mLocationClient
                        .getLastKnownLocation();
                if (aMapLocation != null) {
                    mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation
                            .getLatitude
                                    (), aMapLocation.getLongitude())));
                }
            }
        } else {
            if (needCheck) {
                EasyPermissions.requestPermissions(this, "开启定位",
                        RC_LOCATION_PERM, permissions);
                needCheck = false;
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView == null)
            return;
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView == null)
            return;
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Logger.d(perms);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAmap = null;
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    private void getMerchantList() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "MERCHANTLIST");
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder().
                setPage(1)
                .setActionType(0)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setTaskType(TaskType.Method.POST)
                .createTask());
    }
}
