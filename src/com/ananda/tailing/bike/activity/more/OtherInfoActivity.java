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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.activity.RomtorActivity;
import com.ananda.tailing.bike.entity.RegisterInfo;
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
 * @description: 其他信息
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午1:09:29
 */
public class OtherInfoActivity extends BaseActivity implements OnClickListener {

	private TitleBarView mTitleBarView;

	static final int DATE_DIALOG_ID = 0;
	// 记录年，月，日
	private int year;
	private int month;
	private int day;

	private EditText etBirth;
	private EditText etJob;
	private EditText etUse;
	private RadioGroup rgSex;
	private RadioButton rbMale;
	private RadioButton rbFeMale;
	private String sex;

	private MyDialog myDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.other_info_layout);
		super.onCreate(savedInstanceState);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("其他信息填写");
		myDialog = new MyDialog(this, handler);
		
	}

	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		etBirth = (EditText) findViewById(R.id.birth_edittext);
		etJob = (EditText) findViewById(R.id.job_edittext);
		etUse = (EditText) findViewById(R.id.main_use_edittext);
		rgSex = (RadioGroup) findViewById(R.id.sex_radiogroup);
		rbMale = (RadioButton) findViewById(R.id.male_radiobutton);
		rbFeMale = (RadioButton) findViewById(R.id.female_radiobutton);

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
				RadioButton rb = (RadioButton) OtherInfoActivity.this
						.findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				sex = rb.getTag().toString();
			}
		});

	}

	/**
	 * 检查用户输入的内容
	 */
	private void checkInputByEditText(int type) {
		if(type == 0) {
			if (!rbMale.isChecked() && !rbFeMale.isChecked()) {
				MyToast.showShortToast(OtherInfoActivity.this, "请选择性别!");
				return;
			}
	
			if (TextUtils.isEmpty(etJob.getText().toString().trim())) {
				MyToast.showShortToast(OtherInfoActivity.this, "请输入职业!");
				return;
			}
	
			if (TextUtils.isEmpty(etUse.getText().toString().trim())) {
				MyToast.showShortToast(OtherInfoActivity.this, "请输入用途!");
				return;
			}
		}

		if (CommonUtils.isNetWorkNormal(OtherInfoActivity.this)) { 
			submitRegister(type);
		} else {
			MyToast.showShortToast(OtherInfoActivity.this, "网络不可用, 请先打开网络!");
		}	
	}
	
	/**
	 * 提交注册数据
	 */
	private void submitRegister(int type) {
		showProgressDialog(OtherInfoActivity.this, "请稍后...");
		// 提交请求数据
		try {			
			RegisterInfo register = new RegisterInfo();
			register.setNmae(PreferencesUtils.getString(OtherInfoActivity.this, "Name"));
			register.setIdintity(PreferencesUtils.getString(OtherInfoActivity.this, "Idintity"));
			register.setMobile(PreferencesUtils.getString(OtherInfoActivity.this, "Mobile"));
			register.setPassword(PreferencesUtils.getString(OtherInfoActivity.this, "Password"));
			if(type == 0) {
				register.setSex(sex);
				register.setBirthday(etBirth.getText().toString().trim());
				register.setJob(etJob.getText().toString().trim());
				register.setUse(etUse.getText().toString().trim());
			} else {
				register.setSex("");
				register.setBirthday("");
				register.setJob("");
				register.setUse("");
			}
			register.setTimestamp(CommonUtils.getTimestamp());
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.REGISTER);
			jsonObject.put("Info",
					JSONObject.parseObject(JSONObject.toJSONString(register)));

			System.err.println("请求： -->" + jsonObject.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			
			HttpRestClient.post(OtherInfoActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler() {
				 
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog();
					MyToast.showShortToast(OtherInfoActivity.this,
							HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					System.err.println("注册成功返回： -->" + content); 
					JSONObject jsonObject = JSONObject.parseObject(content);
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("010")) {								
						String userId = jsonInfo.getString("UserId");
						// 保存用户唯一ID
						PreferencesUtils.putString(OtherInfoActivity.this, "UserId", userId);
						myDialog.show();
						myDialog.setTextInfo("注册成功");
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								startActivity(new Intent(OtherInfoActivity.this, RomtorActivity.class));
								OtherInfoActivity.this.finish();
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

	private void setMakeDateEditText() {
		// 隐藏EditText键盘
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
			return new DatePickerDialog(this, mSetDateListener, year, month,
					day);
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
		if (v.getId() == R.id.submit_button) {
			checkInputByEditText(0);
		} else if (v.getId() == R.id.skip_button) {
			checkInputByEditText(1);
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
