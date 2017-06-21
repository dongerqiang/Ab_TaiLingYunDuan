package com.ananda.tailing.bike.activity.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.smartbike.SmartBikeInstance;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.MyToast;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.SlipButton;
import com.ananda.tailing.bike.view.SlipButton.OnChangedListener;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 设置
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午6:03:19
 */
public class SettingActivity extends BaseActivity {

	private TitleBarView mTitleBarView;

	private SlipButton slipLock; // 车辆上锁
	private SlipButton slipUnLock; // 车辆解锁
	private SlipButton slipAlarm; // 寻车警报
	private SlipButton slipSaddles; // 鞍座开启
	private SlipButton slipRide; // 本次骑行报告
	private SlipButton slipFort; // 自动设防
	private SlipButton slipArs; // ARS自动语音功能
	private SlipButton slipVoiceControl;//静音布防
	private SlipButton slipSpeed; // 仪表速度显示
	private SlipButton slipBatery; // 仪表电量显示
	private SlipButton slipVersion; // 版本更新提示
	private SlipButton slipWarning; // 警告消息提示
	private SlipButton slipPush; // 促销消息推送
	private SlipButton SlipMonitor; // 状态监测
	private SlipButton SlipOverload; // 超负荷监测
	
	private Button guard_distance_setting_bt;//布防距离
	private TextView guard_distance_show_tv;

	private RadioGroup vibration_radiogroup;
	private RadioButton vibration_level_1;
	private RadioButton vibration_level_2;
	private RadioButton vibration_level_3;
	private RadioButton vibration_level_4;
	private RadioButton vibration_level_5;
	
