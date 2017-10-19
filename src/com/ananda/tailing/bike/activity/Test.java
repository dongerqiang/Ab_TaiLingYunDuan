package com.ananda.tailing.bike.activity;

import java.util.Random;

import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.CoordinateConverter.CoordType;
import com.amap.api.maps2d.model.LatLng;
import com.ananda.tailing.bike.smartbike.SmartBikeInstance;

import android.content.Intent;

public class Test extends Thread{
	public static void main(String[] args) {
		LatLng latLng = new LatLng(0 ,0);
		
		CoordinateConverter converter  = new CoordinateConverter();
		// CoordType.GPS 待转换坐标类型
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标点 DPoint类型
		converter.coord(latLng);
		// 执行转换操作
		LatLng desLatLng = converter.convert();
	}
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
			if(SmartBikeInstance.getInstance().arsAlarm(ARS)){
				Intent intent = new Intent();
				intent.setAction(RomtorActivity.ARS_TAG);
				intent.putExtra("ars", ARS);
				System.out.println("--------------send ars:" + ARS);
				MyApplication.getInstance().sendBroadcast(intent);
			}
		}
		
		
	}
	
}
