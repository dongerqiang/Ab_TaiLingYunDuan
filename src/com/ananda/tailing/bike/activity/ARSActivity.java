package com.ananda.tailing.bike.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.entity.HelpInfoEntity;
import com.ananda.tailing.bike.entity.HelpListInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.ImageLoadManager;
import com.ananda.tailing.bike.util.JsonPaserUtils;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TabBarView;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * @package com.ananda.tailing.bike.activity
 * @description: ARS界面
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-7 下午1:34:32
 */
public class ARSActivity extends BaseActivity implements OnClickListener {
	
	/** 电机 */
	private LinearLayout layoutMotors;
	private LinearLayout layoutTMotors;
	private LinearLayout layoutOMotors;
	private Button btMotors;
	private ListView lvMotors;

	/** 转把 */
	private LinearLayout layoutTurn;
	private LinearLayout layoutTTurn;
	private LinearLayout layoutOTurn;
	private Button btTurn;
	private ListView lvTurn;

	/** 刹车 */
	private LinearLayout layoutBrake;
	private LinearLayout layoutTBrake;
	private LinearLayout layoutOBrake;
	private Button btBrake;
	private ListView lvBrake;

	/** 混合 */
	private LinearLayout layoutMixed;
	private LinearLayout layoutTMixed;
	private LinearLayout layoutOMixed;
	private Button btMixed;
	private ListView lvMixed;

	private LayoutInflater inflaters;
	private Animation enterAnimation;
	
	private Map<Integer, Integer> mMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> tMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> bMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> xMap = new HashMap<Integer, Integer>();
	private ScrollView scrollView;

	private ArsAdapter arsAdapter;
	private ImageLoadManager imageLoadManager;
	private LinearLayout m_emptyLayout;
	private LinearLayout t_emptyLayout;
	private LinearLayout b_emptyLayout;
	private LinearLayout x_emptyLayout;

	// 多媒体文件
	private MediaPlayer mediaPlayer = null;
	private PlaySound mPlaySound;
	private HashMap<Integer, Integer> spMap;
	private List<HelpListInfo> helpListInfoList;

	private int fault;
	private boolean errorPlayFlag = true;

	private MyDialog myDialog;
	private String userId;  // 用户登录ID
	
	private TitleBarView titleBarView;
//	private TabBarView tabBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.ars_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		inflaters = LayoutInflater.from(this);
		imageLoadManager = ImageLoadManager.getInstance(this);

		userId = PreferencesUtils.getString(this, "UserId");
		myDialog = new MyDialog(this, handler);
				
