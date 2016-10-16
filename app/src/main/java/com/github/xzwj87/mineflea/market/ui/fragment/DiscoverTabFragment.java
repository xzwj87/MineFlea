package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.NearbyGoodsInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/9/16.
 */

public class DiscoverTabFragment extends BaseFragment implements View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        AMap.InfoWindowAdapter {
    public static final String TAG = DiscoverTabFragment.class.getSimpleName();

    private MarkerOptions mMarkerOptions;
    private AMap aMap;
    private LatLng latlng = new LatLng(39.761, 116.434);

    @BindView(R.id.map) MapView mapView;
    @BindView(R.id.clearMap) Button clearMapBtn;
    @BindView(R.id.resetMap) Button resetMapBtn;
    @BindView(R.id.iv_zan) ImageView iv_zan;
    @BindView(R.id.info_img) ImageView iv_info;

    public DiscoverTabFragment() {
    }

    public static DiscoverTabFragment newInstance() {
        DiscoverTabFragment fragment = new DiscoverTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate) {
        View root = inflater.inflate(R.layout.fragment_discover_tab, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    private void init() {
        Log.v(TAG,"init()");
        clearMapBtn.setOnClickListener(this);
        resetMapBtn.setOnClickListener(this);
        iv_zan.setOnClickListener(this);
        iv_info.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            addMarkersToMap();// 往地图上添加marker
        }
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

    }

    private void addMarkersToMap() {
        mMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(mMarkerOptions);

        for (NearbyGoodsInfo info : NearbyGoodsInfo.infos){
            MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(info.getName())
                    .position(info.getLatlng()
                    ).draggable(true);
            aMap.addMarker(options);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(AppGlobals.getAppContext(), "dianji", Toast.LENGTH_SHORT).show();
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


    /**
     * 根据info为布局上的控件设置信息
     *
     * @param mMarkerInfo2
     * @param info
     */
//    protected void popupInfo(RelativeLayout mMarkerLy, Info info)
//    {
//        ViewHolder viewHolder = null;
//        if (mMarkerLy.getTag() == null)
//        {
//            viewHolder = new ViewHolder();
//            viewHolder.infoImg = (ImageView) mMarkerLy
//                    .findViewById(R.id.info_img);
//            viewHolder.infoName = (TextView) mMarkerLy
//                    .findViewById(R.id.info_name);
//            viewHolder.infoDistance = (TextView) mMarkerLy
//                    .findViewById(R.id.info_distance);
//            viewHolder.infoZan = (TextView) mMarkerLy
//                    .findViewById(R.id.info_zan);
//
//            mMarkerLy.setTag(viewHolder);
//        }
//        viewHolder = (ViewHolder) mMarkerLy.getTag();
//        viewHolder.infoImg.setImageResource(info.getImgId());
//        viewHolder.infoDistance.setText(info.getDistance());
//        viewHolder.infoName.setText(info.getName());
//        viewHolder.infoZan.setText(info.getZan() + "");
//    }

    private class ViewHolder
    {
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


}
