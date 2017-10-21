package com.ananda.tailing.bike.activity;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.more.ArsReceiver;
import com.ananda.tailing.bike.activity.more.LoginActivity;
import com.ananda.tailing.bike.data.BaseResponse;
import com.ananda.tailing.bike.entity.RideReportInfo;
import com.ananda.tailing.bike.smartbike.DeviceAdapter;
import com.ananda.tailing.bike.smartbike.DeviceDB;
import com.ananda.tailing.bike.smartbike.SmartBikeInstance;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.RideReportDialog;
import com.ananda.tailing.bike.view.TabBarView;
import com.ananda.tailing.bike.view.TitleBarView;
import com.fu.baseframe.net.CallServer;
import com.fu.baseframe.net.CustomDataRequest;
import com.fu.baseframe.net.HttpListener;
import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xiaofu_yan.blux.blue_guard.BlueGuard;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @package com.ananda.tailing.bike.activity
 * @description: 遥控界面
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-7 下午12:27:12
 */
public class RomtorActivity extends BaseActivity implements OnClickListener {
	public static List<Integer> arsList = new ArrayList<Integer>();
//	public static boolean arsServiceFlag = true;
	public static final String DEVICE_TAG = "com.ananda.tailing.bike.activity.RomtorActivity.device";
	public static final String PAIR_TAG = "com.ananda.tailing.bike.activity.RomtorActivity.pair";
	public static final String ARS_TAG = "com.ananda.tailing.bike.activity.RomtorActivity.ars";
	public static final String START_STATUS_GET = "com.ananda.tailing.bike.activity.RomtorActivity.getstatus";
	public static final String ALARM_TAG = "com.ananda.tailing.bike.activity.RomtorActivity.alarm";

	public static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 0x00012;
	public static final int WRITE_FINE_LOCATION_REQUEST_CODE = 0x00013;
	public static final int WRITE_EXTERNAL_STORAGE_CODE = 0x00014;
	public static final int READ_EXTERNAL_STORAGE_CODE = 0x00015;
	public static final int READ_PHONE_STATE_CODE = 0x00016;
	
	private Context context;
	private String userId;
	private MyDialog myDialog;
	private Dialog dialog;
	private DeviceDB.Record record;
	private DeviceAdapter adapter;
	private String mKey;
	
	private EditText mPassEditView;
	
	/** 上锁、解锁、寻车警报、打开座桶 */
	private Button btnUnlocking, btnLocking, btnCarAlarm, btnOpenBarrel,btnCloud;

	private BluetoothAdapter btAdapt;

	public static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_SCAN_IMEI = 33;
	private TitleBarView titleBarView;
	private TabBarView tabBarView;

	private ImageView bluetoothSearch;
	
	private SpeedReceiver speedReceiver;
	
	/** 骑行报告  */
	private RideReportDialog reportDialog;
	List<Float> speedList = new ArrayList<Float>();
	
	private String sdate = null;
	private String cdate = null;

	private DeviceReceiver deviceReceiver;
	private PairReceiver pairReceiver;
	private ArsReceiver arsReceiver;

