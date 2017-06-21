package com.ananda.tailing.bike.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 蓝牙密码
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:25:39
 */
public class BluetoothPwdActivity extends BaseActivity implements OnClickListener {

	private TitleBarView mTitleBarView;
	
	private EditText etBtPwd;
	private ImageView ivBtPwd;
	private LinearLayout layoutBtPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.bluetooth_passwd_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
	}
	
	/* 初始化控件
	 * (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("蓝牙密码");
		etBtPwd = (EditText) findViewById(R.id.bluetooth_pwd_edittext);
		ivBtPwd = (ImageView) findViewById(R.id.bluetooth_pwd_imageview);
		layoutBtPwd = (LinearLayout) findViewById(R.id.bluetooth_pwd_linearlayout);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.previous_button) {
			this.finish();
		} else if(v.getId() == R.id.next_button) {
			startActivity(new Intent(this, OtherInfoActivity.class)); 
		}
	}

}
