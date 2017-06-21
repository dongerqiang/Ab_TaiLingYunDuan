package com.ananda.tailing.bike.activity.more;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.http.entity.StringEntity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.entity.EditUserInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.MyDialog;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 编辑用户信息
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-24 下午5:00:20
 */
public class EditUserInfoActivity extends BaseActivity implements OnClickListener {

	private TitleBarView mTitleBarView;

	static final int DATE_DIALOG_ID = 0;
	// 记录年，月，日
	private int year;
	private int month;
	private int day;
	
	private EditText etName;
	private EditText etBirth;
	private EditText etJob;
	private EditText etUse;
	private EditText etIDCard;
	private EditText etMobile;
	private RadioGroup rgSex;
	private RadioButton rbMale;
	private RadioButton rbFeMale;
	private String sex;
	
	private LinearLayout layoutCard;
	private ImageView ivDrop;
	
	private MyDialog myDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.edit_user_info);
		super.onCreate(savedInstanceState);

		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("编辑用户");
		
		myDialog = new MyDialog(this, handler);
		
	}

	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		etName = (EditText) findViewById(R.id.name_edittext);
		etBirth = (EditText) findViewById(R.id.birth_edittext);
		etJob = (EditText) findViewById(R.id.job_edittext);
		etUse = (EditText) findViewById(R.id.main_use_edittext);
		etIDCard = (EditText) findViewById(R.id.id_card_edittext);
		etMobile = (EditText) findViewById(R.id.contact_phone_edittext);
		rgSex = (RadioGroup) findViewById(R.id.sex_radiogroup);		
		rbMale = (RadioButton) findViewById(R.id.male_radiobutton);
		rbFeMale = (RadioButton) findViewById(R.id.female_radiobutton);
		
		layoutCard = (LinearLayout) findViewById(R.id.more_mes_linearlayout);
		ivDrop = (ImageView) findViewById(R.id.drop_imageview);
		
		etName.setText(PreferencesUtils.getString(EditUserInfoActivity.this, "Name", "")); 
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		// 更新现有时间
		updateDisplay();
		setMakeDateEditText();
		
		rgSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) EditUserInfoActivity.this
						.findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				sex = rb.getTag().toString();
			}
		});
		
	}
		
	private void setMakeDateEditText() {
		//隐藏EditText键盘
		etBirth.setInputType(InputType.TYPE_NULL);
		etBirth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
	}
	
	/**
	 * 当显示时间窗口被创建时调用
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mSetDateListener, year,
						month, day);
		}
		return null;
	}
	
	/*
	 * 定义时间显示监听器
	 */
	private DatePickerDialog.OnDateSetListener mSetDateListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int myear, int monthOfYear,
				int dayOfMonth) {
			// 进行时间显示的更新
			year = myear;
			month = monthOfYear;
			day = dayOfMonth;
			// 更新时间的显示
			updateDisplay();
		}
	};
	
	/**
	 * 更新时间显示器
	 */
	private void updateDisplay() {
		String strMonth = (month + 1) + "";
		if (strMonth.length() < 2) {
			strMonth = "0" + strMonth;
		}
		
		String strDay = day + "";
		if (strDay.length() < 2) {
			strDay = "0" + strDay;
		}
		
		etBirth.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(year).append("-").append(strMonth).append("-")
				.append(strDay).append(" "));
	}
	

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.user_edit_button) {
			checkInputUserInfo();
		} else if(v.getId() == R.id.drop_imageview) {
			layoutCard.setVisibility(View.VISIBLE);
			ivDrop.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 检查用户输入的信息
	 */
	private void checkInputUserInfo() {
		if (!rbMale.isChecked() && !rbFeMale.isChecked()) {
			MyToast.showShortToast(EditUserInfoActivity.this, "请选择性别!");
			return;
		}
		
		if(TextUtils.isEmpty(etJob.getText().toString().trim())) {
			MyToast.showShortToast(EditUserInfoActivity.this, "请输入职业!");
			return;
		}
		
		if(TextUtils.isEmpty(etUse.getText().toString().trim())) {
			MyToast.showShortToast(EditUserInfoActivity.this, "请输入用途!");
			return;
		}
		
		if(TextUtils.isEmpty(etIDCard.getText().toString().trim())) {
			MyToast.showShortToast(EditUserInfoActivity.this, "请输入身份证号码!");
			return;	
		}
		
		if(TextUtils.isEmpty(etMobile.getText().toString().trim())) {
			MyToast.showShortToast(EditUserInfoActivity.this, "请输入联系电话!");
			return;
		}
		
		if (CommonUtils.isNetWorkNormal(EditUserInfoActivity.this)) { 
			submitEditUserInfo();
		} else {
			MyToast.showShortToast(EditUserInfoActivity.this, "网络不可用, 请先打开网络!");
		}
		
	}

	/**
	 * 提交用户编辑信息
	 */
	private void submitEditUserInfo() {		
		showProgressDialog(EditUserInfoActivity.this, "请稍后...");
		try {
			EditUserInfo editUserInfo = new EditUserInfo();
			editUserInfo.setUserId(PreferencesUtils.getString(EditUserInfoActivity.this, "UserId", ""));
			editUserInfo.setSex(sex);
			editUserInfo.setBirthday(etBirth.getText().toString().trim());
			editUserInfo.setJob(etJob.getText().toString().trim());
			editUserInfo.setUse(etUse.getText().toString().trim());
			editUserInfo.setIdintity(etIDCard.getText().toString().trim());
			editUserInfo.setMobile(etMobile.getText().toString().trim());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.EDITUSERINFO);
			jsonObject.put("Info", JSONObject.parseObject(JSONObject.toJSONString(editUserInfo)));
			
			System.err.println("编辑用户提交报文: -->" + jsonObject); 
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			
			HttpRestClient.post(EditUserInfoActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){

				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog(); 
					MyToast.showShortToast(EditUserInfoActivity.this, HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					System.err.println("编辑用户信息返回报文: -->" + content); 
					JSONObject jsonObject =JSONObject.parseObject(content); 
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("050")) {								
						String message = jsonInfo.getString("Message");
						myDialog.show();
						myDialog.setTextInfo(message);
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(EditUserInfoActivity.this, LoginActivity.class);
								intent.putExtra("first", "1");
								startActivity(intent);
								EditUserInfoActivity.this.finish();
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
