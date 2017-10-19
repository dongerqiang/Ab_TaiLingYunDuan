package com.ananda.tailing.bike.smartbike;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.activity.MeterActivity;
import com.ananda.tailing.bike.activity.MyApplication;
import com.ananda.tailing.bike.activity.RomtorActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xiaofu_yan.blux.blue_guard.BlueGuard;
import com.xiaofu_yan.blux.blue_guard.BlueGuard.PairResult;
import com.xiaofu_yan.blux.blue_guard.BlueGuard.State;
import com.xiaofu_yan.blux.le.server.BluxSsServer;
import com.xiaofu_yan.blux.smart_bike.SmartBike;
import com.xiaofu_yan.blux.smart_bike.SmartBikeManager;
import com.xiaofu_yan.blux.smart_bike.SmartBikeServerConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.Toast;

public class SmartBikeInstance{
	private  State currentState;
	private  int currentGear = -1;

	private  SmartBikeServerConnection mConnection;
	private  SmartBikeManager mSmartBikeManager;
	private  SmartBike mSmartBike;
	private  DeviceDB.Record record;
	private Activity activity;
	public  State state;
	private  List<Float> speedList = new ArrayList<Float>();
	private static SmartBikeInstance instance;
	public static SmartBikeInstance getInstance(){
		return instance == null ? instance = new SmartBikeInstance() : instance;
	}
	
	private SmartBikeInstance(){
		
	}
	
	public void loadSmartBike(){
		instance = this;
		PreferencesUtils.putInt(MyApplication.getInstance(), "energy", 0);
		record = DeviceDB.load(MyApplication.getInstance());
		mConnection = new SmartBikeServerConnection();
		mConnection.delegate = new ServerConnectionDelegate();
		
	}
	
	public void connect(Activity activity){
		this.activity = activity;
		if(mSmartBikeManager == null){
			mConnection.connect(activity);
		}
	}
	
	public  void playSound(BlueGuard.Sound sound){
		if(mSmartBike != null){
			mSmartBike.playSound(sound);
		}
	}
	
	public  int getDis(){
		if(mSmartBike != null){
			return mSmartBike.currentRangePercent();
		}
		return 1000;
	}
	
	public  void openTrunk(){
		if(mSmartBike != null){
			mSmartBike.openTrunk();
		}
	}
	
	public  State getState(){
		State result = null;
		if(mSmartBike != null){
			result = mSmartBike.state();
		}
		return result;
	}
	
	public  int setGuardDistance(){
		if(mSmartBike != null){
			int persent = mSmartBike.currentRangePercent();
			mSmartBike.setAutoArmRangePercent(persent);
			return persent;
		}
		return -1;
	}
	
	public  boolean clickBluetooth(){
		if(mSmartBikeManager == null){
			return false;
		}
		mSmartBikeManager.scanSmartBike();
		return true;
	}
	
	public  void getDevice(String identifier){
		mSmartBikeManager.getDevice(identifier);
	}
	
	public  void setState(BlueGuard.State state){
		if(mSmartBike != null){
			mSmartBike.setState(state);
		}
	}
	
	public  boolean pairDevice(String password){
		if(password != null && password != ""){
			mSmartBike.pair(Integer.decode(password));
		}else{
			return false;
		}
		return true;
	}
	
	public  void connectByKey(String key){
		mSmartBike.setConnectionKey(key);
		mSmartBike.connect();
	}
	
	public  SmartBike getSmartBike(){
		return mSmartBike;
	}
	
	public  List<Float> getSpeedList(){
		return speedList;
	}
	
	public  void clearSpeedList(){
		speedList.clear();
	}
	
	public  boolean isConnected(){
		if(mSmartBike == null || !mSmartBike.connected()){
			return false;
		}else{
			return true;
		}
	}
	
