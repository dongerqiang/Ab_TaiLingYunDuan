package com.ananda.tailing.bike.activity.more;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.activity.DeviceListActivity;
import com.ananda.tailing.bike.bluetooth.BlueToothDirective;
import com.ananda.tailing.bike.bluetooth.BlueToothMsg;
import com.ananda.tailing.bike.bluetooth.BluetoothChatService;
import com.ananda.tailing.bike.entity.AmendPwdInfo;
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
 * @description: 修改密码
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:30:12
 */
public class AmendPwdActivity extends BaseActivity implements OnClickListener{

	private TitleBarView mTitleBarView;
	
	private LinearLayout layoutFormer, layoutNew, layoutRepeat;
	private EditText etFormorPwd, etNewPwd, etRepeatPwd;
	private ImageView ivFormorPwd, ivNewPwd, ivRepeatPwd;
	
	/** 蓝牙连接操作 */
	private BluetoothChatService mChatService = null;
	private BluetoothAdapter btAdapt = null;
	public static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_ENABLE2_BT = 3;
	private String mConnectedDeviceName;
	private String pAddress = null;
	/** 接收蓝牙设备返回的结果 */
	private String successMes = null;
	
	private MyDialog myDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.amend_passwd_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("修改密码");
		myDialog = new MyDialog(this, handler);
		
