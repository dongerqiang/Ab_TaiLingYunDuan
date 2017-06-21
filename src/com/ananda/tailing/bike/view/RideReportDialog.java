package com.ananda.tailing.bike.view;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.MyApplication;
import com.ananda.tailing.bike.activity.RomtorActivity;
import com.ananda.tailing.bike.entity.RideReportInfo;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.DateUtils;
import com.ananda.tailing.bike.util.HttpAPI;
import com.ananda.tailing.bike.util.HttpRestClient;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 骑行报告
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-24 下午2:56:45
 */
public class RideReportDialog extends Dialog {

	private Context ctx;
	private Handler mHandler;
	
	private TextView tvMileage;        // 行驶里程
	private TextView tvTravelTime;     // 行驶时间
	private TextView tvBatery;         // 耗电量
	private TextView tvMaxSpeed;       // 最高行驶速度
	private TextView tvAverageSpeed;   // 平均行驶速度
	private TextView tvRating;         // 能耗评分
	private TextView tvControlTemper;  // 控制器温度
	private TextView tvCurrent;        // 电流值
	private TextView tvBTime;          // 起始时间
	private TextView tvETime;          // 结束时间
	
	private String beginTime;
	private String endTime;
	private int mileage;
	
	/**
	 * @param context
	 */
	public RideReportDialog(Context context, Handler handler) { 
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
		ctx = context;
		mHandler = handler;
	}

	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.ride_report_layout);
		initWidget();
		commit();
	}
	
	protected void commit(){
		List<RideReportInfo> listReport = new ArrayList<RideReportInfo>();
		RideReportInfo report1 = new RideReportInfo();
		report1.setAverageSpeed(tvAverageSpeed.getText().toString());          // 平均行驶速度
		report1.setControlTemper(tvControlTemper.getText().toString());         // 控制器温度
		report1.setEnergyRated(PreferencesUtils.getInt(ctx, "energy"));            // 能耗评级
		report1.setLonger(tvTravelTime.getText().toString());                // 时长
		report1.setMagnitudeOfCurrent(tvCurrent.getText().toString());    // 电流量
		report1.setMileage(tvMileage.getText().toString());               // 里程
		report1.setSaveTime(DateUtils.currentDate());              // 保存时间
		report1.setSurplusBatery("");         // 剩余电量
//		report1.setChargingTimes(2);          // 充电次数
		listReport.add(report1);
		if (CommonUtils.isNetWorkNormal(MyApplication.getInstance())) {
			// 写逻辑提交数据到服务器
			try {
				JSONObject jsonLogin = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < listReport.size(); i++) {
					RideReportInfo info = listReport.get(i);
					JSONObject jsonObject = JSONObject.parseObject(JSONObject
							.toJSONString(info));
					jsonArray.add(jsonObject);
				}
				jsonLogin.put("list", jsonArray);
				jsonLogin.put("Timestamp", CommonUtils.getTimestamp());

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Result", HttpAPI.RIDEREPORT);
				jsonObject.put("Info", JSONObject.parseObject(jsonLogin.toString()));

				System.err.println("骑行报告报文：-->" + jsonObject.toString());
				StringEntity stringEntity;
				stringEntity = new StringEntity(jsonObject.toString());
				HttpRestClient.post(MyApplication.getInstance(), HttpAPI.USER_URL,
						stringEntity, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(error, content);
								MyToast.showShortToast(MyApplication.getInstance(),
										HttpAPI.RESPONSE);
							}

							@Override
							public void onSuccess(int statusCode, String content) {
								// TODO Auto-generated method stub
								super.onSuccess(statusCode, content);
								System.out.println("返回的报文123: -->" + content); 
								JSONObject jsonObject = JSONObject.parseObject(content); 
								String result = jsonObject.getString("Result");
								if(result.equals("070")) {
									// 上传成功之后, 更新时间, 并清除记录
									System.out.println("上传成功");
	//								PreferencesUtils.putString(RomtorActivity.this, 
	//										"date_time", CommonUtils.getCurrentDate());
	//								db.clearTable(DBState.TABLE_RIDEREPORT);
								}
							}

						});
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			MyToast.showShortToast(MyApplication.getInstance(), "网络不可用, 请先打开网络!");
		}
	}

	/**
	 * 初始化控件
	 */
	private void initWidget() {
		tvMileage = (TextView) findViewById(R.id.mileage_textview);
		tvTravelTime = (TextView) findViewById(R.id.travel_time_textview);
		tvBatery = (TextView) findViewById(R.id.batery_textview);
		tvMaxSpeed = (TextView) findViewById(R.id.maxinum_speed_textview);
		tvAverageSpeed = (TextView) findViewById(R.id.average_speed_textview);
		tvRating = (TextView) findViewById(R.id.rating_textview); 
		tvControlTemper = (TextView) findViewById(R.id.control_temper_textview);
		tvCurrent = (TextView) findViewById(R.id.current_textview);
		tvBTime = (TextView) findViewById(R.id.begin_time_textview);
		tvETime = (TextView) findViewById(R.id.end_time_textview);
		
		// 本次行驶里程
		if(PreferencesUtils.getInt(ctx, "mileage") > 0) {
			mileage = PreferencesUtils.getInt(ctx, "mileage"); 
			tvMileage.setText(PreferencesUtils.getInt(ctx, "mileage")+ "公里");
		} else {
			mileage = 0;
			tvMileage.setText("0公里");
		}
		// 最高行驶速度
		if(PreferencesUtils.getInt(ctx, "maxSpeed") > 0) {
			tvMaxSpeed.setText(PreferencesUtils.getInt(ctx, "maxSpeed") + "km/h");
		} else {
			tvMaxSpeed.setText("0km/h");
		}
		// 耗电量
		if(PreferencesUtils.getInt(ctx, "bateryShow") > 0) {
			int batery = PreferencesUtils.getInt(ctx, "bateryShow");
			switch(batery) {
			case 1: {
				tvBatery.setText("0%");
				break;
			}
			case 2: {
				tvBatery.setText("20%");
				break;
			}
			case 3: {
				tvBatery.setText("50%");
				break;
			}
			case 4: {
				tvBatery.setText("80%");
				break;
			}
			case 5: {
				tvBatery.setText("100%");
				break;
			}
			}
		} else {
			tvBatery.setText("0%");
		}
		
		// 能耗评分
		if(PreferencesUtils.getInt(ctx, "energy") > 0) {
			System.out.println("能耗评分: " + PreferencesUtils.getInt(ctx, "energy") + "分");  
			tvRating.setText(PreferencesUtils.getInt(ctx, "energy") + "分");
		} else {
			tvRating.setText("0分");
		}
		
		// 控制器温度
//		if(PreferencesUtils.getInt(ctx, "temper") > 0) {
//			int enerty = PreferencesUtils.getInt(ctx, "temper");
//			int nenerty = enerty - 40;
//			System.out.println(String.valueOf(nenerty).substring(1)  + "°");  
//			tvControlTemper.setText(String.valueOf(nenerty).substring(1) + "°");
//		} else {
//			tvControlTemper.setText("0°");
//		}
		tvControlTemper.setText("-/-");
		
		// 电流值
//		if(PreferencesUtils.getInt(ctx, "current") > 0) {
//			tvCurrent.setText(PreferencesUtils.getInt(ctx, "current") + "");
//		} else {
//			tvCurrent.setText("0");
//		}
		
		tvCurrent.setText("-/-");
		
		// 起始时间
		if(!TextUtils.isEmpty(PreferencesUtils.getString(ctx, "beginTime"))) {
			beginTime = PreferencesUtils.getString(ctx, "beginTime");
		} else {
			beginTime = CommonUtils.getDateTime();
		}
		
		// 结束时间
		if(!TextUtils.isEmpty(PreferencesUtils.getString(ctx, "endTime"))) {
			endTime = PreferencesUtils.getString(ctx, "endTime");
		} else {
			endTime = CommonUtils.getDateTime();
		}
				
		float minute = CommonUtils.getMistining(endTime, beginTime);	
		System.out.println("所耗时间：-->" + minute); 
		// 行驶时间
		tvTravelTime.setText(CommonUtils.getTravelTime(minute) + "分钟");		
		tvBTime.setText(beginTime + "           起"); 
		tvETime.setText(endTime +  "           止");		
		// 平均行驶速度
		System.err.println("本次里程: -->" + mileage); 
		System.err.println("时间差: -->" + minute);

		System.out.println("平均行驶速度: -->" + CommonUtils.getAverageSpeed(mileage, minute)); 
		if(CommonUtils.getAverageSpeed(mileage, minute).equals("NaN")) {
			tvAverageSpeed.setText("0km/h");
		} else {
			tvAverageSpeed.setText(".0".equals(CommonUtils.getAverageSpeed(mileage, minute))?"0":CommonUtils.getAverageSpeed(mileage, minute) + "km/h");
		}
				
//		// 保存骑行报告数据 
//		RideReportInfo report = new RideReportInfo();
//		report.setAverageSpeed(tvAverageSpeed.getText().toString().trim());          // 平均行驶速度
//		report.setControlTemper(tvControlTemper.getText().toString().trim());         // 控制器温度
//		report.setEnergyRated(Integer.valueOf(tvRating.getText().toString().trim()));            // 能耗评级
//		report.setLonger(String.valueOf(minute));                // 时长
//		report.setMagnitudeOfCurrent(tvControlTemper.getText().toString().trim());    // 电流量
//		report.setMileage(tvMileage.getText().toString().trim());               // 里程
//		report.setPeriodOfTime(String.valueOf(minute));          // 时段
//		report.setSaveTime(CommonUtils.getDateTime());              // 保存时间
//		report.setSurplusBatery(tvBatery.getText().toString().trim());         // 剩余电量
//		// 保存数据到本地数据库
//		SQLUtil.saveRideReport(RideReportActivity.this, report);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(5000);
					mHandler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
		
	}
	
}