	private Handler dialogHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			if (myDialog != null) {
				myDialog.dismiss();
			}
		}
	};
	
	private Handler pairHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			pairDevice();
		}
	};
	
	
	/***
	 * 新增代码
	 * @create date 2016年9月8日 15:20:48
	 * @author fwc
	 */
	OnClickListener carStatusOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			getCarState();
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.romtor_layout);
		CommonUtils.subActivityStack.add(this);
		context = this;
		btAdapt = BluetoothAdapter.getDefaultAdapter();
		startBluetooth();		
		super.onCreate(savedInstanceState); 
		resisterReceivers();
		
		myDialog = new MyDialog(this, dialogHandler);
		reportDialog = new RideReportDialog(this, dialogHandler);
		
		adapter = new DeviceAdapter(context);
		
		if(!SmartBikeInstance.getInstance().isConnected()){
			try{
				SmartBikeInstance.getInstance().loadSmartBike();
			}catch(Exception e){
				MyToast.showShortToast(this, "台铃服务未启动!");
			}
			
		}
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
	              != PackageManager.PERMISSION_GRANTED) {
	          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
	                  WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
	    }
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
	              != PackageManager.PERMISSION_GRANTED) {
	          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
	        		  WRITE_FINE_LOCATION_REQUEST_CODE);//自定义的code
	    }
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
	              != PackageManager.PERMISSION_GRANTED) {
	          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
	        		  WRITE_EXTERNAL_STORAGE_CODE);//自定义的code
	    }
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
	              != PackageManager.PERMISSION_GRANTED) {
	          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
	        		  READ_EXTERNAL_STORAGE_CODE);//自定义的code
	    }
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
	              != PackageManager.PERMISSION_GRANTED) {
	          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
	        		  READ_PHONE_STATE_CODE);//自定义的code
	    }
	}

	
	protected void resisterReceivers(){
		IntentFilter speedFilter = new IntentFilter(MeterActivity.SPEED_TAG);
		speedReceiver = new SpeedReceiver();
		this.registerReceiver(speedReceiver, speedFilter);
		
		IntentFilter deviceFilter = new IntentFilter(DEVICE_TAG);
		deviceReceiver = new DeviceReceiver();
		this.registerReceiver(deviceReceiver, deviceFilter);
		
		IntentFilter pairFilter = new IntentFilter(PAIR_TAG);
		pairReceiver = new PairReceiver();
		this.registerReceiver(pairReceiver, pairFilter);

//		if(arsServiceFlag){
			IntentFilter arsFilter = new IntentFilter(ARS_TAG);
			arsReceiver = new ArsReceiver(context);
			this.registerReceiver(arsReceiver, arsFilter);
//		}
			
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		titleBarView = (TitleBarView) findViewById(R.id.title_bar);
		titleBarView.setTitle("遥控功能");
		tabBarView = (TabBarView) findViewById(R.id.tab_bar);
		tabBarView.setIndex(0);

		findViewById(R.id.selfCarCheckImg).setOnClickListener(carStatusOnClick);
		
		btnUnlocking = (Button) findViewById(R.id.button_unlocking);
		btnLocking = (Button) findViewById(R.id.button_locking);
		btnCarAlarm = (Button) findViewById(R.id.button_car_alarm);
		btnOpenBarrel = (Button) findViewById(R.id.button_open_barrel_ride);
		btnCloud = (Button) findViewById(R.id.button_car_cloud);
		btnCloud.setOnClickListener(this);
		
		bluetoothSearch = (ImageView) findViewById(R.id.imageview_bluetooth_search);
		bluetoothSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String phoneControl = PreferencesUtils.getString(context, "control_type");
				if(phoneControl != null && "1".equals(phoneControl)){
					MyToast.showShortToast(context, "正在使用手柄控制!");
				}else{
					if (btAdapt.isEnabled()) {
//						if (isLogin()) {
							System.out.println("搜索蓝牙设备...");
							adapter.reset();
							if(!SmartBikeInstance.getInstance().clickBluetooth()){
								SmartBikeInstance.getInstance().loadSmartBike();
								MyToast.showShortToast(RomtorActivity.this, "正在启动蓝牙服务。。。。");
								return;
							}
							showDeviceDialog();
//						} else {
//							myDialog.show();
//							myDialog.setTextInfo("请先登录!");
//						}
					} else {
						Toast.makeText(RomtorActivity.this,
								R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});

		if(SmartBikeInstance.getInstance().getSmartBike() == null || (SmartBikeInstance.getInstance().getSmartBike().state() != BlueGuard.State.STARTED && SmartBikeInstance.getInstance().getSmartBike().state() != BlueGuard.State.RUNNING)){
			initClickListener(false);
		}else{
			initClickListener(true);
		}
		
		// 用户第一次使用APP
		if (TextUtils.isEmpty(PreferencesUtils.getString(RomtorActivity.this,
				"pwd"))) {
			String smdate = CommonUtils.getCurrentDate();
			System.out.println("日期: -->" + smdate);
			PreferencesUtils
					.putString(RomtorActivity.this, "date_time", smdate);
		}
		// 获取之前保存的日期
		if (!TextUtils.isEmpty(PreferencesUtils.getString(RomtorActivity.this,
				"date_time"))) {
			sdate = PreferencesUtils
					.getString(RomtorActivity.this, "date_time");
		}
		// 获取当前的日期
		cdate = CommonUtils.getCurrentDate();
		// 计算相差的天数
		int days = 0;
		try {
			days = CommonUtils.daysBetween(sdate, cdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e);
		}
		// 如果当前得到的天数为一周, 则上传骑行报告数据到服务器
		if (days == 7) {
			submitReportToService();
		}
		
		submitReportToService();
	}

	private void initClickListener(boolean isRunning) {
		
//		if(isRunning){
//			Toast.makeText(this, "行驶中，无法使用遥控功能！", Toast.LENGTH_LONG).show();;
//			btnUnlocking.setClickable(false);
//			btnLocking.setClickable(false);
//			btnCarAlarm.setClickable(false);
//			btnOpenBarrel.setClickable(false);
//		}else{
			// 上锁
			if (PreferencesUtils.getString(this, "unlock", "0").equals("0")) {
				btnUnlocking.setOnClickListener(this);
			}
			// 解锁
			if (PreferencesUtils.getString(this, "lock", "0").equals("0")) {
				btnLocking.setOnClickListener(this);
			}
			// 寻车警报
			if (PreferencesUtils.getString(this, "alarm", "0").equals("0")) {
				btnCarAlarm.setOnClickListener(this);
			}
			// 鞍座开启
			//if (PreferencesUtils.getString(this, "saddles", "0").equals("0")) {
				btnOpenBarrel.setOnClickListener(this);
			//}

			// 超负荷监测
			if (PreferencesUtils.getString(this, "overload", "0").equals("0")) {

			}
//		}
		
	}

	/**
	 * 提交骑行报告到服务器
	 */
	private void submitReportToService() {
		//List<RideReportInfo> listReport = SQLUtil.queryRideReport(db);
		List<RideReportInfo> listReport = new ArrayList<RideReportInfo>();
		RideReportInfo report1 = new RideReportInfo();
		report1.setAverageSpeed("110");          // 平均行驶速度
		report1.setControlTemper("30°");         // 控制器温度
		report1.setEnergyRated(2);            // 能耗评级
		report1.setLonger("80");                // 时长
		report1.setMagnitudeOfCurrent("50");    // 电流量
		report1.setMileage("200");               // 里程
		report1.setSaveTime("");              // 保存时间
		report1.setSurplusBatery("40%");         // 剩余电量
		report1.setChargingTimes(2);          // 充电次数
		
		RideReportInfo report2 = new RideReportInfo();
		report2.setAverageSpeed("180");          // 平均行驶速度
		report2.setControlTemper("60°");         // 控制器温度
		report2.setEnergyRated(4);            // 能耗评级
		report2.setLonger("100");                // 时长
		report2.setMagnitudeOfCurrent("70");    // 电流量
		report2.setMileage("260");               // 里程
		report2.setSaveTime("");              // 保存时间
		report2.setSurplusBatery("20%");         // 剩余电量
		report2.setChargingTimes(5);          // 充电次数
		
		RideReportInfo report3 = new RideReportInfo();
		report3.setAverageSpeed("150");          // 平均行驶速度
		report3.setControlTemper("50°");         // 控制器温度
		report3.setEnergyRated(2);            // 能耗评级
		report3.setLonger("120");                // 时长
		report3.setMagnitudeOfCurrent("70");    // 电流量
		report3.setMileage("250");               // 里程
		report3.setSaveTime("");              // 保存时间
		report3.setSurplusBatery("80%");         // 剩余电量
		report3.setChargingTimes(1);          // 充电次数
				
		listReport.add(report1);
		listReport.add(report2);
		listReport.add(report3);
		// 判断是否连接了网络
		if (CommonUtils.isNetWorkNormal(this)) {
			// 写逻辑提交数据到服务器
			try {
				JSONObject jsonLogin = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < listReport.size(); i++) {
					RideReportInfo info = listReport.get(i);
					JSONObject jsonObject = JSONObject.parseObject(JSONObject
							.toJSONString(info));
					jsonArray.add(jsonObject);
				}
				jsonLogin.put("list", jsonArray);
				jsonLogin.put("Timestamp", CommonUtils.getTimestamp());

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Result", HttpAPI.RIDEREPORT);
				jsonObject.put("Info", JSONObject.parseObject(jsonLogin.toString()));

				System.err.println("骑行报告报文：-->" + jsonObject.toString());
				StringEntity stringEntity;
				stringEntity = new StringEntity(jsonObject.toString());
				HttpRestClient.post(RomtorActivity.this, HttpAPI.USER_URL,
						stringEntity, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(error, content);
								MyToast.showShortToast(RomtorActivity.this,
										HttpAPI.RESPONSE);
							}

							@Override
							public void onSuccess(int statusCode, String content) {
								// TODO Auto-generated method stub
								super.onSuccess(statusCode, content);
								System.out.println("返回的报文123: -->" + content); 
								JSONObject jsonObject = JSONObject.parseObject(content); 
								String result = jsonObject.getString("Result");
								if(result.equals("070")) {
									// 上传成功之后, 更新时间, 并清除记录
									System.out.println("上传成功");
	//								PreferencesUtils.putString(RomtorActivity.this, 
	//										"date_time", CommonUtils.getCurrentDate());
	//								db.clearTable(DBState.TABLE_RIDEREPORT);
								}
							}

						});
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			MyToast.showShortToast(this, "网络不可用, 请先打开网络!");
		}
	}
	
	
	protected void showDeviceDialog(){
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.device_list2, null);
		ListView paired_devices = (ListView) view.findViewById(R.id.paired_devices);
		paired_devices.setAdapter(adapter);
		dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
		paired_devices.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				SmartBikeInstance.getInstance().stopScan();
				SmartBikeInstance.getInstance().clearDevice();
				record = adapter.getClickItem(position);
				SmartBikeInstance.getInstance().getDevice(record.identifier);
				dialog.dismiss();

			}
		});
		
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				SmartBikeInstance.getInstance().stopScan();
				SmartBikeInstance.getInstance().clearDevice();
				dialog.dismiss();
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 上锁
		case R.id.button_unlocking: {
			if(isLogin()){
				if(!isRunning()){
					SmartBikeInstance.getInstance().setState(BlueGuard.State.ARMED);
					setReportInfo();
					if(PreferencesUtils.getString(this, "ride", "0").equals("0")){
						reportDialog.show();
					}
					SmartBikeInstance.getInstance().clearSpeedList();
				}else{
					MyToast.showShortToast(this, "行驶中，无法使用遥控功能!");
				}
			}
			break;
		}
		// 解锁
		case R.id.button_locking: {
			if(isLogin()){
				if(!isRunning()){
				SmartBikeInstance.getInstance().setState(BlueGuard.State.STOPPED);
				}else{
					MyToast.showShortToast(this, "行驶中，无法使用遥控功能!");
				}
			}
			break;
		}
		// 寻车警报
		case R.id.button_car_alarm: {
			if(isLogin()){
				if(!isRunning()){
				SmartBikeInstance.getInstance().playSound(BlueGuard.Sound.FIND);
				}else{
					MyToast.showShortToast(this, "行驶中，无法使用遥控功能!");
				}
			}
			break;
		}
		// 打开座桶
		case R.id.button_open_barrel_ride: {
			if(isLogin()){
				if(!isRunning()){
				SmartBikeInstance.getInstance().openTrunk();
				}else{
					MyToast.showShortToast(this, "行驶中，无法使用遥控功能!");
				}
			}
			break;
		}
		case R.id.button_car_cloud:
			{
				if(!PreferencesUtils.getBoolean(this, "LoginFlag")){
					Intent intent = new Intent(this,LoginActivity.class);
					intent.putExtra("first", "0");
					startActivity(intent);
				}else{
					
				com.ananda.tailing.bike.activity.CloudSmartControlActivity_.intent(context).start();
				}
				/*Intent captureIntent = new Intent(this,CaptureActivity.class);
				captureIntent.putExtra("isFromCloud", true);
				startActivityForResult(captureIntent, REQUEST_SCAN_IMEI);*/
			}
		}
	}
	
	protected boolean isRunning(){
		if((SmartBikeInstance.getInstance().getState() == BlueGuard.State.RUNNING) || (SmartBikeInstance.getInstance().getState() == BlueGuard.State.STARTED)){
			return true;
		}else{
			return false;
		}
	}

	protected void setReportInfo(){
		float maxSpeed = 0;
		speedList = SmartBikeInstance.getInstance().getSpeedList();
		if(speedList.size() == 0){
			return;
		}
		for(Float f : speedList){
			if(f > maxSpeed){
				maxSpeed = f;
			}
		}
		
		PreferencesUtils.putInt(context, "maxSpeed", (int)maxSpeed);
	}

	@Override
	public void onDestroy() {
		if(deviceReceiver!=null){
			this.unregisterReceiver(deviceReceiver);
		}
		if(pairReceiver!=null){
			this.unregisterReceiver(pairReceiver);
		}
		if(arsReceiver!=null){
			try{
				this.unregisterReceiver(arsReceiver);
			}catch(Exception e){
			}
		}
		if(speedReceiver!=null){
			this.unregisterReceiver(speedReceiver);
		}
		
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
		SmartBikeInstance.getInstance().connect(this);
	}
	
	public void onBackPressed() {
		CommonUtils.ExitActivity(this);
	}
	
	protected boolean isLogin(){
		return true;
//		String phoneControl = PreferencesUtils.getString(context, "control_type");
//		if(phoneControl != null && "1".equals(phoneControl)){
//			MyToast.showShortToast(this, "正在使用手柄控制!");
//			return false;
//		}
//		if(!TextUtils.isEmpty(userId) && !userId.equals("0")){
			//return true;
//		}else{
//			return false;
//		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQUEST_ENABLE_BT: {
			initActivity();
			break;
			
		}
		case REQUEST_SCAN_IMEI:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				//显示 (Bitmap) data.getParcelableExtra("bitmap")
				if(TextUtils.isEmpty(result)|| result.length()!=15){
					MyToast.showShortToast(this, "二维码格式错误");
				}else{
					PreferencesUtils.putString(this, "IMEI", result);
					com.ananda.tailing.bike.activity.CloudSmartControlActivity_.intent(context).start();
				}
			}
			break;
		}
	}
	
	private void initActivity(){
		String phoneControl = PreferencesUtils.getString(context, "control_type");
		if(phoneControl != null && "1".equals(phoneControl)){
			return;
		}
		record = DeviceDB.load(context);
		userId = PreferencesUtils.getString(this, "UserId");
		
	}
	
	
	/**
	 * 检测是否打开蓝牙
	 */
	private void startBluetooth() {
		// 如果本程序启动时蓝牙没有打开，要求它启用。
		
		if(btAdapt == null){
			//btAdapt = BluetoothAdapter.getDefaultAdapter();
			MyToast.showShortToast(this, "没有发现蓝牙设备，请检查手机蓝牙工作是正常!");
			return;
		}
		if(btAdapt != null){
			if (!btAdapt.isEnabled()) {
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			}else{
				initActivity();
			}
		}
		
	}

	public class DeviceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("scan_device", action);
			
			if (action.equals(DEVICE_TAG)) {
				String identifier = intent.getStringExtra("device_identifier");
				String name = intent.getStringExtra("device_name");
				if(identifier != null && name != null){
					Log.d("scan_device", name+":"+identifier);
					DeviceDB.Record rec = new DeviceDB.Record(name, identifier, null);
					adapter.addDevice(rec);
					adapter.notifyDataSetChanged();
				}
			
			}
		}
	}
	
	public class PairReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(PAIR_TAG)) {
				pairHandler.sendEmptyMessage(0);
			}
		}
	}
	
	public class SpeedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MeterActivity.SPEED_TAG)) {
//				String speedKm = intent.getStringExtra("speed_km");
//				float speedKmFloat = Float.valueOf(speedKm);
//				if(speedKmFloat != 0){
//					initClickListener(true);
//				}else{
//					initClickListener(false);   
//				}
			}
		}
	}
	
	protected void pairDevice(){
		DeviceDB.Record deviceInfo = DeviceDB.load(context);
		if(deviceInfo != null){
			mKey = deviceInfo.key;
		}
		System.out.println("---------------------currentKey:" + mKey);
//		if(mKey != null) {
//			SmartBikeInstance.getInstance().connectByKey(mKey);
//		}
//		else {
			LayoutInflater inflater = (LayoutInflater) RomtorActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.pair_pass_input, (ViewGroup)findViewById(R.id.pair_pass_word_layout));
			mPassEditView = (EditText) layout.findViewById(R.id.pair_pass_key_input);
			AlertDialog.Builder builder = new AlertDialog.Builder(RomtorActivity.this);
			builder.setTitle("设备配对");
			builder.setMessage("配对号:");
			builder.setView(layout);
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String password = RomtorActivity.this.mPassEditView.getText().toString();
					if(TextUtils.isEmpty(password)){
						myDialog.show();
						myDialog.setTextInfo("密码不能为空！");
						return;
					}
					SmartBikeInstance.getInstance().pairDevice(password);
				}
			});
			builder.show();
