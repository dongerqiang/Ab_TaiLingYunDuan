package com.ananda.tailing.bike.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.view.TitleBarView;
import com.google.zxing.client.android.CaptureActivity;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 扫描界面
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午12:55:18
 */
public class QrCodeActivity extends BaseActivity implements OnClickListener {
	
	private TitleBarView mTitleBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.qr_code_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.scan_button) {
			startActivity(new Intent(this, CaptureActivity.class)); 
		} else if(v.getId() == R.id.skip_button) {
			startActivity(new Intent(this, OtherInfoActivity.class)); 
		}
	}

	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("二维码扫描");
	}

}