	public  void setGuardDiatance(){
		String type = PreferencesUtils.getString(MyApplication.getInstance(), "distance_level");
		if(mSmartBike == null){
			return;
		}else if("1".equals(type)){
			mSmartBike.setAutoArmRangePercent(10);
		}else if("2".equals(type)){
			mSmartBike.setAutoArmRangePercent(80);
		}else if("3".equals(type)){
			mSmartBike.setAutoArmRangePercent(100);
		}
	}
	
	public  void setAutoArm(){
		if(!isConnected()){
			return;
		}
		if(PreferencesUtils.getString(MyApplication.getInstance(), "fortification", "0").equals("0")){
			System.out.println("setAlarmConfig...");
			mSmartBike.setAutoArmRangePercent(50);
		}else{
			System.out.println("closeAlarmConfig");
			mSmartBike.setAutoArmRangePercent(-1);
		}
	}
	
	public  void initAlarmVoice(){
		if(!isConnected()){
			return;
		}
		if(PreferencesUtils.getString(MyApplication.getInstance(), "bike_voice_control", "0").equals("0")){
			mSmartBike.setAlarmConfig(true, true);
		}else{
			mSmartBike.setAlarmConfig(false, true);
		}
	}
	
	public  void stopScan(){
		mSmartBikeManager.stopScan();
	}
	
	public  void closeDevice(){
		if(mSmartBike != null){
			mSmartBike.cancelConnect();
		}
		BluxSsServer.sharedInstance().stop();
	}
	
	public  void clearDevice(){
		if(mSmartBike != null){
			mSmartBike.cancelConnect();
		}
	}

	public  void closeVoice(Context context){
		if(mSmartBike != null){
			mSmartBike.setAlarmConfig(false, true);
		}else{
			MyToast.showShortToast(context, "设备未连接！");
		}
	}

	public  void openVoice(Context context){
		if(mSmartBike != null){
			mSmartBike.setAlarmConfig(true, true);
		}else{
			MyToast.showShortToast(context, "设备未连接！");
		}
	}
	
	public  void setSmartBikeArmConfigTrue(){
		if(mSmartBike != null){
			mSmartBike.setAlarmConfig(true, true);
		}
	}
	
	public  void setShockSensitivity(){
		if(!isConnected()){
			return;
		}
		String vibrationLevel = PreferencesUtils.getString(MyApplication.getInstance(), "vibration_level", "0");
		mSmartBike.setShockSensitivity(Integer.valueOf(vibrationLevel));
	}
	
	public  boolean arsAlarm(Integer i){
		for(Integer arsInt : RomtorActivity.arsList){
			if(arsInt == i){
				return false;
			}
		}
		return true;
	}
	
	private  class ServerConnectionDelegate extends SmartBikeServerConnection.Delegate {
		@Override
		public void smartBikeServerConnected(SmartBikeManager smartBikeManager) {
			System.out.println("server connected......");
			mSmartBikeManager = smartBikeManager;
			mSmartBikeManager.delegate = new SmartBikeManagerDelegate();
			mSmartBikeManager.setContext(activity);
			if(record != null){
				mSmartBikeManager.getDevice(record.identifier);
			}
			
		}

		@Override
		public void smartBikeServerDisconnected() {
		}
	}
	
	// SmartGuardManager.Delegate
	private  class SmartBikeManagerDelegate extends SmartBikeManager.Delegate {
		
				@Override
				public void smartBikeManagerFoundSmartBike(String identifier, String name, int nMode) {
							System.out.println("smartBikeManagerFoundSmartBike,identifier:" + identifier);
							Intent intent = new Intent();
							intent.setAction(RomtorActivity.DEVICE_TAG);
							intent.putExtra("device_identifier", identifier);
							intent.putExtra("device_name", name);
							MyApplication.getInstance().sendBroadcast(intent);
				}
				
				

