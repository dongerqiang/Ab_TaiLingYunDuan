package com.ananda.tailing.bike.activity.more;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;

import com.ananda.tailing.bike.activity.ARSActivity;
import com.ananda.tailing.bike.activity.MyApplication;
import com.ananda.tailing.bike.activity.RomtorActivity;

public class ArsReceiver extends BroadcastReceiver {
	private boolean dialogFlag = true;
	private Context context;
	
	public ArsReceiver(Context context){
		this.context = context;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(RomtorActivity.ARS_TAG)) {
			if(dialogFlag){
				int arsInt = intent.getIntExtra("ars", 0);
				showArsDialog(arsInt);
				dialogFlag = false;
			}
			
		}
		
	}
	
	public void showArsDialog(final int rssi){
		final String rssiStr = String.valueOf(rssi);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("电车故障");
		builder.setMessage("是否显示故障原因？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RomtorActivity.arsList.add(rssi);
				dialogFlag = true;
				Intent intent = new Intent(MyApplication.getInstance(),ARSActivity.class);
				intent.putExtra("RSSI", String.valueOf(rssiStr));
				context.startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RomtorActivity.arsList.add(rssi);
				dialogFlag = true;
				dialog.dismiss();
			}
		});
		builder.show();
	}
	

}
