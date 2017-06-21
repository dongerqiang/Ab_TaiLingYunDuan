package com.ananda.tailing.bike.activity.consultation;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;

public class ConsultationItemDetailsActivity extends BaseActivity implements OnClickListener {

	private WebView consultation_details_vv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.consultation_item_details);
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initWidget() {
		consultation_details_vv = (WebView) findViewById(R.id.consultation_details_vv);
		String urlData = (String) getIntent().getSerializableExtra("consultation_web_url");
//		consultation_details_vv.getSettings().setJavaScriptEnabled(true);
		consultation_details_vv.getSettings().setDefaultTextEncodingName("UTF-8");
		consultation_details_vv.loadData(urlData, "text/html; charset=UTF-8", null);
	}

	

}