				@Override
				public void smartBikeManagerGotSmartBike(SmartBike smartBike) {
					System.out.println("..........gotSmartBike");
					mSmartBike = smartBike;
					mSmartBike.setAlarmConfig(true, true);
					mSmartBike.delegate = new BlueGuardDelegate();
					
					if(!mSmartBikeManager.isScanning()){
						if(record != null && !TextUtils.isEmpty(record.key)){
							mSmartBike.setConnectionKey(record.key);
							mSmartBike.connect();
						}else{
							Intent intent = new Intent();
							intent.setAction(RomtorActivity.PAIR_TAG);
							MyApplication.getInstance().sendBroadcast(intent);
						}
					}else{
						String userLocalKey = PreferencesUtils.getString(MyApplication.getInstance(), "UserKey");
						if(userLocalKey != null && !userLocalKey.trim().isEmpty()){
//							DeviceInfo deviceInfo = DeviceDB.load(MyApplication.getInstance(), mSmartBike.identifier());
//							if(deviceInfo == null){
//								deviceInfo = new DeviceInfo();
//								deviceInfo.setKey(userLocalKey);
//								deviceInfo.setIdentifier(mSmartBike.identifier());
//								deviceInfo.setName(mSmartBike.name());
//							}else{
//								deviceInfo.setKey(userLocalKey);
//							}
//							
//							DeviceDB.save(MyApplication.getInstance(), deviceInfo);
//							mSmartBike.setConnectionKey(userLocalKey);
//							mSmartBike.connect();
						}else{
							Intent intent = new Intent();
							intent.setAction(RomtorActivity.PAIR_TAG);
							MyApplication.getInstance().sendBroadcast(intent);
						}
					}
				}
			}
			// BlueGuard delegate
			private  class BlueGuardDelegate extends SmartBike.Delegate {
				@Override
				public void blueGuardConnected(BlueGuard blueGuard) {
					Toast.makeText(MyApplication.getInstance(),"连接成功！", Toast.LENGTH_SHORT)
							.show();
					mSmartBike.getAccountManager(); //
					setAutoArm();
					setShockSensitivity();
					setGuardDiatance();
					initAlarmVoice();
//					mSmartBike.startARS();
					Intent intent = new Intent();
					intent.setAction(RomtorActivity.START_STATUS_GET);
					MyApplication.getInstance().sendBroadcast(intent);
				}
				
				@Override
				public void blueGuardPairResult(BlueGuard blueGuard, BlueGuard.PairResult result, String key) {
					if(result == PairResult.ERROR){
						Toast.makeText(MyApplication.getInstance(),"连接失败！", Toast.LENGTH_SHORT)
						.show();
						Intent intent = new Intent();
						intent.setAction(RomtorActivity.PAIR_TAG);
						MyApplication.getInstance().sendBroadcast(intent);
					}else if(result == PairResult.SUCCESS){
						System.out.println("PairResult.SUCCESS");
						
						DeviceDB.Record rec = new DeviceDB.Record(blueGuard.name(), blueGuard.identifier(), key);
				
						DeviceDB.save(MyApplication.getInstance(), rec);
						//DeviceDB.saveUsed(MyApplication.getInstance(), rec);
						uploadDeviceKey(key);
					}
					
				}
				
				@Override
				public void blueGuardDisconnected(BlueGuard blueGuard, BlueGuard.DisconnectReason reason){
					if(reason == BlueGuard.DisconnectReason.ERROR_PERMISSION){
						MyToast.showShortToast(MyApplication.getInstance(), "连接失败！");
					}
					
				}
				@Override
				public void blueGuardAlarm(BlueGuard blueGuard, BlueGuard.Alarm type){
					System.err.println("-----------------------------------type:" + type);
					if(BlueGuard.Alarm.HIGH.equals(type)||BlueGuard.Alarm.LOW.equals(type)){
						Intent intent = new Intent();
						intent.setAction(RomtorActivity.ALARM_TAG);
						MyApplication.getInstance().sendBroadcast(intent);
					}
					
				}
				@Override
				public void blueGuardState(BlueGuard blueGuard, BlueGuard.State state){
					System.err.println("------------------BlueGuard.State:" + state);
					if(BlueGuard.State.STOPPED.equals(currentState) && BlueGuard.State.STARTED.equals(state)){
						PreferencesUtils.putString(MyApplication.getInstance(), "beginTime", CommonUtils.getDateTime());
					}else if(BlueGuard.State.STARTED.equals(currentState) && BlueGuard.State.STOPPED.equals(state)){
						PreferencesUtils.putString(MyApplication.getInstance(), "endTime", CommonUtils.getDateTime());
					}
					currentState = state;
				}
				
