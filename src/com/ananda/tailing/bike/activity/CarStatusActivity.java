package com.ananda.tailing.bike.activity;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.data.BaseResponse;
import com.ananda.tailing.bike.data.CarInfoResponse;
import com.ananda.tailing.bike.data.Constants;
import com.ananda.tailing.bike.entity.CarInfo;
import com.ananda.tailing.bike.net.HttpExecute;
import com.ananda.tailing.bike.net.HttpRequest;
import com.ananda.tailing.bike.net.HttpResponseListener;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.TitleBarView;
import com.fu.baseframe.net.CallServer;
import com.fu.baseframe.net.CustomDataRequest;
import com.fu.baseframe.net.HttpListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;

import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_car_status_layout)
public class CarStatusActivity extends BaseActivity {
	@ViewById
	Button btnBufang,btnCefang,btndisEle,btnconnEle;
	@ViewById
	FrameLayout bateryFl;
	@ViewById
	ImageView bateryProImg;
	@ViewById
	TextView senTv,bateryTv,bufangTv,infoTv;
	
	@ViewById
	SeekBar senSeekbar;
	
	@ViewById
	TitleBarView title_bar;
	
	@AfterViews
	public void initViews(){
		btnBufang.setOnClickListener(btnOnClick);
		btnCefang.setOnClickListener(btnOnClick);
		btndisEle.setOnClickListener(btnOnClick);
		btnconnEle.setOnClickListener(btnOnClick);
		
		senSeekbar.setOnSeekBarChangeListener(seekBarChange);
		
		title_bar.setTitleLeftImg(R.drawable.icon_back, "车辆状态");
		title_bar.setTitleClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
	}
	
