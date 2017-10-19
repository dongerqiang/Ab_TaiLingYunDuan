package com.ananda.tailing.bike.activity;

import com.activeandroid.ActiveAndroid;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.bluetooth.BluetoothChatService;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.fu.baseframe.FrameApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.xiaofu_yan.blueguardserver.ServerControlActivity;
import com.xiaofu_yan.blux.le.server.BluxSsServer;

import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import cn.jpush.android.api.JPushInterface;

/**
 * @package com.ananda.tailing.bike.activity
 * @description: 全局变量
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-14 上午10:05:16
 */
public class MyApplication extends FrameApplication	 {
	
	public static String DEVIDE_ID= "";
	
	
	public static String MAIN_URL = "http://gps.qdigo.net:13080/";
	
	 private static MyApplication instance;

	    public static MyApplication getInstance() {
	        return instance;
	    }
	
	public BluetoothChatService mBluetoothChatService;

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		startSmartBikeService(getApplicationContext());
		ActiveAndroid.initialize(this);
		instance = this;
		 JPushInterface.init(getApplicationContext());
		com.netease.nis.bugrpt.CrashHandler.init(this.getApplicationContext());
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		L.disableLogging();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	public static void startSmartBikeService(Context context){
//		Intent intent = new Intent("com.xiaofu_yan.blux.le.server.BluxSsService");
//		intent.setAction("com.xiaofu_yan.blux.le.server.BluxSsService.ACTION");
//		startService(intent);
		
		Notification notification;
		Notification.Builder nb = new Notification.Builder(context);
		nb.setSmallIcon(R.drawable.logo);
		nb.setTicker("SmartGuard");
		nb.setContentTitle("SmartGuard");
		nb.setContentText("SmartGuard Sever is running!");
		notification = nb.build();
		Intent settingViewIntent = new Intent();
		settingViewIntent.setClass(context, RomtorActivity.class);
		notification.contentIntent = PendingIntent.getActivity(context, 0, settingViewIntent, 0);
		System.out.println("---------------------startBluxSsServer");
		BluxSsServer.sharedInstance().setForeGroundNotification(notification);
		BluxSsServer.sharedInstance().start(context);
	}
	
	public class BootupReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("XIAOFU", "POWER UP");
			Notification notification;
			Notification.Builder nb = new Notification.Builder(context);
			nb.setSmallIcon(R.drawable.ic_launcher);
			nb.setTicker("SmartGuard");
			nb.setContentTitle("SmartGuard");
			nb.setContentText("SmartGuard Sever is running!");
			notification = nb.build();
			
			Intent scActivityIntent = new Intent();
			scActivityIntent.setClass(context, ServerControlActivity.class);
			notification.contentIntent = PendingIntent.getActivity(context, 0, scActivityIntent, 0);
			
			BluxSsServer.sharedInstance().setForeGroundNotification(notification);
			BluxSsServer.sharedInstance().start(context);
		}
		
	}
	
	public void dialogInputDeviceId(final Context context,final Callback callback){
		final Dialog dialog = new Dialog(context,R.style.custom_dialog);
		dialog.setContentView(View.inflate(context, R.layout.dialog_input_device_id_layout, null));
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		
		final EditText devId = (EditText) dialog.findViewById(R.id.deviceIdEt);
		
		
		dialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				HideKeyboard(devId);
			}
		});
		
		dialog.findViewById(R.id.complate).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String id = devId.getText().toString();
				if(!TextUtils.isEmpty(id)){
					MyApplication.DEVIDE_ID = id;
					PreferencesUtils.putString(MyApplication.this, "IMEI", id);
					callback.handleMessage(null);
				}
				dialog.dismiss();
				HideKeyboard(devId);
			}
		});
		
		dialog.show();
	}
	
    public static void HideKeyboard(View v)
    {
      InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );     
      if ( imm.isActive( ) ) {     
          imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );   
      }    
    }
}
