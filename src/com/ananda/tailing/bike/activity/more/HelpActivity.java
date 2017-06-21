package com.ananda.tailing.bike.activity.more;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.entity.HelpInfoEntity;
import com.ananda.tailing.bike.entity.HelpListInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.ImageLoadManager;
import com.ananda.tailing.bike.util.JsonPaserUtils;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 帮助信息
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:43:47
 */
public class HelpActivity extends BaseActivity implements OnClickListener {
		
	private TitleBarView mTitleBarView;
	
	private LinearLayout layoutCommon;
	private LinearLayout layoutCTop;
	private LinearLayout layoutCOpen;
	private ListView lvCommon;
	
	private LinearLayout layoutInstruct;
	private LinearLayout layoutITop;
	private LinearLayout layoutIOpen;
	private ListView lvInstruct;
	
	private ScrollView hScrollView;
	private LayoutInflater inflater;
	private Animation enterAnimation;
	
	private Map<Integer, Integer> cMap;
	private Map<Integer, Integer> iMap;
	
	private HelpAdapter helpAdapter;
	private ImageLoadManager imageLoad;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.help_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		inflater = LayoutInflater.from(this);
		imageLoad = ImageLoadManager.getInstance(this);
		
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("帮助信息");
		initAnimation();
	}
	
	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		hScrollView = (ScrollView) findViewById(R.id.help_scrollview);
		
		layoutCommon = (LinearLayout) findViewById(R.id.common_layout);
		layoutCTop = (LinearLayout) findViewById(R.id.common_top_layout);
		layoutCOpen = (LinearLayout) findViewById(R.id.common_open_layout);
		
		layoutInstruct = (LinearLayout) findViewById(R.id.instruct_layout);
		layoutITop = (LinearLayout) findViewById(R.id.instruct_top_layout);
		layoutIOpen = (LinearLayout) findViewById(R.id.instruct_open_layout);
		

		cMap = new HashMap<Integer, Integer>();
		iMap = new HashMap<Integer, Integer>();		
		cMap.put(1, 0);
		cMap.put(2, 8);
		
		iMap.put(1, 0);
		iMap.put(2, 8);
				
		lvCommon = (ListView) findViewById(R.id.common_help_listview);
		lvInstruct = (ListView) findViewById(R.id.instruct_help_listview);

		if (CommonUtils.isNetWorkNormal(HelpActivity.this)) { 
			getHelpListWork(); 
		} else {
			String helpList = PreferencesUtils.getString(HelpActivity.this, "help_list");
			if(!TextUtils.isEmpty(helpList)) {
				JSONObject jsonObject = JSONObject.parseObject(helpList); 
				if (jsonObject.getString("isSuccess").equals("true")) {
					List<HelpListInfo> helpListInfoList = JsonPaserUtils
							.parseJsonToArsInfo(jsonObject.getJSONArray("data")
									.toString());
					loadNewList(helpListInfoList);
					System.out.println("解析后的数据：-->" + helpListInfoList.size());
				} else {
					System.out.println(jsonObject.getString("errorMsg"));
				}
			} else {			
				MyToast.showShortToast(HelpActivity.this, "网络不可用, 请先打开网络!");
			}
		}	
		
	}
	
	/**
	 * 初始化动画资源
	 */
	private void initAnimation() {
		enterAnimation = AnimationUtils.loadAnimation(this, 
				R.anim.top_menu_enter);
	}
	
	/**
	 * 请求网络获取帮助信息
	 */
	private void getHelpListWork() {
		showProgressDialog(HelpActivity.this, "请稍后...");		
		HttpRestClient.get(HttpAPI.HELP_URL, new AsyncHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				dismissProgressDialog();
				MyToast.showShortToast(HelpActivity.this, HttpAPI.RESPONSE);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				dismissProgressDialog();
				System.err.println("ARS返回数据：-->" + content);
				PreferencesUtils.putString(HelpActivity.this, "help_list", content); 
				JSONObject jsonObject = JSONObject.parseObject(content); 
				if (jsonObject.getString("isSuccess").equals("true")) {
					List<HelpListInfo> helpListInfoList = JsonPaserUtils
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


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {		
		// TODO Auto-generated method stub
		if(hScrollView != null) {
			hScrollView.scrollTo(10, 10);
		}
		switch(v.getId()) {
		// 常见问题
		case R.id.common_layout: {
			setTextInforMation(1);
			if(helpAdapter != null) {
				helpAdapter.notifyDataSetChanged();
			}
			break;
		}
		// 使用说明
		case R.id.instruct_layout: {
			setTextInforMation(2);
			if(helpAdapter != null) {
				helpAdapter.notifyDataSetChanged();
			}
			break;
		}
		// 常见问题语音
		case R.id.common_audio_button: {
			
			break;
		}
		// 使用说明语音
		case R.id.instruct_audio_button: {
			
			break;
		}
		}
	}
	
	class PlaySound {
		
		SoundPool soundPool; 
		int loadId;
		
		public PlaySound(int resId){
			soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
			loadId = soundPool.load(HelpActivity.this, resId, 1);
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					// TODO Auto-generated method stub
					soundPool.play(loadId, 1, 1, 0, 0, 1.0f);
				}
			});			 
		}
		
		public void destory(){
			soundPool.unload(loadId);
			soundPool.release();
		}
	}
		
	/**
	 * 逻辑转换设置
	 * @param index
	 */
	private void setTextInforMation(int index) {
		if (CommonUtils.isNetWorkNormal(HelpActivity.this)) { 
			updateUI(index);
		} else {
			String helpList = PreferencesUtils.getString(HelpActivity.this, "help_list");
			if(!TextUtils.isEmpty(helpList)) {
				JSONObject jsonObject = JSONObject.parseObject(helpList); 
				if (jsonObject.getString("isSuccess").equals("true")) {
					List<HelpListInfo> helpListInfoList = JsonPaserUtils
							.parseJsonToArsInfo(jsonObject.getJSONArray("data")
									.toString());
					loadNewList(helpListInfoList);
					System.out.println("解析后的数据：-->" + helpListInfoList.size());
				} else {
					System.out.println(jsonObject.getString("errorMsg"));
				}
				updateUI(index);
			} else {			
				MyToast.showShortToast(HelpActivity.this, "网络不可用, 请先打开网络!");
			}
		}	
		
	}
	
	/**
	 * @param index
	 */
	private void updateUI(int index) {
		layoutCommon.setVisibility((index==1)?View.GONE:View.VISIBLE);
		layoutCTop.setVisibility((index==1)?View.VISIBLE:View.GONE);
		layoutCOpen.setVisibility((index==1)?View.VISIBLE:View.GONE);
				
		layoutInstruct.setVisibility((index==2)?View.GONE:View.VISIBLE);
		layoutITop.setVisibility((index==2)?View.VISIBLE:View.GONE);
		layoutIOpen.setVisibility((index==2)?View.VISIBLE:View.GONE);
		
		switch(index) {
		case 1: {
			layoutCOpen.startAnimation(enterAnimation);
			break;
		}
		case 2: {
			layoutIOpen.startAnimation(enterAnimation);
			break;
		}
		}
	}
	
	/**
	 * 设置不同的数据源
	 * @param index
	 */
	private void loadNewList(List<HelpListInfo> helpListInfoList){
		for(int i = 0; i < helpListInfoList.size(); i++) {
			if(helpListInfoList.get(i).getGzid().equals("1")) {
				helpAdapter = new HelpAdapter(helpListInfoList.get(i).getData());
				lvCommon.setAdapter(helpAdapter);
				
			} else if(helpListInfoList.get(i).getGzid().equals("2")) {
				helpAdapter = new HelpAdapter(helpListInfoList.get(i).getData());
				lvInstruct.setAdapter(helpAdapter);
				
			}	
		}
	}
	
	class HelpAdapter extends BaseAdapter {
		
		private List<HelpInfoEntity> helpList;
		
		private HelpAdapter(List<HelpInfoEntity> helpList) {
			this.helpList = helpList;	
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return helpList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return helpList.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View createView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(createView == null) {
				createView = inflater.inflate(R.layout.help_item_layout, null);
				holder = new ViewHolder();
				holder.ivHelp = (ImageView) createView.findViewById(R.id.help_item_imageview);
				holder.tvHelp = (TextView) createView.findViewById(R.id.help_item_str_textview);
				holder.tvStep = (TextView) createView.findViewById(R.id.step_textview);
				createView.setTag(holder);
			} else {
				holder = (ViewHolder) createView.getTag();
			}
			
			holder.tvStep.setText(helpList.get(position).getTitle());
			holder.tvHelp.setText(helpList.get(position).getContent());
			//imageLoad.display(helpList.get(position).getImageurl(), holder.ivHelp);
			
			return createView;
		}
		
	}
	
	class ViewHolder {
		ImageView ivHelp;
		TextView tvHelp;
		TextView tvStep;
	}
	
}
