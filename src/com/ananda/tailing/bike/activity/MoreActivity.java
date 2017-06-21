package com.ananda.tailing.bike.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.more.AboutActivity;
import com.ananda.tailing.bike.activity.more.ArsReceiver;
import com.ananda.tailing.bike.activity.more.GoodsActivity;
import com.ananda.tailing.bike.activity.more.HelpActivity;
import com.ananda.tailing.bike.activity.more.LoginActivity;
import com.ananda.tailing.bike.activity.more.SettingActivity;
import com.ananda.tailing.bike.activity.more.UserInfoActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TabBarView;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 更多
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:16:04
 */
public class MoreActivity extends BaseActivity implements OnClickListener {

	private Intent intent;
	private ImageView layoutLogin, layoutHelp, layoutUser, layoutGoods,
		layoutSet, layoutArs,layoutAbout;
	
	private Context context;
	private TitleBarView titleBarView;
	private TabBarView tabBarView;
	private ArsReceiver arsReceiver;
	
	private String userId; // 用户登录ID
	private MyDialog myDialog;
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
		setContentView(R.layout.more_layout);
		super.onCreate(savedInstanceState);		
		CommonUtils.subActivityStack.add(this);	
		context = this;
		myDialog = new MyDialog(this, dialogHandler);
		userId = PreferencesUtils.getString(this, "UserId");

//		if(RomtorActivity.arsServiceFlag){
			IntentFilter arsFilter = new IntentFilter(RomtorActivity.ARS_TAG);
			arsReceiver = new ArsReceiver(context);
			this.registerReceiver(arsReceiver, arsFilter);
//		}
	}
	

	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		titleBarView = (TitleBarView) findViewById(R.id.title_bar);
		titleBarView.setTitle("更多功能");
		
		tabBarView = (TabBarView) findViewById(R.id.tab_bar);
		tabBarView.setIndex(3);

		
	    layoutLogin = (ImageView) findViewById(R.id.imageview_login);
	    layoutHelp = (ImageView) findViewById(R.id.imageview_help);
	    layoutUser = (ImageView) findViewById(R.id.imageview_user);
	    layoutGoods = (ImageView) findViewById(R.id.imageview_goods);
	    layoutSet = (ImageView) findViewById(R.id.imageview_settings);
	    layoutArs = (ImageView) findViewById(R.id.imageview_ars);
	    layoutAbout = (ImageView) findViewById(R.id.imageview_about);
	    
	    layoutLogin.setOnClickListener(this);
	    layoutHelp.setOnClickListener(this);
	    layoutUser.setOnClickListener(this);
	    layoutGoods.setOnClickListener(this);
	    layoutSet.setOnClickListener(this);
	    layoutArs.setOnClickListener(this);
	    layoutAbout.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.imageview_login: {   // 登录/注册
			intent = new Intent(this, LoginActivity.class);
			intent.putExtra("first", "1");
			break;
		}
		case R.id.imageview_help: {    // 帮助信息
			intent = new Intent(this, HelpActivity.class);
			break;
		}
		case R.id.imageview_user: {    // 用户信息
			intent = new Intent(this, UserInfoActivity.class);
			break;
		}
		case R.id.imageview_goods: {   // 产品信息
			intent = new Intent(this, GoodsActivity.class);
			break;
		}
		case R.id.imageview_settings: {// 设置
//			if (!TextUtils.isEmpty(userId) && !userId.equals("0")) {
				intent = new Intent(this, SettingActivity.class);
				break;
//			}else {
//				myDialog.show();
//				myDialog.setTextInfo("请先登录!");
//				break;
//			}
		}
		case R.id.imageview_ars: {// Ars
			intent = new Intent(this, ARSActivity.class);
			break;
		}
		case R.id.imageview_about: {   // 关于我们
			intent = new Intent(this, AboutActivity.class);
			break;
		}
		}
		if(intent != null){
			startActivity(intent);
		}
		
	}
	
	@Override
	public void onDestroy() {
		if(arsReceiver!=null){
			try{
				this.unregisterReceiver(arsReceiver);
			}catch(Exception e){
			}
		}
		super.onDestroy();
	}
	
	public void onBackPressed() {
		startActivity(new Intent(MoreActivity.this, RomtorActivity.class));
		overridePendingTransition(0, 0);
		MoreActivity.this.finish();
	}

}