		btAdapt = BluetoothAdapter.getDefaultAdapter();
		if (!btAdapt.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE2_BT);
		} else {
			pAddress = PreferencesUtils.getString(this, "address");
			if(!TextUtils.isEmpty(pAddress)) {
				// 把蓝牙设备对象
				BluetoothDevice device = btAdapt.getRemoteDevice(pAddress);
				mChatService = new BluetoothChatService(this, mHandler);
				// 试图连接到装置
				mChatService.connect(device);
			} else {
				startBluetooth();
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		layoutFormer = (LinearLayout) findViewById(R.id.former_pwd_linearlayout);
		layoutNew = (LinearLayout) findViewById(R.id.new_pwd_linearlayout);
		layoutRepeat = (LinearLayout) findViewById(R.id.repeat_pwd_linearlayout);
		etFormorPwd = (EditText) findViewById(R.id.former_pwd_edittext);
		etNewPwd = (EditText) findViewById(R.id.new_pwd_edittext);
		etRepeatPwd = (EditText) findViewById(R.id.repeat_pwd_edittext);
		ivFormorPwd = (ImageView) findViewById(R.id.formor_pwd_imageview);
		ivNewPwd = (ImageView) findViewById(R.id.new_pwd_imageview);
		ivRepeatPwd = (ImageView) findViewById(R.id.repeat_pwd_imageview);
		
		etFormorPwd.addTextChangedListener(new EditTextWatcher(etFormorPwd));
		etNewPwd.addTextChangedListener(new EditTextWatcher(etNewPwd));
		etRepeatPwd.addTextChangedListener(new EditTextWatcher(etRepeatPwd));
		
		etFormorPwd.setOnFocusChangeListener(new MyFocusListen());
		etNewPwd.setOnFocusChangeListener(new MyFocusListen());
		etRepeatPwd.setOnFocusChangeListener(new MyFocusListen());
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.submit_button) {
			if(TextUtils.isEmpty(etFormorPwd.getText().toString().trim())) {
				MyToast.showShortToast(this, "请输入原密码!");
				ivFormorPwd.setVisibility(View.VISIBLE);
				ivFormorPwd.setImageResource(R.drawable.laments_icon);
				return;
			}
			if(checkRepeatPwd()) {
				ivRepeatPwd.setVisibility(View.VISIBLE);
				ivRepeatPwd.setImageResource(R.drawable.hook_icon);
				if (CommonUtils.isNetWorkNormal(this)) {
					// 修改控制器密码
					// 原密码
					String formorPwd = etFormorPwd.getText().toString().trim();
					// 新密码
					String newPwd = etNewPwd.getText().toString().trim();
					
					// 获取原密码的3位
					int fPwd1 = CommonUtils.getSubPwd1(AmendPwdActivity.this, formorPwd);
					int fPwd2 = CommonUtils.getSubPwd2(AmendPwdActivity.this, formorPwd);
					int fPwd3 = CommonUtils.getSubPwd3(AmendPwdActivity.this, formorPwd);
					
					// 获取新密码的3位
					int nPwd1 = CommonUtils.getSubPwd1(AmendPwdActivity.this, newPwd);
					int nPwd2 = CommonUtils.getSubPwd2(AmendPwdActivity.this, newPwd);
					int nPwd3 = CommonUtils.getSubPwd3(AmendPwdActivity.this, newPwd);
										
					// 前面3位是原密码, 后面3位的新密码
					sendBluetoothMessage(BlueToothDirective.sentSettingPwd(fPwd1, fPwd2, fPwd3, nPwd1, nPwd2, nPwd3));
					
				} else {
					MyToast.showShortToast(this, "网络不可用, 请先打开网络!");
				}				
			}
		}
	}
	 
	/**
	 * 提交修改密码 
	 */
	private void submitAmendPwd() {		 
		showProgressDialog(AmendPwdActivity.this, "请稍后...");
		try {
			AmendPwdInfo amendPwdInfo = new AmendPwdInfo();
			amendPwdInfo.setUserId(PreferencesUtils.getString(AmendPwdActivity.this, "UserId", ""));
			amendPwdInfo.setPassword(etFormorPwd.getText().toString().trim());
			amendPwdInfo.setNewPassword(etNewPwd.getText().toString().trim());
			amendPwdInfo.setTimestamp(String.valueOf(new Date().getTime()));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Result", HttpAPI.AMENDPWD);
			jsonObject.put("Info", JSONObject.parseObject(JSONObject.toJSONString(amendPwdInfo)));
			System.err.println("请求： -->" + jsonObject.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			
			HttpRestClient.post(AmendPwdActivity.this, HttpAPI.USER_URL, stringEntity, new AsyncHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					dismissProgressDialog(); 
					MyToast.showShortToast(AmendPwdActivity.this, HttpAPI.RESPONSE);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					dismissProgressDialog();
					System.err.println("注册成功返回： -->" + content);
					JSONObject jsonObject =JSONObject.parseObject(content);
					String result = jsonObject.getString("Result");
					String info = jsonObject.getString("Info");
					JSONObject jsonInfo = JSONObject.parseObject(info);
					if(result.equals("020")) {								
						String message = jsonInfo.getString("Message");
						myDialog.show();
						myDialog.setTextInfo(message);
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub 
								PreferencesUtils.putString(AmendPwdActivity.this, "Password", 
										etNewPwd.getText().toString().trim());
								Intent intent = new Intent(AmendPwdActivity.this, LoginActivity.class);
								intent.putExtra("first", "1");
								startActivity(intent);
								AmendPwdActivity.this.finish();								
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
	 * EditText文本改变状态处理
	 * @package com.ananda.tailing.bike.activity.more
	 * @description: 
	 * @version 1.0
	 * @author JackieCheng
	 * @email xiaming5368@163.com
	 * @date 2014-2-26 上午10:14:53
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
				layoutFormer.setSelected(true);
			} else if(et.getTag().equals("2")) {
				layoutNew.setSelected(true);
			} else {
				layoutRepeat.setSelected(true);
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

		/* (non-Javadoc)
		 * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
		 */
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(v == etFormorPwd) {
				// 检查原密码
				if(!hasFocus) {
					checkFormorPwd();
				}
			} else if(v == etNewPwd) {
				// 检查新密码
				if(!hasFocus) {
					checkNewPwd();
				}
			} 
		}
		
	}
	
	/**
	 * 检查原密码
	 */
	private void checkFormorPwd() {
		if(!RegExpValidator.IsPassword(etFormorPwd.getText().toString().trim())) {
			MyToast.showShortToast(AmendPwdActivity.this, "请输入正确的6位数密码(含字母)!");
			ivFormorPwd.setVisibility(View.VISIBLE);
			ivFormorPwd.setImageResource(R.drawable.laments_icon);
		} else {
			ivFormorPwd.setVisibility(View.VISIBLE);
			ivFormorPwd.setImageResource(R.drawable.hook_icon);
		}
	}
	
	/**
	 * 检查新密码
	 */
	private void checkNewPwd() {
		if(!RegExpValidator.IsPassword(etNewPwd.getText().toString().trim())) {
			MyToast.showShortToast(AmendPwdActivity.this, "请输入正确的6位数密码(含字母)!");
			ivNewPwd.setVisibility(View.VISIBLE);
			ivNewPwd.setImageResource(R.drawable.laments_icon);
		} else {
			ivNewPwd.setVisibility(View.VISIBLE);
			ivNewPwd.setImageResource(R.drawable.hook_icon);
		}
	}
	
	/**
	 * 检查重复密码
	 */
	private boolean checkRepeatPwd() {
		if(!etNewPwd.getText().toString().trim()
				.equals(etRepeatPwd.getText().toString().trim())) {
			MyToast.showShortToast(AmendPwdActivity.this, "两次密码输入不一致!");
			ivRepeatPwd.setVisibility(View.VISIBLE);
			ivRepeatPwd.setImageResource(R.drawable.laments_icon);
			return false;
		}
		return true;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 蓝牙聊天服务站
		if (mChatService != null) {
			mChatService.stop();
			mChatService = null;
		}
	}
	
	/**
	 * 检测是否打开蓝牙
	 */
	private void startBluetooth() {
		// 如果本程序启动时蓝牙没有打开，要求它启用。 setupChat()将被称为在onActivityResult
		if (!btAdapt.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (mChatService == null) {
				setupChat();
			} 
		}
	}
	
	/**
	 * 请求设备连接
	 */
	private void setupChat() {
		// 初始化BluetoothChatservice执行蓝牙连接
		Intent serverIntent = new Intent(AmendPwdActivity.this,
				DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}
	
	/**
	 * 发送一个消息
	 * 
	 * @param message
	 *            一个文本字符串发送.
	 */
	private void sendBluetoothMessage(String message) {
		// 检查我们实际上在任何连接
		if (mChatService != null) {
			mChatService.setHandler(mHandler);
			if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				Toast.makeText(AmendPwdActivity.this, R.string.not_connected,
						Toast.LENGTH_SHORT).show();
				return;
			}
	
			// 检查实际上有东西寄到
			if (message.length() > 0) {
				// 得到消息字节和告诉BluetoothChatservice写
				mChatService.write(CommonUtils.hexStr2Bytes(message));
			}
		} else {
			Toast.makeText(AmendPwdActivity.this, R.string.not_connected,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE: {
			// 当DeviceliStactivity返回连接装置
			if (resultCode == Activity.RESULT_OK) {
				mChatService = new BluetoothChatService(AmendPwdActivity.this, mHandler);
				// 获得设备地址
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// 把蓝牙设备对象
				BluetoothDevice device = btAdapt.getRemoteDevice(address);
				// 试图连接到装置
				mChatService.connect(device);
			}
			break;
		}
		case REQUEST_ENABLE_BT: {
			// 当请求启用蓝牙返回
			if (resultCode == Activity.RESULT_OK) { 
				System.out.println("蓝牙已启用!");
				// 蓝牙已启用，所以建立一个聊天会话
				setupChat();		
			} else {
				// 用户未启用蓝牙或发生错误
				Toast.makeText(AmendPwdActivity.this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case REQUEST_ENABLE2_BT:{
			pAddress = PreferencesUtils.getString(this, "address");
			if (!TextUtils.isEmpty(pAddress)) {
				System.out.println("已保存地址");  
				// 把蓝牙设备对象
				BluetoothDevice device = btAdapt.getRemoteDevice(pAddress);
				mChatService = new BluetoothChatService(this, mHandler);
				// 试图连接到装置
				mChatService.connect(device);
			}	
			break;
		}
		}
	}
	
	private final Handler mHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case BlueToothMsg.MESSAGE_STATE_CHANGE: {
				switch (msg.arg1) {
				// 设备已连接
				case BluetoothChatService.STATE_CONNECTED:
					myApplication.mBluetoothChatService = mChatService;
					System.out.println("设备已连接!"); 
					break;
				// 设备正在连接
				case BluetoothChatService.STATE_CONNECTING:
					System.out.println("正在连接...");
					break;
				// 设备未连接
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					System.out.println("设备未连接...");
					break;
				}
				break;
			}
			// 发送命令
			case BlueToothMsg.MESSAGE_WRITE: {
				byte[] writeBuf = (byte[]) msg.obj;
				System.out.println("发送命令: --"
						+ CommonUtils.Bytes2HexString(writeBuf));
				break;
			}
			// 接收命令
			case BlueToothMsg.MESSAGE_READ: {
				successMes = msg.obj.toString();
				System.err.println("接收的命令: -->" + successMes); 
				String result = CommonUtils.checkBluetoothReuslt(successMes);
				if (!TextUtils.isEmpty(result)) {
					// 开始字节
					String startBytes = result.substring(0, 2);					
					// 结束字节
					String endBytes = result.substring(10, 12);
					if (startBytes.equals("3A") && endBytes.equals("0D")) {	
						if(result.equalsIgnoreCase("3A797533210D")) {
							// 控制器密码设置成功!
							// 修改APP密码
							submitAmendPwd(); 
						}	
					}
				}
				break;
			}
			// 成功连接设备
			case BlueToothMsg.MESSAGE_DEVICE_NAME: {
				// 保存该连接装置的名字
				mConnectedDeviceName = msg.getData().getString(BlueToothMsg.DEVICE_NAME);				
				PreferencesUtils.putString(AmendPwdActivity.this, "device_name", mConnectedDeviceName);				
				if(!TextUtils.isEmpty(PreferencesUtils.getString(AmendPwdActivity.this, "device_name"))) {
					Toast.makeText(AmendPwdActivity.this,
							"已连接 " + PreferencesUtils.getString(AmendPwdActivity.this, "device_name"), 
							Toast.LENGTH_SHORT).show();
				}	
				break;
			}			
			}
		}
		
	};

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