	private RadioGroup control_type_radiogroup;
	private RadioButton control_type_phone_rb;
	private RadioButton control_type_hand_rb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.setting_layout);
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
	}

	protected void initSmartBike() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("设置");
		
		guard_distance_setting_bt = (Button) findViewById(R.id.guard_distance_setting_bt);
		guard_distance_show_tv = (TextView) findViewById(R.id.guard_distance_show_tv);
		String guard_diatance = PreferencesUtils.getString(this, "guard_distance");
		if(guard_diatance != null && !guard_diatance.isEmpty()){
			guard_distance_show_tv.setText(guard_diatance);
		}
		guard_distance_setting_bt.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				int result = SmartBikeInstance.setGuardDistance();
				String distance = "";
				if(result == -1){
					MyToast.showShortToast(SettingActivity.this, "设备未连接！");
				}else if(result < 10){
					distance = "非常近";
				}else if(result >= 10 && result < 30){
					distance = "很近";
				}else if(result >= 30 && result < 50){
					distance = "比较近";
				}else if(result >= 50){
					distance = "远";
				}
				MyToast.showShortToast(SettingActivity.this, "设置成功！");
				PreferencesUtils.putString(SettingActivity.this, "guard_distance", distance);
				guard_distance_show_tv.setText(distance);
				return false;
			}
		});
		
		slipUnLock = (SlipButton) findViewById(R.id.unlock_slipbutton);
		if(PreferencesUtils.getString(this, "unlock", "0").equals("0")) {
			slipUnLock.NowChoose = true;
		} else {
			slipUnLock.NowChoose = false;
		}
		slipLock = (SlipButton) findViewById(R.id.lock_slipbutton);
		if(PreferencesUtils.getString(this, "lock", "0").equals("0")) {
			slipLock.NowChoose = true;
		} else {
			slipLock.NowChoose = false;
		}
		slipAlarm = (SlipButton) findViewById(R.id.find_car_slipbutton);
		if(PreferencesUtils.getString(this, "alarm", "0").equals("0")) {
			slipAlarm.NowChoose = true;
		} else {
			slipAlarm.NowChoose = false;
		}
		slipSaddles = (SlipButton) findViewById(R.id.saddles_open_slipbutton);
		if(PreferencesUtils.getString(this, "saddles", "0").equals("0")) {
			slipSaddles.NowChoose = true;
		} else {
			slipSaddles.NowChoose = false;
		}
		slipRide = (SlipButton) findViewById(R.id.ride_report_slipbutton);
		if(PreferencesUtils.getString(this, "ride", "0").equals("0")) {
			slipRide.NowChoose = true;
		} else {
			slipRide.NowChoose = false;
		}
		slipFort = (SlipButton) findViewById(R.id.automatic_fortification_slipbutton);
		if(PreferencesUtils.getString(this, "fortification", "0").equals("0")) {
			slipFort.NowChoose = true;
		} else {
			slipFort.NowChoose = false;
		}
		slipArs = (SlipButton) findViewById(R.id.ars_voice_slipbutton);
		if(PreferencesUtils.getString(this, "ars", "0").equals("0")) {
			slipArs.NowChoose = true;
		} else {
			slipArs.NowChoose = false;
		}
		slipVoiceControl = (SlipButton) findViewById(R.id.voice_control_slipbutton);
		if(PreferencesUtils.getString(this, "bike_voice_control", "0").equals("0")) {
			slipVoiceControl.NowChoose = false;
		} else {
			slipVoiceControl.NowChoose = true;
		}
		
		slipSpeed = (SlipButton) findViewById(R.id.meter_speed_slipbutton);
		if(PreferencesUtils.getString(this, "speedControl", "0").equals("0")) {
			slipSpeed.NowChoose = true;
		} else {
			slipSpeed.NowChoose = false;
		}
		slipBatery = (SlipButton) findViewById(R.id.meter_batery_slipbutton);
		if(PreferencesUtils.getString(this, "batery", "0").equals("0")) {
			slipBatery.NowChoose = true;
		} else {
			slipBatery.NowChoose = false;
		}
		
		SlipMonitor = (SlipButton) findViewById(R.id.status_monitoring);
		if(PreferencesUtils.getString(this, "monitor", "0").equals("0")) {
			SlipMonitor.NowChoose = true;
		} else {
			SlipMonitor.NowChoose = false;
		}
		
		SlipOverload = (SlipButton) findViewById(R.id.overload);
		if(PreferencesUtils.getString(this, "overload", "0").equals("0")) {
			SlipOverload.NowChoose = true;
		} else {
			SlipOverload.NowChoose = false;
		}
		
		
		slipVersion = (SlipButton) findViewById(R.id.ersion_update_prompt_slipbutton);
		slipWarning = (SlipButton) findViewById(R.id.warning_message_prompts_slipbutton);
		slipPush = (SlipButton) findViewById(R.id.promotional_messages_push_slipbutton);
		
		// 车辆上锁
		slipUnLock.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "unlock", "0");  // 开
				} else { 
					PreferencesUtils.putString(SettingActivity.this, "unlock", "1");  // 关
				}
			}
		});
		// 车辆解锁
		slipLock.SetOnChangedListener(new OnChangedListener() { 
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "lock", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "lock", "1");
				}
			}
		});
		// 寻车报警
		slipAlarm.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "alarm", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "alarm", "1");
				}
			}
		});
		// 鞍座开启
		slipSaddles.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "saddles", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "saddles", "1");
				}
			}
		});
		// 本次骑行报告
		slipRide.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "ride", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "ride", "1");
				}
			}
		});
		// 自动设防
		slipFort.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "fortification", "0");
					SmartBikeInstance.setAutoArm();
				} else {
					PreferencesUtils.putString(SettingActivity.this, "fortification", "1");
					SmartBikeInstance.setAutoArm();
				}
			}
		});
		// ARS自动语音功能
		slipArs.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "ars", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "ars", "1");
				}
			}
		});
		
		// 语音控制功能
		slipVoiceControl.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if (CheckState) {
					PreferencesUtils
							.putString(SettingActivity.this, "bike_voice_control", "1");
					SmartBikeInstance.closeVoice(SettingActivity.this);
				} else {
					PreferencesUtils
							.putString(SettingActivity.this, "bike_voice_control", "0");
					SmartBikeInstance.openVoice(SettingActivity.this);
				}
			}
		});
		// 仪表速度显示
		slipSpeed.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "speedControl", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "speedControl", "1");
				}
			}
		});
		// 仪表电量显示
		slipBatery.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "batery", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "batery", "1");
				}
			}
		});
		// 版本更新提示
		slipVersion.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "version", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "version", "1");
				}
			}
		});
		// 状态监测
		SlipMonitor.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "monitor", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "monitor", "1");
				}
			}
		});
		// 超负荷监测
		SlipOverload.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "overload", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "overload", "1");
				}
			}
		});		
				
		slipWarning.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "", "1");
				}
			}
		});
		slipPush.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				if(CheckState) {
					PreferencesUtils.putString(SettingActivity.this, "", "0");
				} else {
					PreferencesUtils.putString(SettingActivity.this, "", "1");
				}
			}
		});
		
		vibration_radiogroup = (RadioGroup) findViewById(R.id.vibration_radiogroup);
		vibration_level_1 = (RadioButton) findViewById(R.id.vibration_level_1);
		vibration_level_2 = (RadioButton) findViewById(R.id.vibration_level_2);
		vibration_level_3 = (RadioButton) findViewById(R.id.vibration_level_3);
		vibration_level_4 = (RadioButton) findViewById(R.id.vibration_level_4);
		vibration_level_5 = (RadioButton) findViewById(R.id.vibration_level_5);
		String level = PreferencesUtils.getString(SettingActivity.this, "vibration_level");
		if("0".equals(level)){
			vibration_radiogroup.check(R.id.vibration_level_1);
		}else if("1".equals(level)){
			vibration_radiogroup.check(R.id.vibration_level_2);
		}else if("2".equals(level)){
			vibration_radiogroup.check(R.id.vibration_level_3);
		}else if("3".equals(level)){
			vibration_radiogroup.check(R.id.vibration_level_4);
		}else if("4".equals(level)){
			vibration_radiogroup.check(R.id.vibration_level_5);
		}
		vibration_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SmartBikeInstance.setSmartBikeArmConfigTrue();
				if(vibration_level_1.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "vibration_level", "0");
				}else if(vibration_level_2.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "vibration_level", "1");
				}else if(vibration_level_3.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "vibration_level", "2");
				}else if(vibration_level_4.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "vibration_level", "3");
				}else if(vibration_level_5.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "vibration_level", "4");
				}
				SmartBikeInstance.setShockSensitivity();
			}
		});
		
		control_type_radiogroup = (RadioGroup) findViewById(R.id.control_type_radiogroup);
		control_type_phone_rb = (RadioButton) findViewById(R.id.control_type_phone_rb);
		control_type_hand_rb = (RadioButton) findViewById(R.id.control_type_hand_rb);
		String control_type = PreferencesUtils.getString(SettingActivity.this, "control_type");
		if("0".equals(control_type)){
			control_type_radiogroup.check(R.id.control_type_phone_rb);
		}else if("1".equals(control_type)){
			control_type_radiogroup.check(R.id.control_type_hand_rb);
		}
		control_type_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(control_type_phone_rb.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "control_type", "0");
				}else if(control_type_hand_rb.getId() == checkedId){
					PreferencesUtils.putString(SettingActivity.this, "control_type", "1");
					SmartBikeInstance.closeDevice();
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