	OnSeekBarChangeListener seekBarChange = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			carSen(seekBar.getProgress());
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			senTv.setText("报警灵敏度："+seekBar.getProgress());
		}
	};
	
	@Override
	public void initWidget() {
		
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		btnBufang.setBackgroundResource(R.drawable.button_right_rect_select_bg);
		btnBufang.setTextColor(Color.BLACK);
		btnCefang.setBackgroundColor(0x00000000);
		btnCefang.setTextColor(Color.WHITE);
		
		btndisEle.setBackgroundResource(R.drawable.button_left_rect_select_bg);
		btndisEle.setTextColor(Color.BLACK);
		btnconnEle.setBackgroundColor(0x00000000);
		btnconnEle.setTextColor(Color.WHITE);
		
		getCarState();
	}



	OnClickListener btnOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(MyApplication.DEVIDE_ID.isEmpty()){
				Toast.makeText(CarStatusActivity.this, "请输入车辆IMEI号", Toast.LENGTH_SHORT).show();
				return;
			}
			if(v == btnBufang){
				carControlBufang();
		
			}else if(v == btnCefang){
				carControlCefang();
			}else if(v == btndisEle){
				carControlCloseEle();
			}else if(v == btnconnEle){
				carControlUpEle();
			}
		}
	};
	
	private void setBateryPro(float pro){
		
		bateryFl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		
		int wid = bateryFl.getMeasuredWidth();
		
		wid = wid - wid / 4 ;
		
		int proWid = (int) (pro * (int)wid /100f);
		
		FrameLayout.LayoutParams params = (LayoutParams) bateryProImg.getLayoutParams();
		params.width = proWid + wid / 4 /3;
	}
	
	private void getCarState(){

		String strUrl = String.format(Constants.BIKE_DETAIL_URL, MyApplication.DEVIDE_ID);
		Request<CarInfoResponse> request = new CustomDataRequest<CarInfoResponse>(strUrl,RequestMethod.GET,CarInfoResponse.class);
		request.setConnectTimeout(60 * 1000);
		request.setReadTimeout(60 * 1000);
		request.setHeader("context-Type", "application/json");
		request.setHeader("mobileNo", "");
		request.setHeader("mobiledeviceId", "");
		request.setHeader("accesstoken",  "");
		CallServer.getRequestInstance().add(this, strUrl.hashCode(), request, new HttpListener<CarInfoResponse>() {

			@Override
			public void onSucceed(int what, Response<CarInfoResponse> response) {
				if(response.isSucceed() && response.get() != null){
					if(response.get().data != null){
						
						CarInfo carInfo = response.get().data;
						showCarState(carInfo);
					}
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag, Exception exception, int responseCode,
					long networkMillis) {
				// TODO Auto-generated method stub
				
			}
		},this,true);
	}
	
	public void showCarState(CarInfo carInfo){
		if(carInfo == null) return;
		StringBuffer sb = new StringBuffer();
		sb.append("你好,"+MyApplication.MOBILE+"\n");
		sb.append("当前设备："+carInfo.imei+"\n");
		if(carInfo.sleep){
			sb.append("设备状态："+"已休眠\n");
		}else{
			if(carInfo.locked){
				sb.append("设备状态："+"打开电门锁\n");
			}else{
				sb.append("设备状态："+"关闭电门锁\n");
			}
		}
		
		infoTv.setText(sb.toString());
		
		if(!carInfo.locked){
			btnBufang.setBackgroundResource(R.drawable.button_right_rect_select_bg);
			btnBufang.setTextColor(Color.BLACK);
			btnCefang.setBackgroundColor(0x00000000);
			btnCefang.setTextColor(Color.WHITE);
		}else{
			btnCefang.setBackgroundResource(R.drawable.button_left_rect_select_bg);
			btnCefang.setTextColor(Color.BLACK);
			btnBufang.setBackgroundColor(0x00000000);
			btnBufang.setTextColor(Color.WHITE);
		}
		
		
		if(!carInfo.fired){
			btndisEle.setBackgroundResource(R.drawable.button_left_rect_select_bg);
			btndisEle.setTextColor(Color.BLACK);
			btnconnEle.setBackgroundColor(0x00000000);
			btnconnEle.setTextColor(Color.WHITE);
		}else{
			btnconnEle.setBackgroundResource(R.drawable.button_right_rect_select_bg);
			btnconnEle.setTextColor(Color.BLACK);
			btndisEle.setBackgroundColor(0x00000000);
			btndisEle.setTextColor(Color.WHITE);
		}
		
		senTv.setText("报警灵敏度："+carInfo.grade);
		senSeekbar.setProgress(carInfo.grade);
		
		float value = carInfo.battery;
        int vol = (int) Math.rint(value);
        setBateryPro(vol);
        
		String batryState = "";
		
		if(carInfo.charging){
			batryState = "充电中";
		}else{
			batryState = "未充电";
		}
        
        bateryTv.setText("充电状态："+batryState+(vol > 0  ? vol : "0") +"%");
	}

	
	private void carControlCefang(){
		
		String strUrl = String.format(Constants.UNLOCK_URL, MyApplication.DEVIDE_ID);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(":imei", MyApplication.DEVIDE_ID);
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, strUrl, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				
				if(result.statusCode == 200){
					btnCefang.setBackgroundResource(R.drawable.button_left_rect_select_bg);
					btnCefang.setTextColor(Color.BLACK);
					btnBufang.setBackgroundColor(0x00000000);
					btnBufang.setTextColor(Color.WHITE);
				}else{
					Toast.makeText(CarStatusActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, param, "POST", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
		
	}
	
	private void carControlBufang(){
		
		
		String strUrl = String.format(Constants.LOCK_URL, MyApplication.DEVIDE_ID);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(":imei", MyApplication.DEVIDE_ID);
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, strUrl, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				if(result.statusCode == 200){
					btnBufang.setBackgroundResource(R.drawable.button_right_rect_select_bg);
					btnBufang.setTextColor(Color.BLACK);
					btnCefang.setBackgroundColor(0x00000000);
					btnCefang.setTextColor(Color.WHITE);
					
					//断电
					btndisEle.setBackgroundResource(R.drawable.button_left_rect_select_bg);
					btndisEle.setTextColor(Color.BLACK);
					btnconnEle.setBackgroundColor(0x00000000);
					btnconnEle.setTextColor(Color.WHITE);
				}else{
					Toast.makeText(CarStatusActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, param, "POST", true);
				
		/*httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");*/
		HttpExecute.getInstance().addRequest(httpRequest);

	}
	//上电
	private void carControlUpEle(){
		
		String strUrl = String.format(Constants.START_URL, MyApplication.DEVIDE_ID);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(":imei", MyApplication.DEVIDE_ID);
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, strUrl, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				if(result.statusCode == 200){
					btnconnEle.setBackgroundResource(R.drawable.button_right_rect_select_bg);
					btnconnEle.setTextColor(Color.BLACK);
					btndisEle.setBackgroundColor(0x00000000);
					btndisEle.setTextColor(Color.WHITE);
					
					//撒防
					btnCefang.setBackgroundResource(R.drawable.button_left_rect_select_bg);
					btnCefang.setTextColor(Color.BLACK);
					btnBufang.setBackgroundColor(0x00000000);
					btnBufang.setTextColor(Color.WHITE);
				}else{
					Toast.makeText(CarStatusActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, param, "POST", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
	}
	
	private void carControlCloseEle(){
	
		String strUrl = String.format(Constants.STOP_URL, MyApplication.DEVIDE_ID);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(":imei", MyApplication.DEVIDE_ID);
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, strUrl, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				if(result.statusCode == 200){
					btndisEle.setBackgroundResource(R.drawable.button_left_rect_select_bg);
					btndisEle.setTextColor(Color.BLACK);
					btnconnEle.setBackgroundColor(0x00000000);
					btnconnEle.setTextColor(Color.WHITE);
				}else{
					Toast.makeText(CarStatusActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, param, "POST", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
	}
	
	
	private void carSen(int sen){
		
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("imei", MyApplication.DEVIDE_ID);
		params.put("grade", sen+"");
		
		HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, Constants.SET_SENSITIVE_URL, new HttpResponseListener<BaseResponse>() {

			@Override
			public void onResult(BaseResponse result) {
				if(result == null) return;
				if(result.statusCode == 200){
					btndisEle.setBackgroundResource(R.drawable.button_left_rect_select_bg);
					btndisEle.setTextColor(Color.BLACK);
					btnconnEle.setBackgroundColor(0x00000000);
					btnconnEle.setTextColor(Color.WHITE);
				}else{
					Toast.makeText(CarStatusActivity.this, result.message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(int code) {
				
			}
		}, BaseResponse.class, params, "POST", true);
				
		httpRequest.addHead("mobileNo", "");
		httpRequest.addHead("mobiledeviceId", "");
		httpRequest.addHead("accesstoken", "");
		HttpExecute.getInstance().addRequest(httpRequest);
	}
}
