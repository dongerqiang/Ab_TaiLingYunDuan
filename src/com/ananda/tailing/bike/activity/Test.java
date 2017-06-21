package com.ananda.tailing.bike.activity;

import java.util.Random;

import com.ananda.tailing.bike.smartbike.SmartBikeInstance;

import android.content.Intent;

public class Test extends Thread{

	public void run(){
		for(int i=0;i<1000;i++){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Random rd = new Random();
			int ARS = rd.nextInt(5);
			if(SmartBikeInstance.arsAlarm(ARS)){
				Intent intent = new Intent();
				intent.setAction(RomtorActivity.ARS_TAG);
				intent.putExtra("ars", ARS);
				System.out.println("--------------send ars:" + ARS);
				MyApplication.getInstance().sendBroadcast(intent);
			}
		}
	}
	
}
