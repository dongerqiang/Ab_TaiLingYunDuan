package com.ananda.tailing.bike.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.more.LoginActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.xiaofu_yan.blueguardserver.BootupReceiver;
import com.xiaofu_yan.blueguardserver.ServerControlActivity;
import com.xiaofu_yan.blux.le.server.BluxSsServer;

/**
 * @package com.ananda.tailing.bike.activity
 * @description: 欢迎页
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-21 下午5:11:17
 */
public class WeclomeActivity extends Activity {

	private String userId;  // 用户登录ID
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weclome_layout);
		CommonUtils.subActivityStack.add(this);
		
		userId = PreferencesUtils.getString(this, "UserId");
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
					intent = new Intent(WeclomeActivity.this, RomtorActivity.class);
				} else {
					intent = new Intent(WeclomeActivity.this, LoginActivity.class);
					intent.putExtra("first", "0");
				}				
				startActivity(intent);				
			}
		}, 2000);
		
	}
		
}
