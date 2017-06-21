package com.ananda.tailing.bike.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.more.ArsReceiver;
import com.ananda.tailing.bike.smartbike.SmartBikeInstance;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TabBarView;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * @package com.ananda.tailing.bike.activity
 * @description: 仪表界面
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-7 上午9:37:30
 */
public class MeterActivity extends BaseActivity {
	public static final String SPEED_TAG = "com.ananda.tailing.bike.activity.meterActivity.speed";
	public static final String GEAR_TAG = "com.ananda.tailing.bike.activity.meterActivity.gear";
	public static final String BATERY_NUM_TAG = "com.ananda.tailing.bike.activity.meterActivity.baterynum";

	private Context context;
	private MyDialog myDialog;
	
	private ProgressBar progress_horizontal;
	private TextView textview_batery_num;
	
	/** 速度仪表色条、速度、指针 */
	private ImageView ivSpPointer;
	private TextView tvSpNumber;
	private float degress; // 速度
	private RotateAnimation spNormalAnim;
	private int timer = 400;
	private float speedDegrees = 0f;
	
	/** 模式 */
	private ImageView meter_mode_iv;
	private TextView meter_mode_tv;

	private String userId; // 用户登录ID
	private ArsReceiver arsReceiver;

	private TitleBarView titleBarView;
	private TabBarView tabBarView;

	private SpeedReceiver speedReceiver;
	private BateryNumReceiver bateryNumReceiver;
	private GearReceiver gearReceiver;
	
	private Handler dialogHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			if (myDialog != null) {
				myDialog.dismiss();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.meter_layout);
		super.onCreate(savedInstanceState);
		context = this;
		CommonUtils.subActivityStack.add(this);
		myDialog = new MyDialog(this, dialogHandler);
		userId = PreferencesUtils.getString(this, "UserId");

		// 先判断用户是否已登录
//		if (!TextUtils.isEmpty(userId) && !userId.equals("0")) {
			if(!SmartBikeInstance.isConnected()){
				myDialog.show();
				myDialog.setTextInfo("未连接蓝牙设备！");
			}else{
				RegisterReceivers();
			}
//		} else {
//			myDialog.show();
//			myDialog.setTextInfo("请先登录!");
//		}
	}
	
	protected void RegisterReceivers(){
		if("0".equals(PreferencesUtils.getString(MeterActivity.this, "speedControl","0"))){
			System.out.println("----------registerSpeedReceiver");
			IntentFilter speedFilter = new IntentFilter(SPEED_TAG);
			speedReceiver = new SpeedReceiver();
			this.registerReceiver(speedReceiver, speedFilter);
		}
		if("0".equals(PreferencesUtils.getString(MeterActivity.this, "batery","0"))){
			IntentFilter bateryNumFilter = new IntentFilter(BATERY_NUM_TAG);
			bateryNumReceiver = new BateryNumReceiver();
			this.registerReceiver(bateryNumReceiver, bateryNumFilter);
		}
//		if(RomtorActivity.arsServiceFlag){
			IntentFilter arsFilter = new IntentFilter(RomtorActivity.ARS_TAG);
			arsReceiver = new ArsReceiver(context);
			this.registerReceiver(arsReceiver, arsFilter);
//		}
		IntentFilter gearFilter = new IntentFilter(GEAR_TAG);
		gearReceiver = new GearReceiver();
		this.registerReceiver(gearReceiver, gearFilter);
			
	}
	
	/*
	 * (non-Javadoc) 初始化控件
	 *
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		titleBarView = (TitleBarView) findViewById(R.id.title_bar);
		titleBarView.setTitle("仪表功能");
		
		progress_horizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
		textview_batery_num = (TextView) findViewById(R.id.textview_batery_num);

		tvSpNumber = (TextView) findViewById(R.id.textview_speed_number);
		ivSpPointer = (ImageView) findViewById(R.id.imageview_speed_pointer);

		meter_mode_tv = (TextView) findViewById(R.id.meter_mode_tv);
		meter_mode_iv = (ImageView) findViewById(R.id.meter_mode_iv);
		
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			tabBarView = (TabBarView) findViewById(R.id.tab_bar);
			tabBarView.setIndex(1);
		}


	}
	

	@Override
	public void onDestroy() {
		if(speedReceiver!=null){
			this.unregisterReceiver(speedReceiver);
		}
		if(bateryNumReceiver!=null){
			this.unregisterReceiver(bateryNumReceiver);
		}
		if(arsReceiver!=null){
			try{
				this.unregisterReceiver(arsReceiver);
			}catch(Exception e){
			}
		}
		if(gearReceiver!=null){
			try{
				this.unregisterReceiver(gearReceiver);
			}catch(Exception e){
			}
		}
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onBackPressed() {
		startActivity(new Intent(MeterActivity.this, RomtorActivity.class));
		overridePendingTransition(0, 0);
		MeterActivity.this.finish();
	}
	
	
	public class SpeedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(SPEED_TAG)) {
				String speedKm = intent.getStringExtra("speed_km");
				float speedKmFloat = Float.parseFloat(speedKm);
				float realSpeedDegree = 0f;
				
				if(speedKmFloat<=20){
					realSpeedDegree = (speedKmFloat) * 220 / 60;
				}else if(speedKmFloat>20 && speedKmFloat<25){
					realSpeedDegree = (speedKmFloat) * 240 / 60;
				}else{
					realSpeedDegree = (speedKmFloat) * 260 / 60;
				}
				tvSpNumber.setText("" + (int)speedKmFloat);
				setSpPointerAnimation(true , realSpeedDegree);
			}
		}
	}
	
	public class BateryNumReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BATERY_NUM_TAG)) {
				String bateryNum = intent.getStringExtra("textview_batery_num");
				System.out.println("----------BateryNumReceiver.bateryNum:" + bateryNum);
				textview_batery_num.setText(bateryNum + "%");
				progress_horizontal.setProgress(Integer.valueOf(bateryNum));
			}
		}
	}
	
	public class GearReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(GEAR_TAG)) {
				int gear = intent.getIntExtra("gear", 1);
				if(gear == 2){
					meter_mode_iv.setImageResource(R.drawable.mode_mountain);
					meter_mode_tv.setText("山地模式");
				}else if(gear == 1){
					meter_mode_iv.setImageResource(R.drawable.mode_city);
					meter_mode_tv.setText("城市模式");
				}else if(gear == 3){
					meter_mode_iv.setImageResource(R.drawable.mode_track);
					meter_mode_tv.setText("赛道模式");
				}
			}
		}
	}
	
	/**
	 * 设置速度仪表指针动画
	 */
	private void setSpPointerAnimation(boolean flag, float toDegress) {
		LinearInterpolator lin = new LinearInterpolator();
		if (flag) { // 正向
			spNormalAnim = new RotateAnimation(speedDegrees, toDegress,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		} else { // 反向
			spNormalAnim = new RotateAnimation(speedDegrees, toDegress,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		spNormalAnim.setDuration(timer); // 设置动画持续的时间
		spNormalAnim.setRepeatCount(0); // 设置重复次数
		spNormalAnim.setFillAfter(true); // 动画执行完后是否停留在执行完的状态
		spNormalAnim.setInterpolator(lin);
		ivSpPointer.startAnimation(spNormalAnim);
		spNormalAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
//				System.err.println("动画执行后: -->" + degress);
//				if (degress == 1.0) {
//					degress = 0;
//					tvSpNumber.setText((int) degress + "");
//				} else {
//					tvSpNumber.setText((int) degress + "");
//				}
			}
		});
		speedDegrees = toDegress;
	}
}
