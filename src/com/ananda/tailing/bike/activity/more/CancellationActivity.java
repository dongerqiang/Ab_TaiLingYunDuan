package com.ananda.tailing.bike.activity.more;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.util.RegExpValidator;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 注销
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:42:23
 */
public class CancellationActivity extends BaseActivity implements OnClickListener {

	private TitleBarView mTitleBarView;
	
	private LinearLayout layoutName, layoutCard, layoutMobile, layoutLoginPwd;
	private EditText etName, etCard, etMobile, etLoginPwd;
	private ImageView ivName, ivCard, ivMobile, ivLoginPwd;
	
	private MyDialog myDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.cancellation_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		myDialog = new MyDialog(this, handler);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("注销用户");
	
	}
	
	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		layoutName = (LinearLayout) findViewById(R.id.name_linearlayout);
		layoutCard = (LinearLayout) findViewById(R.id.card_linearlayout);
		layoutMobile = (LinearLayout) findViewById(R.id.mobile_linearlayout);
		layoutLoginPwd = (LinearLayout) findViewById(R.id.login_pwd_linearlayout);
		etName = (EditText) findViewById(R.id.name_edittext);
		etCard = (EditText) findViewById(R.id.card_edittext);
		etMobile = (EditText) findViewById(R.id.mobile_edittext);
		etLoginPwd = (EditText) findViewById(R.id.login_pwd_edittext);
		ivName = (ImageView) findViewById(R.id.name_imageview);
		ivCard = (ImageView) findViewById(R.id.card_imageview);
		ivMobile = (ImageView) findViewById(R.id.mobile_imageview);
		ivLoginPwd = (ImageView) findViewById(R.id.login_pwd_imageview);
		
		etName.addTextChangedListener(new EditTextWatcher(etName));
		etCard.addTextChangedListener(new EditTextWatcher(etCard));
		etMobile.addTextChangedListener(new EditTextWatcher(etMobile));
		etLoginPwd.addTextChangedListener(new EditTextWatcher(etLoginPwd));
		etName.setOnFocusChangeListener(new MyFocusListen());
		etCard.setOnFocusChangeListener(new MyFocusListen());
		etMobile.setOnFocusChangeListener(new MyFocusListen());
		etLoginPwd.setOnFocusChangeListener(new MyFocusListen());
		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.confrim_cancell_button) {
			if(TextUtils.isEmpty(etName.getText().toString().trim())) {
				MyToast.showShortToast(this, "请输入姓名!");
				ivName.setVisibility(View.VISIBLE);
				ivName.setImageResource(R.drawable.laments_icon);
				return;
			}
			
			if(checkLoginPwd()) {
				ivLoginPwd.setVisibility(View.VISIBLE);
				ivLoginPwd.setImageResource(R.drawable.hook_icon);
				
				// 注销用户
				PreferencesUtils.getClearSharePreVlaue(this);
				myDialog.show();
				myDialog.setTextInfo("注销用户成功!");
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (CommonUtils.isNetWorkNormal(CancellationActivity.this)) { 
							getLoginOutWork();
						} else {
							MyToast.showShortToast(CancellationActivity.this, "网络不可用, 请先打开网络!");
						}						
					}
				}, 1000);
				
			}
		}
	}
	
	/**
	 * 注销请求网络
	 */
	private void getLoginOutWork() {
		showProgressDialog(CancellationActivity.this, "请稍后...");
		
		try {
			JSONObject jsonLogin = new JSONObject();
			jsonLogin.put("Name", etName.getText().toString().trim());
			jsonLogin.put("Password", etLoginPwd.getText().toString().trim());
			jsonLogin.put("Identity", etCard.getText().toString().trim());
			jsonLogin.put("Mobile", etMobile.getText().toString().trim());
			jsonLogin.put("Timestamp", new String(CommonUtils.getTimestamp()));
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.LOGOUT);
			jsonObject.put("Info",
					JSONObject.parseObject(jsonLogin.toString())); 
			
			System.err.println("请求： -->" + jsonObject.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			
			HttpRestClient.post(CancellationActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler() {
				
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog();
					MyToast.showShortToast(CancellationActivity.this,
							HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					System.err.println("注销成功返回： -->" + content); 
					JSONObject jsonObject = JSONObject.parseObject(content);
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("090")) {			
						PreferencesUtils.putBoolean(CancellationActivity.this, "isChecked", false); 
						PreferencesUtils.putString(CancellationActivity.this, "LoginName", "");
						PreferencesUtils.putString(CancellationActivity.this, "Password", "");
						String message = jsonInfo.getString("Message");
						myDialog.show();
						myDialog.setTextInfo(message);
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(CancellationActivity.this, LoginActivity.class);
								intent.putExtra("first", "1");
								startActivity(intent);
								CancellationActivity.this.finish();
							}
						}, 1000);	
											
					} else {
						String message = jsonInfo.getString("Message");
						myDialog.show();
						myDialog.setTextInfo(message);
					}	
				}

			});
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
	}
	
	/**
	 * EditText文本状态改变处理
	 * @package com.ananda.tailing.bike.activity.more
	 * @description: 
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-2-26 上午10:36:51
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
			if(et.getTag().equals("1")) {
				layoutName.setSelected(true);
			} else if(et.getTag().equals("2")) {
				layoutCard.setSelected(true);
			} else if(et.getTag().equals("3")) {
				layoutMobile.setSelected(true);
			} else {
				layoutLoginPwd.setSelected(true);
			}
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
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
	
	/**
	 * 失去光标后, EditText显示状态提示
	 * @package com.ananda.tailing.bike.activity.more
	 * @description: 
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-2-28 下午4:59:51
	 */
	class MyFocusListen implements OnFocusChangeListener {

		/* (non-Javadoc)
		 * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
		 */
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			 if(v == etName) {
				// 检查用户名
				if(!hasFocus) {
					checkLoginName();
				}
			} else if(v == etCard) {
				// 检查身份证号码
				if(!hasFocus) {
					checkIDCard();
				}				
			} else if(v == etMobile) {
				// 检查手机号码
				if(!hasFocus) {
					checkMobilePhone();
				} 
			}
		}
		
	}
	
	/**
	 * 检查用户名
	 */
	private void checkLoginName() {
		if(TextUtils.isEmpty(etName.getText().toString().trim())) {
			MyToast.showShortToast(CancellationActivity.this, "请输入姓名!");
			ivName.setVisibility(View.VISIBLE);
			ivName.setImageResource(R.drawable.laments_icon);
		} else {
			ivName.setVisibility(View.VISIBLE);
			ivName.setImageResource(R.drawable.hook_icon);
		}
	}
	
	/**
	 * 检查身份证号
	 */
	private void checkIDCard() {
		if(!RegExpValidator.IsIDcard(etCard.getText().toString().trim())) {
			MyToast.showShortToast(CancellationActivity.this, "请输入正确的身份证号码!");
			ivCard.setVisibility(View.VISIBLE);
			ivCard.setImageResource(R.drawable.laments_icon);
		} else {
			ivCard.setVisibility(View.VISIBLE);
			ivCard.setImageResource(R.drawable.hook_icon);
		}
	}

	/**
	 * 检查手机号码
	 */
	private void checkMobilePhone() {
		if(!RegExpValidator.IsHandset(etMobile.getText().toString().trim())) {
			MyToast.showShortToast(CancellationActivity.this, "请输入正确的手机号码!");
			ivMobile.setVisibility(View.VISIBLE);
			ivMobile.setImageResource(R.drawable.laments_icon);
		} else {
			ivMobile.setVisibility(View.VISIBLE);
			ivMobile.setImageResource(R.drawable.hook_icon);
		}
	}
	
	/**
	 * 检查登陆密码 
	 */
	private boolean checkLoginPwd() {
		if(!RegExpValidator.IsPassword(etLoginPwd.getText().toString().trim())) {
			MyToast.showShortToast(CancellationActivity.this, "请输入正确的6位数密码(含字母)!");
			ivLoginPwd.setVisibility(View.VISIBLE);
			ivLoginPwd.setImageResource(R.drawable.laments_icon);
			return false;
		} 
		return true;
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