				@Override
				public void smartBikeUpdateController(SmartBike smartBike, int ARS, int gear, boolean speedLimited, boolean cruise){
					if(ARS != 0){
						if(arsAlarm(ARS)){
							Intent intent = new Intent();
							intent.setAction(RomtorActivity.ARS_TAG);
							intent.putExtra("ars", ARS);
							MyApplication.getInstance().sendBroadcast(intent);
						}
					}
					
					if(gear != currentGear){
						currentGear = gear;
						Intent intent = new Intent();
						intent.setAction(MeterActivity.GEAR_TAG);
						intent.putExtra("gear", gear);
						MyApplication.getInstance().sendBroadcast(intent);
					}
				}

				@Override
				public void smartBikeUpdateBattery(SmartBike smartBike, int energyLevel, float currentInAmpere, int batteryCapacity, int batteryChargeCounter) {
					PreferencesUtils.putInt(MyApplication.getInstance(), "energy", energyLevel);
					PreferencesUtils.putInt(MyApplication.getInstance(), "current", (int)currentInAmpere);
					PreferencesUtils.putInt(MyApplication.getInstance(), "bateryShow", batteryChargeCounter);
					System.out.println("----------BateryNumReceiver.bateryNum:" + batteryCapacity);
					Intent intent = new Intent();
					intent.setAction(MeterActivity.BATERY_NUM_TAG);
					intent.putExtra("textview_batery_num", "" + batteryCapacity);
					MyApplication.getInstance().sendBroadcast(intent);
				}

				@Override
				public void smartBikeUpdateData(SmartBike smartBike, float tempterature, float speedInKmph, float mileageInKm) {
					PreferencesUtils.putInt(MyApplication.getInstance(), "mileage", (int)mileageInKm);
					PreferencesUtils.putInt(MyApplication.getInstance(), "temper", (int)tempterature);
					speedList.add(speedInKmph);
					Intent intent = new Intent();
					intent.setAction(MeterActivity.SPEED_TAG);
					System.out.println("---------------------bikeSpeed:" + speedInKmph);
					intent.putExtra("speed_km", "" + speedInKmph);
					MyApplication.getInstance().sendBroadcast(intent);
				}
				

			}
	

			private  void uploadDeviceKey(String key){
				try {
					JSONObject jsonLogin = new JSONObject();
					jsonLogin.put("Name", PreferencesUtils.getString(MyApplication.getInstance(), "UserName"));
					jsonLogin.put("UserKey",key);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("Result", HttpAPI.COMMITKEY);
					jsonObject.put("Info", JSONObject.parseObject(jsonLogin.toString()));
					
					System.err.println("获取设备key报文：-->" + jsonObject.toString()); 
					StringEntity stringEntity = new StringEntity(jsonObject.toString());
					HttpRestClient.post(MyApplication.getInstance(), HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){
						
						@Override
						public void onFailure(Throwable error, String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							Toast.makeText(MyApplication.getInstance(), HttpAPI.RESPONSE, Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, content);
							System.err.println("获取设备key返回报文： -->" + content); 
							JSONObject jsonObject = JSONObject.parseObject(content); 
							String result = jsonObject.getString("Result");
							String info = jsonObject.getString("Info");
							JSONObject jsonInfo = JSONObject.parseObject(info);
							if(result.equals("110")) {			
								System.out.println("上传设备key到服务器。");
							} else {
								String message = jsonInfo.getString("Message");
								Toast.makeText(MyApplication.getInstance(),message, Toast.LENGTH_SHORT)
								.show();
							}		
						}				
					});
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
}