//		}
	}
	
	private void carSelfCheckDialog(String msg){
		final Dialog dialog = new Dialog(this,R.style.custom_dialog);
		dialog.setContentView(View.inflate(this, R.layout.dialog_error_check_layout, null));
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.findViewById(R.id.rl).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		TextView tv = (TextView) dialog.findViewById(R.id.errorMsgTv);
		tv.setText(msg);
		dialog.show();
	}
	
	
	private void getCarState(){
		String strUrl = String.format("http://gps.qdigo.net:13080/ops/faultDetect/%s",MyApplication.DEVIDE_ID);
		Request<BaseResponse> request = new CustomDataRequest<BaseResponse>(strUrl,RequestMethod.POST,BaseResponse.class);
		request.setConnectTimeout(60 * 1000);
		request.setReadTimeout(60 * 1000);
		request.setHeader("context-Type", "application/json");
		request.setHeader("mobileNo", "");
		request.setHeader("mobiledeviceId", "");
		request.setHeader("accesstoken",  "");
		Map<String,String> params = new HashMap<String, String>();
		params.put("imeiIdOrDeviceId", MyApplication.DEVIDE_ID);
		request.add(params);
		
		CallServer.getRequestInstance().add(this, strUrl.hashCode(), request, new HttpListener<BaseResponse>() {

			@Override
			public void onSucceed(int what, Response<BaseResponse> response) {
				if(response.isSucceed() && response.get() != null){
					if(response.get().statusCode == 200){
						Toast.makeText(RomtorActivity.this, "设备良好！", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(RomtorActivity.this, response.get().message, Toast.LENGTH_SHORT).show();
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
	
}
