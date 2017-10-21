package com.ananda.tailing.bike.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.CoordinateConverter.CoordType;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.share.ShareSearch.ShareFromAndTo;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.more.LoginActivity;
import com.ananda.tailing.bike.data.BaseResponse;
import com.ananda.tailing.bike.data.CarInfoResponse;
import com.ananda.tailing.bike.data.Constants;
import com.ananda.tailing.bike.data.PathInfo;
import com.ananda.tailing.bike.data.PathResponse;
import com.ananda.tailing.bike.entity.CarInfo;
import com.ananda.tailing.bike.net.HttpExecute;
import com.ananda.tailing.bike.net.HttpRequest;
import com.ananda.tailing.bike.net.HttpResponseListener;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.TitleBarView;
import com.fu.baseframe.net.CallServer;
import com.fu.baseframe.net.CustomDataRequest;
import com.fu.baseframe.net.HttpListener;
import com.google.zxing.client.android.CaptureActivity;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_cloud_smart_control_layout)
public class CloudSmartControlActivity extends BaseActivity implements LocationSource,AMapLocationListener {

	@ViewById
	MapView mMapView = null;
	private OnLocationChangedListener mListener;
	private AMapLocation amapLocationCurr;
	//声明AMapLocationClient类对象
	private AMapLocationClient mLocationClient = null;
	//声明AMapLocationClientOption对象
	private AMapLocationClientOption mLocationOption = null;
	private final int REQUEST_SCAN_IMEI_CLOUD = 34;
	AMap aMap;
	protected Bundle savedInstanceState;
	
	@ViewById
	public TextView deviceIdTv,deviceStatusTv;
	
	@ViewById
	TitleBarView title_bar;
	
