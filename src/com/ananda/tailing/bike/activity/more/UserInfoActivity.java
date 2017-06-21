package com.ananda.tailing.bike.activity.more;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.entity.UserInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.JsonPaserUtils;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 用户信息
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:51:25
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {
	
	private TitleBarView mTitleBarView;

	private LinearLayout layoutMore;
	private ImageView ivDrop;
	
	private TextView tvName, tvSex, tvBirthday, tvJob, tvUse, tvIDCard, tvMobile;
	
	private MyDialog myDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.userinfo_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("用户信息");
		
		myDialog = new MyDialog(this, handler);
		
	}
	
	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		layoutMore = (LinearLayout) findViewById(R.id.more_mes_linearlayout);
		ivDrop = (ImageView) findViewById(R.id.drop_imageview);
		tvName = (TextView) findViewById(R.id.name_textview);
		tvSex = (TextView) findViewById(R.id.sex_textview);
		tvBirthday = (TextView) findViewById(R.id.birthday_textview);
		tvJob = (TextView) findViewById(R.id.job_textview);
		tvUse = (TextView) findViewById(R.id.use_textview);
		tvIDCard = (TextView) findViewById(R.id.id_card_textview);
		tvMobile = (TextView) findViewById(R.id.mobile_textview);
		
		if (CommonUtils.isNetWorkNormal(UserInfoActivity.this)) { 
			getUserInfo();
		} else {
			MyToast.showShortToast(UserInfoActivity.this, "网络不可用, 请先打开网络!");
		}		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.user_edit_button) {
			Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
			startActivity(intent);
		} else if(v.getId() == R.id.drop_imageview) {
			layoutMore.setVisibility(View.VISIBLE);
			ivDrop.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获取用户信息
	 */
	private void getUserInfo() {
		showProgressDialog(UserInfoActivity.this, "请稍后...");
		try {
			JSONObject jsonUser = new JSONObject();
			jsonUser.put("UserId", PreferencesUtils.getString(UserInfoActivity.this, "UserId", ""));
			jsonUser.put("Timestamp", CommonUtils.getTimestamp());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.GETUSERINFO);
			jsonObject.put("Info", JSONObject.parseObject(jsonUser.toString()));
			
			System.err.println("获取用户信息报文: -->" + jsonUser.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			
			HttpRestClient.post(UserInfoActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog();
					MyToast.showShortToast(UserInfoActivity.this, HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					System.err.println("获取用户信息返回报文: -->" + content); 
					JSONObject jsonObject = JSONObject.parseObject(content); 
					String result = jsonObject.getString("Result");
					if(result.equals("040")) {
						UserInfo userInfo = JsonPaserUtils.parseJsonToUserInfo(jsonObject.getString("Info"));
						tvName.setText(userInfo.getName());
						tvSex.setText(userInfo.getSex());
						tvBirthday.setText(userInfo.getBirthday());
						tvJob.setText(userInfo.getJob());
						tvUse.setText(userInfo.getUse());
						tvIDCard.setText(userInfo.getIdintity());
						tvMobile.setText(userInfo.getMobile());
					} else {
						String info = jsonObject.getString("Info");
						JSONObject jsonInfo = JSONObject.parseObject(info);
						String message = jsonInfo.getString("Message");
						myDialog.show();
						myDialog.setTextInfo(message);
					}
				}
			});
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Handler handler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			if(myDialog != null) {
				myDialog.dismiss();
				myDialog = null;
			}
		}
		
	};

}
