package com.ananda.tailing.bike.activity.more;

import android.os.Bundle;
import android.webkit.WebView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 关于我们
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午6:05:48
 */
public class AboutActivity extends BaseActivity {
	
	private TitleBarView mTitleBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.about_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
	}

	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("关于我们");
		
		WebView mWeb = (WebView) findViewById(R.id.webView1);
		mWeb.loadUrl("file:///android_asset/company.html"); 
	}

}
