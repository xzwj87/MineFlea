package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.NearbyGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.presenter.NearbyGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.NearbyGoodsView;
import com.github.xzwj87.mineflea.market.ui.activity.HomeActivity;
import com.github.xzwj87.mineflea.market.ui.activity.NearbyGoodsActivity;
import com.github.xzwj87.mineflea.market.ui.alimap.BusResultListAdapter;
import com.github.xzwj87.mineflea.market.ui.alimap.DriveRouteDetailActivity;
import com.github.xzwj87.mineflea.market.ui.alimap.DrivingRouteOverLay;
import com.github.xzwj87.mineflea.market.ui.alimap.WalkRouteDetailActivity;
import com.github.xzwj87.mineflea.utils.AMapUtil;
import com.github.xzwj87.mineflea.utils.Constants;
import com.github.xzwj87.mineflea.utils.ToastUtil;
import com.github.xzwj87.mineflea.utils.UiUtils;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


//Created by jason on 10/9/16.
public class NearbyTabFragment extends BaseFragment implements NearbyGoodsView, View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener, AMap.InfoWindowAdapter, LocationSource,
        AMapLocationListener, RouteSearch.OnRouteSearchListener, AMap.OnMapClickListener {
    public static final String TAG = "[NearbyTabFragment]";

    @Inject
    NearbyGoodsPresenterImpl mPresenter;

    private AMap aMap;

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.bottom_layout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.bus_result)
    LinearLayout mBusResultLayout;
    @BindView(R.id.firstline)
    TextView mRotueTimeDes;
    @BindView(R.id.secondline)
    TextView mRouteDetailDes;
    @BindView(R.id.route_drive)
    ImageView mDrive;
    @BindView(R.id.route_bus)
    ImageView mBus;
    @BindView(R.id.route_walk)
    ImageView mWalk;
    @BindView(R.id.route_CrosstownBus)
    TextView mCrossBus;
    @BindView(R.id.bus_result_list)
    ListView mBusResultList;
    @BindView(R.id.fam_search)
    FloatingActionButton fam_search;
    @BindView(R.id.fam_reset)
    FloatingActionButton fam_reset;
    @BindView(R.id.multiple_actions_left)
    FloatingActionsMenu fam_menu;

    private ProgressDialog progDialog = null;// 搜索时进度条

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private LatLng myLocation;

    private long distance = 0;
    private int selected = 0;

    //private MapView mapView;

    private EditText etSearchDis;
    private Spinner spinner;

    private List<PublishGoodsInfo> list;
    //private List<NearbyGoodsInfo> list;
    private BroadcastReceiver receiver;

    private static final int KM = 1;
    private static final int M = 0;

    private LatLonPoint mStartPoint_bus = new LatLonPoint(40.818311, 111.670801);//起点，111.670801,40.818311
    private LatLonPoint mEndPoint_bus = new LatLonPoint(44.433942, 125.184449);//终点，
    private String mCurrentCityName = "北京";

    private AMapLocation mCurrentAmapLocation;

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void finishView() {
    }

    @Override
    public void updateMarkerDisplay(List<PublishGoodsInfo> list) {
        this.list = list;
        addMarkersToMap();
    }

    //广播接收器
    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.COM_SEARCH_GOODS_ACTION) {
                showSearchDialog();
            }
        }
    }

    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    private final int ROUTE_TYPE_DRIVE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedSate) {
        View root = inflater.inflate(R.layout.fragment_nearby_tab, container, false);
        ButterKnife.bind(this, root);
        mPresenter.setView(this);
        mPresenter.init();
        mapView = (MapView) root.findViewById(R.id.map);
        mapView.onCreate(savedSate);
        initView(root);
        loadData();
        return root;
    }

    //加载数据
    private void loadData() {
        //NearbyProtocol protocol = new NearbyProtocol();
        //list = protocol.loadNearbyData();
        //addMarkersToMap();
        mPresenter.loadDataFromServer();
    }

    private void initView(View root) {
        setUpMap();
        setupLocationStyle();
        registerReceiver();
        setSearchView(root);
    }

    private void setSearchView(View root) {
        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
        mDrive.setOnClickListener(this);
        mBus.setOnClickListener(this);
        mWalk.setOnClickListener(this);
        mCrossBus.setOnClickListener(this);
        fam_search.setOnClickListener(this);
        fam_reset.setOnClickListener(this);
    }

    //设置一些amap的属性
    private void setUpMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setScaleControlsEnabled(true);//显示比例尺控件
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMapClickListener(this);
    }

    //设置定位的类型
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        //myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        //myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        //myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        aMap.setMyLocationStyle(myLocationStyle);
    }

    //注册广播监听
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(Constants.COM_SEARCH_GOODS_ACTION);
        receiver = new Receiver();
        getActivity().registerReceiver(receiver, filter);
    }

    //取消广播监听
    private void unRegisterReceiver() {
        getActivity().unregisterReceiver(receiver);
    }

    //设置搜索的弹框
    private void showSearchDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        View searchView = inflater.inflate(R.layout.nearby_fragment_search_dialog, null, false);
        etSearchDis = (EditText) searchView.findViewById(R.id.etSearchDis);
        Button btn_ok = (Button) searchView.findViewById(R.id.btn_ok);
        Button btn_cl = (Button) searchView.findViewById(R.id.btn_cancel);
        spinner = (Spinner) searchView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),               //上下文的引用
                R.array.planets_array,              //参数引用了string。xml文档中的String数组
                android.R.layout.simple_spinner_item);//指定Spinner的样式，是一个布局id.由android系统据顶。也可以自己定义
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialog.setView(searchView);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSearchDis.getText().toString())) {
                    Toast.makeText(getActivity(), "请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                distance = Integer.valueOf(etSearchDis.getText().toString());
                selected = spinner.getSelectedItemPosition();
                setMarkerInDis(distance, selected);
            }
        });
        btn_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.route_bus:
                onBusClick();
                break;
            case R.id.route_drive:
                onDriveClick();
                break;
            case R.id.route_walk:
                onWalkClick();
                break;
            case R.id.route_CrosstownBus:
                onCrosstownBusClick();
                break;
            case R.id.fam_reset:
                fam_menu.toggle();
                ToastUtil.show(AppGlobals.getAppContext(), "clear_map");
                resetMap();
                break;
            case R.id.fam_search:
                fam_menu.toggle();
                ToastUtil.show(AppGlobals.getAppContext(), "fam_search");
                showSearchDialog();
                break;
            default:
                break;
        }
    }

    //地图重新设置
    private void resetMap() {
        clearMap();
        addMarkersToMap();
        getLocation();
    }

    public void onBusClick() {
        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_select);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.GONE);
        mBusResultLayout.setVisibility(View.VISIBLE);
    }

    public void onDriveClick() {
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        mDrive.setImageResource(R.drawable.route_drive_select);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
    }

    public void onWalkClick() {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_select);
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
    }

    public void onCrosstownBusClick() {
        searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BusDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.GONE);
        mBusResultLayout.setVisibility(View.VISIBLE);
    }

    //开始搜索路径规划方案
    public void searchRouteResult(int routeType, int mode) {
        mStartPoint = new LatLonPoint(myLocation.latitude, myLocation.longitude);
        if (mStartPoint == null) {
            ToastUtil.show(AppGlobals.getAppContext(), "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(AppGlobals.getAppContext(), "终点未设置");
            return;
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        } else if (routeType == ROUTE_TYPE_CROSSTOWN) {
            RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(mStartPoint_bus, mEndPoint_bus);
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo_bus, mode, "呼和浩特市", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            query.setCityd("农安县");
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }

    public NearbyTabFragment() {
    }

    public static NearbyTabFragment newInstance() {
        NearbyTabFragment fragment = new NearbyTabFragment();
        return fragment;
    }

    //添加地图覆盖物
    private void addMarkersToMap() {
        if (list == null) {
            ToastUtil.showToast("附近没有数据");
            return;
        }
        if (list.size() <= 0)
            return;
        for (PublishGoodsInfo info : list) {
            LatLng pos = new LatLng(info.getLng(), info.getLat());
            MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(info.getName()).position(pos).draggable(true);
            Marker marker = aMap.addMarker(options);
            marker.setObject(info);
        }
    }

    //搜索指定范围的物品
    private void setMarkerInDis(long dis, int select) {
        Log.e(TAG, "setMarkerInDis");
        if (aMap != null) {
            aMap.clear();
            //markerLy.setVisibility(View.INVISIBLE);
            for (PublishGoodsInfo info : list) {
                LatLng pos = new LatLng(info.getLng(), info.getLat());
                double l = UiUtils.getDistanceM(myLocation, pos);
                if (select == KM) {
                    l = UiUtils.getDistanceKm(myLocation, pos);
                }
                if (l <= dis) {
                    MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(info.getName()).position(pos).draggable(true);
                    Marker marker = aMap.addMarker(options);
                    marker.setObject(info);
                }
            }
            getLocation();
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
        NearbyGoodsInfo info = (NearbyGoodsInfo) marker.getObject();
        showGoodsInfoDialog(info);
        showRoutGuide(marker);
        mEndPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
        return false;
    }

    private void showRoutGuide(Marker marker) {
        LatLng latlng = marker.getPosition();
        LatLonPoint start = new LatLonPoint(myLocation.latitude, myLocation.longitude);
        LatLonPoint end = new LatLonPoint(latlng.latitude, latlng.longitude);
        RouteSearch.FromAndTo fromTo = new RouteSearch.FromAndTo(start, end);
    }

    //显示点击物品的信息
    private void showGoodsInfoDialog(NearbyGoodsInfo info) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();

        View searchView = inflater.inflate(R.layout.nearby_fragment_goods_dialog, null, false);
        ImageView goodsPicIv = (ImageView) searchView.findViewById(R.id.goods_pic_iv);
        TextView goodsDisTv = (TextView) searchView.findViewById(R.id.goods_dis_tv);
        TextView goodsPriseTv = (TextView) searchView.findViewById(R.id.goods_prise_tv);
        TextView goodsNameTv = (TextView) searchView.findViewById(R.id.goods_name_tv);
        ImageView goodsPriseIv = (ImageView) searchView.findViewById(R.id.goods_prise_iv);
        Button btnOK = (Button) searchView.findViewById(R.id.goods_btn_ok);
        ImageButton btnCl = (ImageButton) searchView.findViewById(R.id.goods_btn_cancel);

        goodsPicIv.setImageResource(info.getImgId());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        goodsDisTv.setText(decimalFormat.format(UiUtils.getDistanceKm(myLocation, info.getLatlng())) + "km");
        goodsPriseTv.setText(info.getZan() + "");
        goodsNameTv.setText(info.getName());

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), NearbyGoodsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        btnCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        dialog.setView(searchView);
        dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        dialog.show();
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

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                setupLocationStyle();
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mCurrentAmapLocation = amapLocation;
                myLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                Log.e(TAG, myLocation.toString());
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
        getLocation();
    }

    private void getLocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(AppGlobals.getAppContext());
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //是否需要返回地址
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次，默认为false
            mLocationOption.setOnceLocation(false);
            //设置是否强制刷新wifi,默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔，单位毫秒，默认为2000ms
            mLocationOption.setInterval(20000);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }
        mlocationClient.startLocation();
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

    //方法必须重写
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    //方法必须重写
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mPresenter.onPause();
    }

    //方法必须重写
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //方法必须重写
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unRegisterReceiver();
        mPresenter.onDestroy();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mBottomLayout.setVisibility(View.GONE);
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    clearMap();
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(getActivity(), mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(AppGlobals.getAppContext(), R.string.no_result);
                }
            } else {
                ToastUtil.show(AppGlobals.getAppContext(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(AppGlobals.getAppContext(), errorCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    clearMap();
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            getActivity(), aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约" + taxiCost + "元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivityForResult(intent, HomeActivity.DRIVE_ACTIVITY_CODE);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(AppGlobals.getAppContext(), R.string.no_result);
                }

            } else {
                ToastUtil.show(AppGlobals.getAppContext(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(AppGlobals.getAppContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    clearMap();
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            getActivity(), aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),
                                    WalkRouteDetailActivity.class);
                            intent.putExtra("walk_path", walkPath);
                            intent.putExtra("walk_result",
                                    mWalkRouteResult);
                            getActivity().startActivityForResult(intent, HomeActivity.WALK_ACTIVITY_CODE);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(AppGlobals.getAppContext(), R.string.no_result);
                }

            } else {
                ToastUtil.show(AppGlobals.getAppContext(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(AppGlobals.getAppContext(), errorCode);
        }
    }

    private void clearMap() {
        if (aMap != null) {
            aMap.clear();// 清理地图上的所有覆盖物
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    }

    //显示进度框
    private void showProgressDialog() {
        if (!getActivity().isFinishing()) {
            if (progDialog == null)
                progDialog = new ProgressDialog(getActivity());
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setIndeterminate(false);
            progDialog.setCancelable(true);
            progDialog.setMessage("正在搜索");
            progDialog.show();
        }
    }

    //隐藏进度框
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

}