package com.ananda.tailing.bike.activity.more;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.activity.CarStatusActivity;
import com.ananda.tailing.bike.activity.MoreActivity;
import com.ananda.tailing.bike.activity.MyApplication;
import com.ananda.tailing.bike.activity.RomtorActivity;
import com.ananda.tailing.bike.data.BaseResponse;
import com.ananda.tailing.bike.data.CarInfoResponse;
import com.ananda.tailing.bike.data.Constants;
import com.ananda.tailing.bike.entity.CarInfo;
import com.ananda.tailing.bike.net.HttpExecute;
import com.ananda.tailing.bike.net.HttpRequest;
import com.ananda.tailing.bike.net.HttpResponseListener;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TitleBarView;
import com.fu.baseframe.net.CallServer;
import com.fu.baseframe.net.CustomDataRequest;
import com.fu.baseframe.net.HttpListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 登录
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:21:48
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private Intent intent;
	private TitleBarView mTitleBarView;
	
	/** 用户名、密码   */
	private LinearLayout layoutUserName, layoutPassword;
	private EditText etUserName, etPassword;
		
	/** 保持登录状态  */
//	private CheckBox checkPwd;
	
	private MyDialog myDialog;
	
	private String type;
	private LinearLayout login_ll;
	private LinearLayout login_ll2;
	private Button login_cancellation_usernmae,codeButton;
	private TextView edittext_user_name2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.login_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("登录");
		
		myDialog = new MyDialog(this, handler);
		
		type = this.getIntent().getExtras().getString("first");
	
	}
	
	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		layoutUserName = (LinearLayout) findViewById(R.id.user_name_layout);
		layoutPassword = (LinearLayout) findViewById(R.id.password_layout);
		etUserName = (EditText) findViewById(R.id.edittext_user_name);
		etPassword = (EditText) findViewById(R.id.edittext_password);
		codeButton = (Button)findViewById(R.id.btn_yzm);
		etUserName.addTextChangedListener(new EditTextWatcher(etUserName));
		etPassword.addTextChangedListener(new EditTextWatcher(etPassword));
		
//		checkPwd = (CheckBox) findViewById(R.id.checkbox_rember_password);
		login_ll = (LinearLayout) findViewById(R.id.login_ll);
		login_ll2 = (LinearLayout) findViewById(R.id.login_ll2);
		login_cancellation_usernmae = (Button) findViewById(R.id.login_cancellation_usernmae);
		edittext_user_name2 = (TextView) findViewById(R.id.edittext_user_name2);
		
		if(PreferencesUtils.getBoolean(LoginActivity.this, "isChecked")) {
			if(PreferencesUtils.getBoolean(LoginActivity.this, "LoginFlag")){
				edittext_user_name2.setText(PreferencesUtils.getString(LoginActivity.this, "LoginName"));
				login_ll.setVisibility(View.GONE);
				login_ll2.setVisibility(View.VISIBLE);
			}
//			etUserName.setText(PreferencesUtils.getString(LoginActivity.this, "LoginName"));
//			etPassword.setText(PreferencesUtils.getString(LoginActivity.this, "Password"));
//			checkPwd.setChecked(true);
		}
		login_cancellation_usernmae.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PreferencesUtils.putBoolean(LoginActivity.this, "LoginFlag",false);
				PreferencesUtils.putString(LoginActivity.this, "UserId", ""); 
				PreferencesUtils.putString(LoginActivity.this, "Password", "");
				PreferencesUtils.putBoolean(LoginActivity.this, "isChecked",false);