		initAnimation();
		titleBarView = (TitleBarView) findViewById(R.id.title_bar);
		titleBarView.setTitle("");
		errorPlayFlag = false;
		setDefaultSelect();
//		tabBarView = (TabBarView) findViewById(R.id.tab_bar);
//		tabBarView.setIndex(2);	
	}
	
	protected void setDefaultSelect(){
		String rssiStr = (String) getIntent().getSerializableExtra("RSSI");
		View view = null;
		if("1".equals(rssiStr)){
			view = findViewById(R.id.turn_layout);
		}else if("2".equals(rssiStr)){
			view = findViewById(R.id.brake_layout);
		}else if("3".equals(rssiStr)){
			view = findViewById(R.id.motors_layout);
		}else if("4".equals(rssiStr)){
			view = findViewById(R.id.mixed_layout);
		}
		autoPlaySound(rssiStr);
		if(view != null){
			errorPlayFlag = true;
			this.onClick(view);
			errorPlayFlag = false;
		}
		
	}
	
	protected void autoPlaySound(String rssiStr) {
		String ars = PreferencesUtils.getString(this, "ars");
		if (ars== null || "0".equals(ars)) {
			if ("1".equals(rssiStr)) {
				if (mediaPlayer != null) {
					try {
						mediaPlayer.stop();
						mediaPlayer.release();
					} catch (Exception e) {

					}

				}
				playAudio(R.raw.turn);
			} else if ("2".equals(rssiStr)) {
				if (mediaPlayer != null) {
					try {
						mediaPlayer.stop();
						mediaPlayer.release();
					} catch (Exception e) {

					}

				}
				playAudio(R.raw.brake);
			} else if ("3".equals(rssiStr)) {
				if (mediaPlayer != null) {
					try {
						mediaPlayer.stop();
						mediaPlayer.release();
					} catch (Exception e) {

					}

				}
				playAudio(R.raw.motors);
			} else if ("4".equals(rssiStr)) {
				if (mediaPlayer != null) {
					try {
						mediaPlayer.stop();
						mediaPlayer.release();
					} catch (Exception e) {

					}

				}
				playAudio(R.raw.mixed);
			}
		}
	}
	
	/* 初始化控件
	 * (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		scrollView = (ScrollView) findViewById(R.id.ars_scrollview);
		layoutMotors = (LinearLayout) findViewById(R.id.motors_layout);
		layoutTMotors = (LinearLayout) findViewById(R.id.motors_top_layout);
		layoutOMotors = (LinearLayout) findViewById(R.id.motors_open_layout);
		btMotors = (Button) findViewById(R.id.motors_audio_button);

		layoutTurn = (LinearLayout) findViewById(R.id.turn_layout);
		layoutTTurn = (LinearLayout) findViewById(R.id.turn_top_layout);
		layoutOTurn = (LinearLayout) findViewById(R.id.turn_open_layout);
		btTurn = (Button) findViewById(R.id.turn_audio_button);

		layoutBrake = (LinearLayout) findViewById(R.id.brake_layout);
		layoutTBrake = (LinearLayout) findViewById(R.id.brake_top_layout);
		layoutOBrake = (LinearLayout) findViewById(R.id.brake_open_layout);
		btBrake = (Button) findViewById(R.id.brake_audio_button);

		layoutMixed = (LinearLayout) findViewById(R.id.mixed_layout);
		layoutTMixed = (LinearLayout) findViewById(R.id.mixed_top_layout);
		layoutOMixed = (LinearLayout) findViewById(R.id.mixed_open_layout);
		btMixed = (Button) findViewById(R.id.mixed_audio_button);

		m_emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_motors);
		t_emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_turn);
		b_emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_brake);
		x_emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_mixed);

		mMap.put(1, 0);
		mMap.put(2, 8);
		tMap.put(1, 0);
		tMap.put(2, 8);
		bMap.put(1, 0);
		bMap.put(2, 8);
		xMap.put(1, 0);
		xMap.put(2, 8);

		layoutMotors.setOnClickListener(this);
		layoutTurn.setOnClickListener(this);
		layoutBrake.setOnClickListener(this);
		layoutMixed.setOnClickListener(this);
		btMotors.setOnClickListener(this);
		btTurn.setOnClickListener(this);
		btBrake.setOnClickListener(this);
		btMixed.setOnClickListener(this);

		lvMotors = (ListView) findViewById(R.id.motors_ars_listview);
		lvTurn = (ListView) findViewById(R.id.turn_ars_listview);
		lvBrake = (ListView) findViewById(R.id.brake_ars_listview);
		lvMixed = (ListView) findViewById(R.id.mixed_ars_listview);

		spMap = new HashMap<Integer, Integer>();
		spMap.put(1, R.raw.brake);     // 刹车
		spMap.put(2, R.raw.turn);      // 转把
		spMap.put(3, R.raw.motors);    // 电机
		spMap.put(4, R.raw.mixed);     // 综合

		if (CommonUtils.isNetWorkNormal(ARSActivity.this)) {
			getArsListWork();
		} else {			
			String arsList = PreferencesUtils.getString(ARSActivity.this, "ars_list");
			if(!TextUtils.isEmpty(arsList)) {
				JSONObject jsonObject = JSONObject.parseObject(arsList);
				if (jsonObject.getString("isSuccess").equals("true")) {
					helpListInfoList = JsonPaserUtils
							.parseJsonToArsInfo(jsonObject.getJSONArray("data")
									.toString());
					loadNewList(helpListInfoList);
					System.out.println("解析后的数据：-->" + helpListInfoList.size());
				} else {
					System.out.println(jsonObject.getString("errorMsg"));
				}
			} else {
				MyToast.showShortToast(ARSActivity.this, "网络不可用, 请先打开网络!");
			}
		}

		if (PreferencesUtils.getInt(ARSActivity.this, "fault") > 0) {
			fault = PreferencesUtils.getInt(ARSActivity.this, "fault");
			updateUI(fault, true);
		}
		
		
	}

	/**
	 * 请求网络获取ARS数据
	 */
	private void getArsListWork() {
		showProgressDialog(ARSActivity.this, "请稍后...");
		HttpRestClient.get(HttpAPI.ARS_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				dismissProgressDialog();
				System.err.println("错误信息: -->" + content);
				MyToast.showShortToast(ARSActivity.this, HttpAPI.RESPONSE);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				dismissProgressDialog();
				System.err.println("ARS返回数据：-->" + content);
				PreferencesUtils.putString(ARSActivity.this, "ars_list", content); 
				JSONObject jsonObject = JSONObject.parseObject(content);
				if (jsonObject.getString("isSuccess").equals("true")) {
					helpListInfoList = JsonPaserUtils
							.parseJsonToArsInfo(jsonObject.getJSONArray("data")
									.toString());
					loadNewList(helpListInfoList);
					System.out.println("解析后的数据：-->" + helpListInfoList.size());
				} else {
					System.out.println(jsonObject.getString("errorMsg"));
				}
			}

		});
	}

	/**
	 * 加载列表数据
	 * 
	 * @param helpListInfoList
	 */
	private void loadNewList(List<HelpListInfo> helpListInfoList) {
		for (int i = 0; i < helpListInfoList.size(); i++) {
			if (helpListInfoList.get(i).getGzid().equals("1")) {
				
				if (helpListInfoList.get(i).getData().size() > 0) {
					arsAdapter = new ArsAdapter(helpListInfoList.get(i)
							.getData());
					lvBrake.setAdapter(arsAdapter);
				} else {
					lvBrake.setEmptyView(b_emptyLayout);
				}

			} else if (helpListInfoList.get(i).getGzid().equals("2")) {
				if (helpListInfoList.get(i).getData().size() > 0) {
					arsAdapter = new ArsAdapter(helpListInfoList.get(i)
							.getData());
					lvTurn.setAdapter(arsAdapter);
				} else {
					lvTurn.setEmptyView(t_emptyLayout);
				}

			} else if (helpListInfoList.get(i).getGzid().equals("3")) {
				if (helpListInfoList.get(i).getData().size() > 0) {
					arsAdapter = new ArsAdapter(helpListInfoList.get(i)
							.getData());
					lvMotors.setAdapter(arsAdapter);
				} else {
					lvMotors.setEmptyView(m_emptyLayout);
				}

			} else if (helpListInfoList.get(i).getGzid().equals("4")) {
				if (helpListInfoList.get(i).getData().size() > 0) {
					arsAdapter = new ArsAdapter(helpListInfoList.get(i)
							.getData());
					lvMixed.setAdapter(arsAdapter);
				} else {
					lvMixed.setEmptyView(x_emptyLayout);
				}
			}
		}
	}

	/**
	 * 初始化动画资源
	 */
	private void initAnimation() {
		enterAnimation = AnimationUtils.loadAnimation(ARSActivity.this,
				R.anim.top_menu_enter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (scrollView != null) {
			scrollView.scrollTo(10, 10);
		}
		// TODO Auto-generated method stub
		switch (v.getId()) {		
		// 刹车故障
		case R.id.brake_layout: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				setTextInforMation(1);
				if(!errorPlayFlag){
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
				}
				
				if (arsAdapter != null) {
					arsAdapter.notifyDataSetChanged();
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		// 转把故障
		case R.id.turn_layout: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				setTextInforMation(2);
				if(!errorPlayFlag){
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
				}
				if (arsAdapter != null) {
					arsAdapter.notifyDataSetChanged();
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		// 电机故障
		case R.id.motors_layout: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				setTextInforMation(3);
				if(!errorPlayFlag){
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
				}
				if (arsAdapter != null) {
					arsAdapter.notifyDataSetChanged();
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		} 
		// 混合故障
		case R.id.mixed_layout: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				setTextInforMation(4);
				if(!errorPlayFlag){
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
				}
				if (arsAdapter != null) {
					arsAdapter.notifyDataSetChanged();
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		// 刹车故障语音
		case R.id.brake_audio_button: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				if (PreferencesUtils.getString(ARSActivity.this, "ars", "0").equals(
						"0")) {
					//mPlaySound = new PlaySound(spMap.get(1));
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
					playAudio(spMap.get(1));
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		// 转把故障语音
		case R.id.turn_audio_button: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				if (PreferencesUtils.getString(ARSActivity.this, "ars", "0").equals(
						"0")) {
					//mPlaySound = new PlaySound(spMap.get(2));
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
					playAudio(spMap.get(2));
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		// 电机故障语音
		case R.id.motors_audio_button: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				if (PreferencesUtils.getString(ARSActivity.this, "ars", "0").equals(
						"0")) {
					//mPlaySound = new PlaySound(spMap.get(3));
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
					playAudio(spMap.get(3));
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		// 混合故障语音
		case R.id.mixed_audio_button: {
//			if(!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				if (PreferencesUtils.getString(ARSActivity.this, "ars", "0").equals(
						"0")) {
					//mPlaySound = new PlaySound(spMap.get(4));
					if (mediaPlayer != null) {
						try{
							mediaPlayer.stop();
							mediaPlayer.release();
						}catch(Exception e){
							
						}
						
					}
					playAudio(spMap.get(4));
				}
//			} else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//			}
			break;
		}
		}
	}

	/**
	 * 播放声音类
	 * 
	 * @package com.ananda.tailing.bike.activity
	 * @description:
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-3-17 上午10:13:41
	 */
	class PlaySound {

		SoundPool soundPool;
		int loadId;

		public PlaySound(int resId) {
			soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
			loadId = soundPool.load(ARSActivity.this, resId, 1);
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					// TODO Auto-generated method stub
					soundPool.play(loadId, 1, 1, 0, 0, 1.0f);
				}
			});
		}

		public void destory() {
			soundPool.unload(loadId);
			soundPool.release();
		}
	}

	/**
	 * 播放录音
	 */
	private void playAudio(int resId) {
		String voiceControl = PreferencesUtils.getString(ARSActivity.this, "voice_control");
		if(voiceControl != null && "1".equals(voiceControl)){
			return;
		}else{
			try {
				 //获取音频服务
	            mediaPlayer = MediaPlayer.create (ARSActivity.this, resId);	 //设置音频源
	            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置流类型
	            mediaPlayer.setLooping(false);	 //设置是否循环播放
	            mediaPlayer.start();	 //开始播放
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 逻辑转换设置
	 * 
	 * @param index
	 */
	private void setTextInforMation(int index) {
		if (CommonUtils.isNetWorkNormal(ARSActivity.this)) {
			updateUI(index, false);
		} else {
			String arsList = PreferencesUtils.getString(ARSActivity.this, "ars_list");
			if(!TextUtils.isEmpty(arsList)) {
				JSONObject jsonObject = JSONObject.parseObject(arsList);
				if (jsonObject.getString("isSuccess").equals("true")) {
					helpListInfoList = JsonPaserUtils
							.parseJsonToArsInfo(jsonObject.getJSONArray("data")
									.toString());
					loadNewList(helpListInfoList);
 					System.out.println("解析后的数据：-->" + helpListInfoList.size());
				} else {
					System.out.println(jsonObject.getString("errorMsg"));
				}
				updateUI(index, false);
			} else {
				MyToast.showShortToast(ARSActivity.this, "网络不可用, 请先打开网络!");
			}
		}
	}

	/**
	 * @param index
	 */
	private void updateUI(int index, boolean isOk) {
		// 刹车
		layoutBrake.setVisibility((index == 1) ? View.GONE : View.VISIBLE);
		layoutTBrake.setVisibility((index == 1) ? View.VISIBLE : View.GONE);
		layoutOBrake.setVisibility((index == 1) ? View.VISIBLE : View.GONE);
		// 转把
		layoutTurn.setVisibility((index == 2) ? View.GONE : View.VISIBLE);
		layoutTTurn.setVisibility((index == 2) ? View.VISIBLE : View.GONE);
		layoutOTurn.setVisibility((index == 2) ? View.VISIBLE : View.GONE);
		// 电机
		layoutMotors.setVisibility((index == 3) ? View.GONE : View.VISIBLE);
		layoutTMotors.setVisibility((index == 3) ? View.VISIBLE : View.GONE);
		layoutOMotors.setVisibility((index == 3) ? View.VISIBLE : View.GONE);		
		// 综合
		layoutMixed.setVisibility((index == 4) ? View.GONE : View.VISIBLE);
		layoutTMixed.setVisibility((index == 4) ? View.VISIBLE : View.GONE);
		layoutOMixed.setVisibility((index == 4) ? View.VISIBLE : View.GONE);

		if(isOk) {
			layoutBrake.setEnabled((fault == 1) ? true : false);
			layoutTurn.setEnabled((fault == 2) ? true : false);
			layoutMotors.setEnabled((fault == 3) ? true : false);
			layoutMixed.setEnabled((fault == 4) ? true : false);
		}

		switch (index) {
		case 1: {
			layoutOBrake.startAnimation(enterAnimation);			
			break;
		}
		case 2: {
			layoutOTurn.startAnimation(enterAnimation);
			break;
		}
		case 3: {
			layoutOMotors.startAnimation(enterAnimation);
			break;
		}
		case 4: {
			layoutOMixed.startAnimation(enterAnimation);
			break;
		}
		}
	}

	class ArsAdapter extends BaseAdapter {

		private List<HelpInfoEntity> listHelp;
		DisplayImageOptions options;

		public ArsAdapter(List<HelpInfoEntity> listHelp) {
			this.listHelp = listHelp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listHelp.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listHelp.get(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View createView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (createView == null) {
				createView = inflaters.inflate(R.layout.ars_item_layout, null);
				holder = new ViewHolder();
				holder.ivArs = (ImageView) createView
						.findViewById(R.id.ars_item_imageview);
				holder.tvArs = (TextView) createView
						.findViewById(R.id.ars_item_str_textview);
				holder.tvStep = (TextView) createView
						.findViewById(R.id.step_textview);
				createView.setTag(holder);
			} else {
				holder = (ViewHolder) createView.getTag();
			}

			holder.tvStep.setText(listHelp.get(position).getTitle());
			holder.tvArs.setText(listHelp.get(position).getContent());
			imageLoadManager.display(listHelp.get(position).getImageurl(),
					holder.ivArs);

			return createView;
		}
	}

	class ViewHolder {
		ImageView ivArs;
		TextView tvStep;
		TextView tvArs;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mPlaySound != null) {
			mPlaySound.destory();
			mPlaySound = null;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			if(myDialog != null) {
				myDialog.dismiss();
			}
		}		
	};
	
	public void onBackPressed() {
		startActivity(new Intent(ARSActivity.this, RomtorActivity.class));
		overridePendingTransition(0, 0);
		ARSActivity.this.finish();
	}
	
}
