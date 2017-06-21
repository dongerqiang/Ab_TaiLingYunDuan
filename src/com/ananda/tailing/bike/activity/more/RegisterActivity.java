package com.ananda.tailing.bike.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.util.RegExpValidator;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 注册
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:23:26
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	private TitleBarView mTitleBarView;

	private LinearLayout layoutName, layoutCard, layoutMobile, layoutLoginPwd,
			layoutRepeatPwd;
	private EditText etName, etCard, etMobile, etLoginPwd, etRepeatPwd;
	private ImageView ivName, ivCard, ivMobile, ivLoginPwd, ivRepeatPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.register_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);

		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("注册");

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
		layoutRepeatPwd = (LinearLayout) findViewById(R.id.repeat_pwd_linearlayout);
		etName = (EditText) findViewById(R.id.name_edittext);
		etCard = (EditText) findViewById(R.id.card_edittext);
		etMobile = (EditText) findViewById(R.id.mobile_edittext);
		etLoginPwd = (EditText) findViewById(R.id.login_pwd_edittext);
		etRepeatPwd = (EditText) findViewById(R.id.repeat_pwd_edittext);
		ivName = (ImageView) findViewById(R.id.name_imageview);
		ivCard = (ImageView) findViewById(R.id.card_imageview);
		ivMobile = (ImageView) findViewById(R.id.mobile_imageview);
		ivLoginPwd = (ImageView) findViewById(R.id.login_pwd_imageview);
		ivRepeatPwd = (ImageView) findViewById(R.id.repeat_pwd_imageview);

		etName.addTextChangedListener(new EditTextWatcher(etName));
		etCard.addTextChangedListener(new EditTextWatcher(etCard));
		etMobile.addTextChangedListener(new EditTextWatcher(etMobile));
		etLoginPwd.addTextChangedListener(new EditTextWatcher(etLoginPwd));
		etRepeatPwd.addTextChangedListener(new EditTextWatcher(etRepeatPwd));
		
		etName.setOnFocusChangeListener(new MyFocusListen());
		etCard.setOnFocusChangeListener(new MyFocusListen());
		etMobile.setOnFocusChangeListener(new MyFocusListen());
		etLoginPwd.setOnFocusChangeListener(new MyFocusListen());
		etRepeatPwd.setOnFocusChangeListener(new MyFocusListen());
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.register_button) {
			if(TextUtils.isEmpty(etName.getText().toString().trim())) {
				MyToast.showShortToast(RegisterActivity.this, "请输入姓名!");
				ivName.setVisibility(View.VISIBLE);
				ivName.setImageResource(R.drawable.laments_icon);				
			} else if(checkRepeatPwd()) {
				ivRepeatPwd.setVisibility(View.VISIBLE);
				ivRepeatPwd.setImageResource(R.drawable.hook_icon);
				
				// 保存用户注册的信息
				PreferencesUtils.putString(this, "Name", etName.getText().toString().trim());
				PreferencesUtils.putString(this, "Idintity", etCard.getText().toString().trim());
				PreferencesUtils.putString(this, "Mobile", etMobile.getText().toString().trim());
				PreferencesUtils.putString(this, "Password", etLoginPwd.getText().toString().trim());
				
				Intent intent = new Intent(this, BluetoothPwdActivity.class);
				startActivity(intent);
				this.finish();
			}
		}
	}

	/**
	 * EditText文本状态改变处理
	 * 
	 * @package com.ananda.tailing.bike.activity.more
	 * @description:
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-2-27 下午1:43:53
	 */
	class EditTextWatcher implements TextWatcher {

		private EditText et;

		public EditTextWatcher(EditText edit) {
			this.et = edit;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		 */
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
		 * int, int, int)
		 */
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if (et.getTag().equals("1")) {
				layoutName.setSelected(true);
			} else if (et.getTag().equals("2")) {
				layoutCard.setSelected(true);
			} else if (et.getTag().equals("3")) {
				layoutMobile.setSelected(true);
			} else if (et.getTag().equals("4")) {
				layoutLoginPwd.setSelected(true);
			} else {
				layoutRepeatPwd.setSelected(true);
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence,
		 * int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
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
			} else if(v == etLoginPwd) {
				// 检查登录密码
				if(!hasFocus) {
					checkLoginPwd();
				}
			}
		}
		
	}
	
	/**
	 * 检查用户名
	 */
	private void checkLoginName() {
		if(TextUtils.isEmpty(etName.getText().toString().trim())) {
			MyToast.showShortToast(RegisterActivity.this, "请输入姓名!");
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
			MyToast.showShortToast(RegisterActivity.this, "请输入正确的身份证号码!");
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
			MyToast.showShortToast(RegisterActivity.this, "请输入正确的手机号码!");
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
	private void checkLoginPwd() {
		if(!RegExpValidator.IsPassword(etLoginPwd.getText().toString().trim())) {
			MyToast.showShortToast(RegisterActivity.this, "请输入正确的6位数密码(含字母)!");
			ivLoginPwd.setVisibility(View.VISIBLE);
			ivLoginPwd.setImageResource(R.drawable.laments_icon);
		} else {
			ivLoginPwd.setVisibility(View.VISIBLE);
			ivLoginPwd.setImageResource(R.drawable.hook_icon);
		}
	}
	
	/**
	 * 检查重复密码
	 */
	private boolean checkRepeatPwd() {
		if(!etLoginPwd.getText().toString().trim()
				.equals(etRepeatPwd.getText().toString().trim())) {
			MyToast.showShortToast(RegisterActivity.this, "两次密码输入不一致!");
			ivRepeatPwd.setVisibility(View.VISIBLE);
			ivRepeatPwd.setImageResource(R.drawable.laments_icon);
			return false;
		} 
		return true;
	}
	
}