//				checkPwd.setChecked(false);
				login_ll.setVisibility(View.VISIBLE);
				login_ll2.setVisibility(View.GONE);
			}
		});
		
		/*checkPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					PreferencesUtils.putBoolean(LoginActivity.this, "isChecked", isChecked);
					PreferencesUtils.putString(LoginActivity.this, "LoginName", etUserName.getText().toString().trim());
					PreferencesUtils.putString(LoginActivity.this, "Password", etPassword.getText().toString().trim());
				} else {
					PreferencesUtils.putBoolean(LoginActivity.this, "isChecked", isChecked);
				}
			}
		});*/
												
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.update_pwd_layout: {   // 修改密码
			intent = new Intent(LoginActivity.this, AmendPwdActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.found_pwd_layout: {    // 找回密码
			intent = new Intent(LoginActivity.this, LookForPwdActivity.class);	
			startActivity(intent);
			break;
		}
		case R.id.cancellation_user_layout: {   // 注销用户名
			intent = new Intent(LoginActivity.this, CancellationActivity.class);		
			startActivity(intent);
			break;
		}
		case R.id.login_button: {      // 登录
			checkLoginInput();	 
			break;
		} 
		case R.id.register_button: {   // 注册
			intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btn_yzm:
			getPinCode();
			startTimer();
			break;
		}		
	}	
	
	/**
	 * 检查登录信息
	 */
	private void checkLoginInput() {
		if(TextUtils.isEmpty(etUserName.getText().toString().trim())) {
			MyToast.showShortToast(LoginActivity.this, "请输入用户名!");
			return;
		} 
		
		if(TextUtils.isEmpty(etPassword.getText().toString())) { 
			MyToast.showShortToast(LoginActivity.this, "请输入验证码!");
			return;
		}
		if (CommonUtils.isNetWorkNormal(this)) { 
			submitLogin();
		} else {
			MyToast.showShortToast(this, "网络不可用, 请先打开网络!");
		}	
	}
	
	
	private void getDeviceKey(){
		try {
			JSONObject jsonLogin = new JSONObject();
			jsonLogin.put("Name", etUserName.getText().toString().trim());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.GETKEY);
			jsonObject.put("Info", JSONObject.parseObject(jsonLogin.toString()));
			
			System.err.println("获取设备key报文：-->" + jsonObject.toString()); 
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpRestClient.post(LoginActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog();
					MyToast.showShortToast(LoginActivity.this, HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					System.err.println("获取设备key返回报文： -->" + content); 
					JSONObject jsonObject = JSONObject.parseObject(content); 
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("120")) {								
						String deviceKey = jsonInfo.getString("UserKey");
						PreferencesUtils.putString(LoginActivity.this, "UserKey", deviceKey); 
					} else {
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
	
	/**
	 * 提交登录数据
	 */
	private void submitLogin() {	
		/*showProgressDialog(LoginActivity.this, "请稍后...");
		try {
			JSONObject jsonLogin = new JSONObject();
			jsonLogin.put("Name", etUserName.getText().toString().trim());
			PreferencesUtils.putString(LoginActivity.this, "UserName", etUserName.getText().toString().trim()); 
			jsonLogin.put("Password", etPassword.getText().toString().trim());
			jsonLogin.put("Timestamp", CommonUtils.getTimestamp());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.LOGIN);
			jsonObject.put("Info", JSONObject.parseObject(jsonLogin.toString()));
			
			System.err.println("登录报文：-->" + jsonObject.toString()); 
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpRestClient.post(LoginActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog();
					MyToast.showShortToast(LoginActivity.this, HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					getDeviceKey();
					System.err.println("登录返回报文： -->" + content); 
					JSONObject jsonObject = JSONObject.parseObject(content); 
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("060")) {								
						String userId = jsonInfo.getString("UserId");
						// 保存用户唯一ID
						PreferencesUtils.putBoolean(LoginActivity.this, "LoginFlag", true); 
						PreferencesUtils.putString(LoginActivity.this, "UserId", userId); 
						PreferencesUtils.putString(LoginActivity.this, "Password", 
								etPassword.getText().toString().trim());
						myDialog = new MyDialog(LoginActivity.this, handler);
						myDialog.show();
						myDialog.setTextInfo("登录成功!");
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								startActivity(new Intent(LoginActivity.this, RomtorActivity.class));
								LoginActivity.this.finish();
							}
						}, 1000);						
					} else {
						String message = jsonInfo.getString("Message");
						myDialog = new MyDialog(LoginActivity.this, handler);
						myDialog.show();
						myDialog.setTextInfo(message);
					}		
				}				
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("verificationCode", etPassword.getText().toString().trim());
		param.put("mobileNo", etUserName.getText().toString().trim());
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, Constants.LOGIN_URL, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				if(result.statusCode == 200){
					MyToast.showShortToast(LoginActivity.this, "登录成功！");	
					PreferencesUtils.putBoolean(LoginActivity.this, "LoginFlag", true); 
					MyApplication.MOBILE = etUserName.getText().toString().trim();
					PreferencesUtils.putString(LoginActivity.this, "UserName", etUserName.getText().toString().trim()); 
					
					startActivity(new Intent(LoginActivity.this, RomtorActivity.class));
					LoginActivity.this.finish();
				}else{
					Toast.makeText(LoginActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, param, "POST", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
	}
	
	/**
	 * EditText文本改变状态处理
	 * @package com.ananda.tailing.bike.activity.more
	 * @description: 
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-2-26 上午10:08:28
	 */
	class EditTextWatcher implements TextWatcher {

		private EditText et;
		
		public EditTextWatcher(EditText edit) {
			this.et = edit;
		}
		
		/* (non-Javadoc)
		 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		 */
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			if(et.getTag().equals("1")) {
				layoutUserName.setSelected(true);
			} else {
				layoutPassword.setSelected(true);
			}
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(type) &&  type.equals("0")) { 
			startActivity(new Intent(LoginActivity.this, RomtorActivity.class));
			LoginActivity.this.finish();			
		} else {
			startActivity(new Intent(LoginActivity.this, MoreActivity.class));
			LoginActivity.this.finish();
		}
	}
	public Timer timer;
	int time = 0;
	public void startTimer(){
		time = 60;
		codeButton.setEnabled(false);
		
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if(time < 0){
							stopTimer();
							return;
						}
						codeButton.setText((time--)+"s");
					}
				});
			}
		}, 0, 1000);
	}
	
	public void stopTimer(){
		codeButton.setEnabled(true);
		codeButton.setText(R.string.pin_code);
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	
	private void getPinCode() {
			Map<String, String> param = new HashMap<String, String>();
			param.put("mobileNo", etUserName.getText().toString().trim());
			
			HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, Constants.PIN_CODE_URL, new HttpResponseListener<BaseResponse>() {

				@Override
				public void onResult(BaseResponse result) {
					if(result == null) return;
					if(result.statusCode == 200){
						MyToast.showShortToast(LoginActivity.this, "获取验证码成功！");		
					}else{
						Toast.makeText(LoginActivity.this, result.message, Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFail(int code) {
					
				}
			}, BaseResponse.class, param, "POST", true);
					
			httpRequest.addHead("mobileNo", "");
			httpRequest.addHead("mobiledeviceId", "");
			httpRequest.addHead("accesstoken", "");
			HttpExecute.getInstance().addRequest(httpRequest);

		}
}
