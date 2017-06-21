package com.ananda.tailing.bike.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.consultation.ConsultationAdapter;
import com.ananda.tailing.bike.activity.consultation.ConsultationItemDetailsActivity;
import com.ananda.tailing.bike.activity.more.ArsReceiver;
import com.ananda.tailing.bike.entity.ConsulationInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.JsonPaserUtils;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.view.ScrollListView;
import com.ananda.tailing.bike.view.TabBarView;
import com.ananda.tailing.bike.view.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 更多
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午4:16:04
 */
public class ConsultationActivity extends BaseActivity implements OnClickListener {

	private TitleBarView titleBarView;
	private TabBarView tabBarView;
	private ScrollListView scrollListView;
	private ConsultationAdapter consultationAdapter;
	private List<ConsulationInfo> consulationInfoList = new ArrayList<ConsulationInfo>();
	private Context context;
	private ArsReceiver arsReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.consultation_layout);
		context = this;
		super.onCreate(savedInstanceState);		
		CommonUtils.subActivityStack.add(this);

//		if(RomtorActivity.arsServiceFlag){
			IntentFilter arsFilter = new IntentFilter(RomtorActivity.ARS_TAG);
			arsReceiver = new ArsReceiver(context);
			this.registerReceiver(arsReceiver, arsFilter);
//		}
	}
	
	@Override
	protected void initWidget() {
		
		titleBarView = (TitleBarView) findViewById(R.id.title_bar);
		titleBarView.setTitle("资讯功能");
		
		tabBarView = (TabBarView) findViewById(R.id.tab_bar);
		tabBarView.setIndex(2);
		
		scrollListView = (ScrollListView) findViewById(R.id.list_consultation);
		
		initData();
		initListener();
	}

	protected void initData(){
		getConsultationListWork();
	}

	protected void initListener(){
		scrollListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bu=new Bundle();
	            bu.putSerializable("consultation_web_url", consulationInfoList.get(position).getContent());
				Intent intent = new Intent(ConsultationActivity.this, ConsultationItemDetailsActivity.class);
				intent.putExtras(bu);
				startActivity(intent);
			}
		});
	}
	

	@Override
	public void onDestroy() {
		if(arsReceiver!=null){
			try{
				this.unregisterReceiver(arsReceiver);
			}catch(Exception e){
			}
		}
		super.onDestroy();
	}
	
	/**
	 * 请求网络获取咨询数据
	 */
	private void getConsultationListWork() {
		showProgressDialog(ConsultationActivity.this, "请稍后...");
		String jr = getStringJr();
		HttpEntity httpEntity = null;
		try {
			httpEntity = new StringEntity(jr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpRestClient.post(context,HttpAPI.USER_URL, httpEntity, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				dismissProgressDialog();
				System.err.println("错误信息: -->" + content);
				MyToast.showShortToast(ConsultationActivity.this, HttpAPI.RESPONSE);
			}
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				dismissProgressDialog();
				System.err.println("consultation返回数据：-->" + content);
				JSONObject jsonObject = JSONObject.parseObject(content);
				System.out.println("----jsonObject:" + jsonObject);
				if (jsonObject.getString("Result").equals("100")) {
					consulationInfoList = JsonPaserUtils
							.parseJsonToConsultationInfo(jsonObject.getJSONArray("Info")
									.toString());
					System.out.println("解析后的数据：-->" + consulationInfoList.size());
					consultationAdapter = new ConsultationAdapter(context, consulationInfoList);
					scrollListView.setAdapter(consultationAdapter);
				} else {
					System.out.println(jsonObject.getString("errorMsg"));
				}
			}
			
		});
		
	}
	
	private String getStringJr(){
		Date date = new Date();
		String timeStr = (String) DateFormat.format("yyyyMMdd", date);
		
		JSONObject c = new JSONObject();
		c.put("Result", "10");
		Map<String,String> map = new HashMap<String,String>();
		map.put("Timestamp", timeStr);
		c.put("Info", map);
		System.out.println(c.toString());
		return c.toString();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void onBackPressed() {
		startActivity(new Intent(ConsultationActivity.this, RomtorActivity.class));
		overridePendingTransition(0, 0);
		ConsultationActivity.this.finish();
	}
}