	@ViewById
	Button bindIMEIClick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
	}
	
	@Override
	protected void initWidget() {
		
	}
	
	@AfterViews
	public void initViews(){
		title_bar.setTitleLeftImg(R.drawable.icon_back, "云端控制");
		title_bar.setTitleClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
		MyApplication.DEVIDE_ID = PreferencesUtils.getString(this, "IMEI", "");
		initMap();
		if(MyApplication.DEVIDE_ID.isEmpty()){
			bindIMEIClick.setText("绑定");
			showInputDialog();
		}else{
			bindIMEIClick.setText("解绑");
		}
		showImei(MyApplication.DEVIDE_ID);
		
	}
	public void showImei(String imei){
		deviceIdTv.setText(MyApplication.DEVIDE_ID);
	}
	private void showInputDialog(){
		
		/*MyApplication.getInstance().dialogInputDeviceId(this,new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				getCarLocation();
				MyApplication.DEVIDE_ID = PreferencesUtils.getString(CloudSmartControlActivity.this, "IMEI", "");
				if(MyApplication.DEVIDE_ID.isEmpty()){
					bindIMEIClick.setText("绑定");
					showInputDialog();
				}else{
					bindIMEIClick.setText("解绑");
				}
				return false;
			}
		});*/
		Intent captureIntent = new Intent(this,CaptureActivity.class);
		captureIntent.putExtra("isFromCloud", true);
		startActivityForResult(captureIntent, REQUEST_SCAN_IMEI_CLOUD);
	}
	
	@Click
	public void bindIMEIClick(){
		showInputDialog();
	}
	
	@Click
	public void carLocationClick(){
		if(!MyApplication.DEVIDE_ID.isEmpty()){
			getCarLocation();
		}else{
			showInputDialog();
		}
		
	}
	
	@Click
	public void carStatusClick(){
		if(!MyApplication.DEVIDE_ID.isEmpty()){
			com.ananda.tailing.bike.activity.CarStatusActivity_.intent(this).start();
		}else{
			showInputDialog();
		}
		
	}
	
	@Click
	public void todayPathClick(){
		if(!MyApplication.DEVIDE_ID.isEmpty()){
			getPath();
		}else{
			showInputDialog();
		}
		
	}
	
	public void initMap(){
		aMap = mMapView.getMap();
		
		mMapView.onCreate(savedInstanceState);
		
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_my_gps));// 设置小蓝点的图标
		
		aMap.setMyLocationStyle(myLocationStyle);
		
		aMap.setOnMarkerClickListener(markerListener);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setOnMapLoadedListener(new OnMapLoadedListener() {
			
			@Override
			public void onMapLoaded() {
				UiSettings uiSettings = aMap.getUiSettings();
				uiSettings.setMyLocationButtonEnabled(false);
				uiSettings.setZoomControlsEnabled(false);
			}
		});
		
		aMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {

			}
		});
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null&& amapLocation.getErrorCode() == 0) {
				amapLocationCurr = amapLocation;
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				MyApplication.DEVIDE_ID = PreferencesUtils.getString(this, "IMEI", "");
				if(!MyApplication.DEVIDE_ID.isEmpty()){
					getCarLocation();
				}
			
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		startPostion();
	}


	@Override
	public void deactivate() {
		mListener = null;
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
		mLocationClient = null;
	}
	
	OnMarkerClickListener markerListener = new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			
			return true;
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState
		// (outState)，实现地图生命周期管理
		mMapView.onSaveInstanceState(outState);
	}
	
	public void startPostion(){
		if (mLocationClient == null) {
			mLocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mLocationClient.setLocationListener(this);
			
			//该方法默认为false。
			mLocationOption.setOnceLocation(true);
			
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mLocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mLocationClient.startLocation();
		}
	}
	
	private void getCarLocation(){
		
		String strUrl = String.format(Constants.BIKE_DETAIL_URL, MyApplication.DEVIDE_ID);
		/*Request<CarInfoResponse> request = new CustomDataRequest<CarInfoResponse>(strUrl,RequestMethod.GET,CarInfoResponse.class);
		request.setConnectTimeout(60 * 1000);
		request.setReadTimeout(60 * 1000);
		request.setHeader("context-Type", "application/json");
		request.setHeader("mobileNo", "");
		request.setHeader("mobiledeviceId", "");
		request.setHeader("accesstoken",  "");
		CallServer.getRequestInstance().add(this, strUrl.hashCode(), request, new HttpListener<CarInfoResponse>() {

			@Override
			public void onSucceed(int what, Response<CarInfoResponse> response) {
				if(response.isSucceed() && response.get() != null){
					if(response.get().data != null){
						
						CarInfo carInfo = response.get().data;
						deviceIdTv.setText(carInfo.imei);
						showCurrStatus(carInfo);
						
					}
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag, Exception exception, int responseCode,
					long networkMillis) {
				MyApplication.DEVIDE_ID  = "";
			}
		},this,true);*/
		HttpRequest<CarInfoResponse> httpRequest = new HttpRequest<CarInfoResponse>(this, strUrl, new HttpResponseListener<CarInfoResponse>() {

			@Override
			public void onResult(CarInfoResponse result) {
				if(result == null) return;
				
				if(result.statusCode == 200){
					if(result!=null){
						CarInfo carInfo = result.data;
						deviceIdTv.setText(carInfo.imei);
						showCurrStatus(carInfo);
					}
				}else{
					Toast.makeText(CloudSmartControlActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, CarInfoResponse.class, null, "GET", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
	}

	@SuppressWarnings("unused")
	private void showCurrStatus(CarInfo carInfo){
		if(carInfo == null) return ;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		StringBuffer statusSb = new StringBuffer();
		Double lat,lng;
		lat = carInfo.latitude;
		lng = carInfo.longitude;
		
		LatLng latLng = new  LatLng(lat, lng);
		
		if(lat == 0 || lng == 0){
			return;
		}
		if(latLng == null){
			return;
		}
		
		if(lat < 0 || lng< -1){
			return;
		}
		
		
		CoordinateConverter converter  = new CoordinateConverter();
		// CoordType.GPS 待转换坐标类型
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标点 DPoint类型
		converter.coord(latLng);
		// 执行转换操作
		LatLng desLatLng = converter.convert();
		
		
		aMap.moveCamera(CameraUpdateFactory.newLatLng(desLatLng));
		
		statusSb.append("时间:"+dateFormat.format(new Date())+"\n");
		
		drawMarker(desLatLng.latitude,desLatLng.longitude);
		
		getXYLocation(desLatLng.latitude, desLatLng.longitude, statusSb);
		
		deviceStatusTv.setText(statusSb.toString());
		
		
		aMap.moveCamera(CameraUpdateFactory.newLatLng(desLatLng));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
	}
	
	private void getXYLocation(double lat,double lng,final StringBuffer sb){
		GeocodeSearch geocodeSearch = new GeocodeSearch(this);
		RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), 200, GeocodeSearch.AMAP);
		geocodeSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
			
			@Override
			public void onRegeocodeSearched(RegeocodeResult result, int rcode) {
				if(result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null){
					sb.append("位置："+result.getRegeocodeAddress().getFormatAddress());
					deviceStatusTv.setText(sb.toString());
				}
				
			}
			
			@Override
			public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
				
			}
		});
		geocodeSearch.getFromLocationAsyn(query);
	}
	Marker marker;
	public void drawMarker(double lat,double lng){
		//清楚
		if(marker!=null){
			marker.remove();
		}
		 //绘制marker
        marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                .title("位置")
                .snippet("DefaultMarker")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_gps)))
                );
	}
	Polyline line;
	private void getPath(){
		String strUrl = String.format(Constants.RUN_PATH_URL, MyApplication.DEVIDE_ID);
		Request<PathResponse> request = new CustomDataRequest<PathResponse>(strUrl,RequestMethod.GET,PathResponse.class);
		request.setConnectTimeout(60 * 1000);
		request.setReadTimeout(60 * 1000);
		request.setHeader("context-Type", "application/json");
		request.setHeader("mobileNo", "");
		request.setHeader("mobiledeviceId", "");
		request.setHeader("accesstoken",  "");
		CallServer.getRequestInstance().add(this, strUrl.hashCode(), request, new HttpListener<PathResponse>() {

			@Override
			public void onSucceed(int what, Response<PathResponse> response) {
				if(response.isSucceed() && response.get() != null){
					if(response.get().data != null && !response.get().data.isEmpty()){
							List<LatLng> latLngs = new ArrayList<LatLng>();
							for (PathInfo pi : response.get().data) {
								if(pi.lat > 0 && pi.lng > 0){
									LatLng latLng = new LatLng(pi.lat ,pi.lng);
									CoordinateConverter converter  = new CoordinateConverter();
									// CoordType.GPS 待转换坐标类型
									converter.from(CoordType.GPS);
									// sourceLatLng待转换坐标点 DPoint类型
									converter.coord(latLng);
									// 执行转换操作
									LatLng desLatLng = converter.convert();
									
									latLngs.add(desLatLng);
								}
								
							}
							
						if(line!=null){
							line.remove();
						}
						 line  =aMap.addPolyline(new PolylineOptions().
						        addAll(latLngs).width(10).color(Color.argb(255, 255, 0, 0)));
					}
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag, Exception exception, int responseCode,
					long networkMillis) {
				// TODO Auto-generated method stub
				
			}
		},this,true);

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQUEST_SCAN_IMEI_CLOUD :
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				PreferencesUtils.putString(CloudSmartControlActivity.this, "IMEI", result);
				MyApplication.DEVIDE_ID = result;
				showImei(result);
				//显示 (Bitmap) data.getParcelableExtra("bitmap")
				if(TextUtils.isEmpty(result)|| result.length()!=15){
					MyToast.showShortToast(this, "二维码格式错误,绑定失败,请重试!");
					bindIMEIClick.setText("绑定");
				}else{
					bindImeiAndPhone();
				}
			}
			break;
		}
	}

	//绑定号码和imei
	private void bindImeiAndPhone() {
		// TODO Auto-generated method stub
		if(!PreferencesUtils.getBoolean(this, "LoginFlag")){
			Intent intent = new Intent(this,LoginActivity.class);
			intent.putExtra("first", "0");
			startActivity(intent);
			return;
		}
		Map<String,String> params = new HashMap<String, String>();
		params.put("imei", MyApplication.DEVIDE_ID);
		params.put("mobileNo",MyApplication.MOBILE);
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, Constants.SET_SENSITIVE_URL, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				if(result.statusCode == 200){
					bindIMEIClick.setText("解绑");
					MyToast.showShortToast(CloudSmartControlActivity.this, "成功绑定设备:"+MyApplication.DEVIDE_ID);
				}else{
					Toast.makeText(CloudSmartControlActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, params, "POST", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
	}
}
