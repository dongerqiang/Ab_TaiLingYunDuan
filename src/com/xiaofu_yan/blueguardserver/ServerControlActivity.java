package com.xiaofu_yan.blueguardserver;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.ananda.tailing.bike.R;
import com.xiaofu_yan.blux.le.server.BluxSsServer;


public class ServerControlActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		Notification notification;
		Notification.Builder nb = new Notification.Builder(ServerControlActivity.this);
		nb.setSmallIcon(R.drawable.ic_launcher);
		nb.setTicker("SmartGuard");
		nb.setContentTitle("SmartGuard");
		nb.setContentText("SmartGuard Sever is running!");
		notification = nb.build();
		Intent settingViewIntent = new Intent();
		settingViewIntent.setClass(ServerControlActivity.this, ServerControlActivity.class);
		notification.contentIntent = PendingIntent.getActivity(ServerControlActivity.this, 0, settingViewIntent, 0);

		BluxSsServer.sharedInstance().setForeGroundNotification(notification);
		BluxSsServer.sharedInstance().start(ServerControlActivity.this);
		
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
