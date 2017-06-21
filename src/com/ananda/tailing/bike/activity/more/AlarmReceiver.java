package com.ananda.tailing.bike.activity.more;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.RomtorActivity;
import com.ananda.tailing.bike.util.PreferencesUtils;

public class AlarmReceiver extends BroadcastReceiver {
	private Context context;
	private MediaPlayer mediaPlayer;
	private boolean alertFlag = true;
	
	public AlarmReceiver(Context context){
		this.context = context;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(RomtorActivity.ALARM_TAG)){
			System.err.println("----------------------------alarm receive");
			if(alertFlag){
				alertFlag = false;
				String voiceControl = PreferencesUtils.getString(context, "voice_control");
				if(voiceControl != null && "1".equals(voiceControl)){
					
				}else{
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
					mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
					mediaPlayer.start();
				}
				
				showAlarmDialog(); 
			}
		}
		
	}
	
	public void showAlarmDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Smart Guard");
		builder.setMessage("Smart Guard 警报");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				alertFlag = true;
			}
		});
		builder.setNegativeButton("关闭警报", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mediaPlayer != null){
					try{
						mediaPlayer.stop();
						mediaPlayer.release();
					}catch(Exception e){
						
					}
				}
				dialog.dismiss();
				alertFlag = true;
			}
		});
		builder.show();
	}
	

}
