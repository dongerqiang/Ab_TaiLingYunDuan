package com.ananda.tailing.bike.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.ARSActivity;
import com.ananda.tailing.bike.activity.ConsultationActivity;
import com.ananda.tailing.bike.activity.MeterActivity;
import com.ananda.tailing.bike.activity.MoreActivity;
import com.ananda.tailing.bike.activity.RomtorActivity;

public class TabBarView extends LinearLayout implements OnClickListener {
	
	/** 遥控、仪表、ARS、更多(图片) */
	private ImageView ivRomtor, ivMeter, ivArs, ivMore;
	/** 遥控、仪表、ARS、更多(文字) */
	private TextView tvRomtor, tvMeter, tvArs, tvMore; 
	
	/** 设置选中的View  */
	private List<View> listImg = new ArrayList<View>();
	
	private Activity act;
	
	public void setIndex(int index) {
		ivRomtor.setSelected((index == 0 ? true : false));
		ivMeter.setSelected((index == 1 ? true : false));
		ivArs.setSelected((index == 2 ? true : false));
		ivMore.setSelected((index == 3 ? true : false));
		tvRomtor.setSelected((index == 0 ? true : false));
		tvMeter.setSelected((index == 1 ? true : false));
		tvArs.setSelected((index == 2 ? true : false));
		tvMore.setSelected((index == 3 ? true : false));
	}
	
	/**
	 * @param context
	 */
	public TabBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		act = ((Activity)context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TabBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		act = ((Activity)context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TabBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		act = ((Activity)context);
		init(context);
		
	}
	private void init(Context context){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_frame, this, true);
		ivRomtor = (ImageView) findViewById(R.id.imageview_romtor);
		ivMeter = (ImageView) findViewById(R.id.imageview_meter);
		ivArs = (ImageView) findViewById(R.id.imageview_ars);
		ivMore = (ImageView) findViewById(R.id.imageview_more);
		tvRomtor = (TextView) findViewById(R.id.textview_romtor);
		tvMeter = (TextView) findViewById(R.id.textview_meter);
		tvArs = (TextView) findViewById(R.id.textview_ars);
		tvMore = (TextView) findViewById(R.id.textview_more);
		
		listImg.add(ivRomtor);
		listImg.add(ivMeter);
		listImg.add(ivArs);
		listImg.add(ivMore);
				
		for(int i = 0; i < listImg.size(); i++) {
			listImg.get(i).setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.imageview_romtor: {  // 遥控
			act.startActivity(new Intent(act, RomtorActivity.class)); 			
			break;
		}
		case R.id.imageview_meter: {    // 仪表盘
			act.startActivity(new Intent(act, MeterActivity.class)); 
			break;
		}
		case R.id.imageview_ars: {      // ARS
			act.startActivity(new Intent(act, ConsultationActivity.class)); 
			break;
		}
		case R.id.imageview_more: {     // 更多
			act.startActivity(new Intent(act, MoreActivity.class)); 
			break;
		}
		}
		act.overridePendingTransition(0, 0);
		act.finish();
	}
	

}
