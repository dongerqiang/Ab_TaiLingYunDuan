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
import com.ananda.tailing.bike.entity.LookForPwdInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.RegExpValidator;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 找回密码
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:34:00
 */
public class LookForPwdActivity extends BaseActivity implements OnClickListener {

	private TitleBarView mTitleBarView;
	private ImageView ivAuto, ivPhone;	
	private LinearLayout autoLayout, phoneLayout;
	
	private LinearLayout[] layoutView = new LinearLayout[3];
	private EditText[] editView = new EditText[3];
	private ImageView ivCard, ivMobile, ivName;
	
	private MyDialog myDialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.lookfor_passwd_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("找回密码");
		
		myDialog = new MyDialog(this, handler);
		
	}
	
	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		ivAuto = (ImageView) findViewById(R.id.imageview_auto);
		ivPhone = (ImageView) findViewById(R.id.imageview_phone);		
		ivAuto.setSelected(true);
		autoLayout = (LinearLayout) findViewById(R.id.auto_linearlayout);
		phoneLayout = (LinearLayout) findViewById(R.id.phone_linearlayout);
		autoLayout.setVisibility(View.VISIBLE);
		
		layoutView[0] = (LinearLayout) findViewById(R.id.card_linearlayout);
		layoutView[1] = (LinearLayout) findViewById(R.id.mobile_linearlayout);
		layoutView[2] = (LinearLayout) findViewById(R.id.name_linearlayout);
		editView[0] = (EditText) findViewById(R.id.card_edittext);
		editView[1] = (EditText) findViewById(R.id.mobile_edittext);
		editView[2] = (EditText) findViewById(R.id.name_edittext);
		ivCard = (ImageView) findViewById(R.id.card_imageview);
		ivMobile = (ImageView) findViewById(R.id.mobile_imageview);
		ivName = (ImageView) findViewById(R.id.name_imageview);
		
		editView[0].addTextChangedListener(new EditTextWatcher(editView[0]));
		editView[1].addTextChangedListener(new EditTextWatcher(editView[1]));
		editView[2].addTextChangedListener(new EditTextWatcher(editView[2]));
		for(EditText edit : editView) {
			edit.setOnFocusChangeListener(new MyFocusListen(true));
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.imageview_auto: {
			showEnableFocus();
			ivAuto.setSelected(true);
			ivPhone.setSelected(false);
			autoLayout.setVisibility(View.VISIBLE);
			phoneLayout.setVisibility(View.GONE);
			break;
		}
		case R.id.imageview_phone: {
			hideEnableFocus();
			ivPhone.setSelected(true);
			ivAuto.setSelected(false);
			phoneLayout.setVisibility(View.VISIBLE);
			autoLayout.setVisibility(View.GONE);
			break;
		}
		case R.id.submit_button: {
			if(TextUtils.isEmpty(editView[0].getText().toString().trim())) {
				MyToast.showShortToast(LookForPwdActivity.this, "请输入身份证号码!");
				ivCard.setVisibility(View.VISIBLE);
				ivCard.setImageResource(R.drawable.laments_icon);
				return;
			} 
			
			if(checkLoginName()) {
				ivName.setVisibility(View.VISIBLE);
				ivName.setImageResource(R.drawable.hook_icon);
				if (CommonUtils.isNetWorkNormal(LookForPwdActivity.this)) { 
					submitLookForPwd();
				} else {
					MyToast.showShortToast(LookForPwdActivity.this, "网络不可用, 请先打开网络!");
				}					
			}
			break;
		}
		}
	}
	
	/**
	 * 提交找回密码
	 */
	private void submitLookForPwd() {
		showProgressDialog(LookForPwdActivity.this, "请稍后..."); 
		try {
			LookForPwdInfo lookForPwdInfo = new LookForPwdInfo();
			lookForPwdInfo.setIdintity(editView[0].getText().toString().trim());
			lookForPwdInfo.setMobile(editView[1].getText().toString().trim());
			lookForPwdInfo.setName(editView[2].getText().toString().trim());
			lookForPwdInfo.setTimestamp(CommonUtils.getTimestamp());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.LOOKFORPWD);
			jsonObject.put("Info", JSONObject.parseObject(JSONObject.toJSONString(lookForPwdInfo)));
			System.err.println("密码找回提交报文: -->" + jsonObject.toJSONString()); 
			StringEntity stringEntity = new StringEntity(jsonObject.toJSONString());
			
			HttpRestClient.post(LookForPwdActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog();
					MyToast.showShortToast(LookForPwdActivity.this, HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					System.err.println("密码找回返回报文: -->" + content);
					JSONObject jsonObject =JSONObject.parseObject(content); 
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("030")) {								
						String message = jsonInfo.getString("Message");
						myDialog.show();
						myDialog.setTextInfo(message);
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(LookForPwdActivity.this, LoginActivity.class);
								intent.putExtra("first", "1");
								startActivity(intent);
								LookForPwdActivity.this.finish();
							}
						}, 1000);
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
	 * 隐藏光标
	 */
	private void hideEnableFocus() {		
		for(LinearLayout layout: layoutView) {
			layout.setFocusable(true);
			layout.setFocusableInTouchMode(true);			
		}
		for(EditText edit : editView) {
			edit.setOnFocusChangeListener(new MyFocusListen(false));
		}
	}
	
	/**
	 * 显示光标
	 */
	private void showEnableFocus() {
		for(LinearLayout layout: layoutView) {
			layout.setFocusable(false);
			layout.setFocusableInTouchMode(false);
		}
		for(EditText edit : editView) {
			edit.setOnFocusChangeListener(new MyFocusListen(true));
		}
		
	}
	
	/**
	 * EditText文本状态改变处理
	 * @package com.ananda.tailing.bike.activity.more
	 * @description: 
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-2-26 上午10:20:49
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
				layoutView[0].setSelected(true);
			} else if(et.getTag().equals("2")) {
				layoutView[1].setSelected(true);
			} else {
				layoutView[2].setSelected(true);
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
		
		private boolean isFocus;
		
		public MyFocusListen(boolean isFocus) {
			this.isFocus = isFocus;
		}

		/* (non-Javadoc)
		 * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
		 */
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(isFocus)
				if(v == editView[0]) {
					// 检查身份证号码
					if(!hasFocus) {
						checkIDCard();
					}
				} else if(v == editView[1]) {
					// 检查手机号码
					if(!hasFocus) {
						checkMobilePhone();
					}
				}
		}
		
	}
	
	/**
	 * 检查身份证号码
	 */
	private void checkIDCard() {
		if(!RegExpValidator.IsIDcard(editView[0].getText().toString().trim())) {
			MyToast.showShortToast(LookForPwdActivity.this, "请输入正确的身份证号码!");
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
		if(!RegExpValidator.IsHandset(editView[1].getText().toString().trim())) {
			MyToast.showShortToast(LookForPwdActivity.this, "请输入正确的手机号码!");
			ivMobile.setVisibility(View.VISIBLE);
			ivMobile.setImageResource(R.drawable.laments_icon);
		} else {
			ivMobile.setVisibility(View.VISIBLE);
			ivMobile.setImageResource(R.drawable.hook_icon);
		}
	}
	
	/**
	 * 检查用户名
	 */
	private boolean checkLoginName() {
		if(TextUtils.isEmpty(editView[2].getText().toString().trim())) {
			MyToast.showShortToast(LookForPwdActivity.this, "请输入姓名!");
			ivName.setVisibility(View.VISIBLE);
			ivName.setImageResource(R.drawable.laments_icon);
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
