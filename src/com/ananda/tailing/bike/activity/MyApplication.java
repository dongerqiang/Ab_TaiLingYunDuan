package com.ananda.tailing.bike.activity;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.bluetooth.BluetoothChatService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.xiaofu_yan.blueguardserver.ServerControlActivity;
import com.xiaofu_yan.blux.le.server.BluxSsServer;

/**
 * @package com.ananda.tailing.bike.activity
 * @description: 全局变量
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-14 上午10:05:16
 */
public class MyApplication extends Application {
	 private static MyApplication instance;

	    public static MyApplication getInstance() {
	        return instance;
	    }
	
	public BluetoothChatService mBluetoothChatService;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
		startSmartBikeService(getApplicationContext());
		ActiveAndroid.initialize(this);
		instance = this;
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
}
