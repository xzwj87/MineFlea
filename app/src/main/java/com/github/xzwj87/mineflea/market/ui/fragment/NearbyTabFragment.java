package com.github.xzwj87.mineflea.market.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.ui.activity.NearbyGoodsActivity;
import com.github.xzwj87.mineflea.market.ui.bean.NearbyGoogsInfo;
import com.github.xzwj87.mineflea.market.ui.protocol.NearbyProtocol;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/9/16.
 */

public class NearbyTabFragment extends BaseFragment implements View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        AMap.InfoWindowAdapter, LocationSource,
        AMapLocationListener {
    public static final String TAG = NearbyTabFragment.class.getSimpleName();

    private MarkerOptions mMarkerOptions;
    private AMap aMap;
    private LatLng latlng = new LatLng(39.761, 116.434);

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private Marker mCurrentMarker;

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.clearMap)
    Button clearMapBtn;
    @BindView(R.id.resetMap)
    Button resetMapBtn;
    @BindView(R.id.iv_zan)
    ImageView iv_zan;
    @BindView(R.id.info_img)
    ImageView iv_info;
    @BindView(R.id.id_marker_info)
    RelativeLayout markerLy;

    public NearbyTabFragment() {
    }

    public static NearbyTabFragment newInstance() {
        NearbyTabFragment fragment = new NearbyTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate) {
        View root = inflater.inflate(R.layout.fragment_nearby_tab, container, false);
        ButterKnife.bind(this, root);
        mapView.onCreate(savedSate);
        init();
        return root;
    }

    private void init() {
        clearMapBtn.setOnClickListener(this);
        resetMapBtn.setOnClickListener(this);
        iv_zan.setOnClickListener(this);
        iv_info.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            addMarkersToMap();// 往地图上添加marker
        }
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        setUpMap();
    }

    //设置一些amap的属性
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    //设置定位的类型
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    //添加地图覆盖物
    private void addMarkersToMap() {
        mMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(mMarkerOptions);
        NearbyProtocol protocol = new NearbyProtocol();
        List<NearbyGoogsInfo> list = protocol.loadNearbyData();
        for (NearbyGoogsInfo info : list) {
            MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(info.getName())
                    .position(info.getLatlng()
                    ).draggable(true);
            Marker marker = aMap.addMarker(options);
            marker.setObject(info);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 清空地图上所有已经标注的marker
             */
            case R.id.clearMap:
                if (aMap != null) {
                    aMap.clear();
                }
                break;
            /**
             * 重新标注所有的marker
             */
            case R.id.resetMap:
                if (aMap != null) {
                    aMap.clear();
                    addMarkersToMap();
                }
                break;
            case R.id.info_img:
                Intent intent = new Intent(getActivity(), NearbyGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_zan:
                Toast.makeText(AppGlobals.getAppContext(), "点赞了", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLoaded() {

    }

    //覆盖物点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mCurrentMarker == null) {
            mCurrentMarker = marker;
        } else {
            if (mCurrentMarker.equals(marker)) {
                markerLy.setVisibility(View.GONE);
            } else {
                NearbyGoogsInfo info = (NearbyGoogsInfo) marker.getObject();
                popupInfo(markerLy, info);
            }
        }
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }


    //根据info为布局上的控件设置信息
    protected void popupInfo(RelativeLayout mMarkerLy, NearbyGoogsInfo info) {
        mMarkerLy.setVisibility(View.VISIBLE);
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null) {
            viewHolder = new ViewHolder();
            viewHolder.infoImg = (ImageView) mMarkerLy
                    .findViewById(R.id.info_img);
            viewHolder.infoName = (TextView) mMarkerLy
                    .findViewById(R.id.info_name);
            viewHolder.infoDistance = (TextView) mMarkerLy
                    .findViewById(R.id.info_distance);
            viewHolder.infoZan = (TextView) mMarkerLy
                    .findViewById(R.id.info_zan);

            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        viewHolder.infoImg.setImageResource(info.getImgId());
        viewHolder.infoDistance.setText(info.getDistance());
        viewHolder.infoName.setText(info.getName());
        viewHolder.infoZan.setText(info.getZan() + "");
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                //mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amapLocation, 18));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    //启动定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(AppGlobals.getAppContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    //关闭定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private class ViewHolder {
        ImageView infoImg;
        TextView infoName;
        TextView infoDistance;
        TextView infoZan;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    //获取两点的距离
    public String getDistance(LatLng l1, LatLng l2) {
        return AMapUtils.calculateLineDistance(l1, l2) + "";
    }

}
