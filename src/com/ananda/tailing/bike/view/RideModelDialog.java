package com.ananda.tailing.bike.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.util.PreferencesUtils;

/**  
 * @package com.ananda.tailing.bike.view 
 * @description: 骑行模式对话框
 * @version v1.0  
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-6-3 下午3:44:02 
 */
public class RideModelDialog extends Dialog {
		
	private Context ctx;
	/** 骑行模式 */
	private RadioGroup rgModel;
	private RadioButton rbCity, rbMount, rbTrack;
	private Button btnConfirm;
	
	public int mode;
	
	public int getMode() {
		return mode;
	}

	/**
	 * @param context
	 */
	public RideModelDialog(Context context) { 
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
		ctx = context;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.ride_mode_dialog);
				
		rgModel = (RadioGroup) findViewById(R.id.radiogroup_model);
		btnConfirm = (Button) findViewById(R.id.button_confirm);
		rbCity = (RadioButton) findViewById(R.id.radio_city);
		rbMount = (RadioButton) findViewById(R.id.radio_mount);
		rbTrack = (RadioButton) findViewById(R.id.radio_track); 
		
		int model = PreferencesUtils.getInt(ctx, "ride_model"); 
		if(model > 0) {
			switch(model) {
			case 1:{
				rbCity.setChecked(true);
				break;
			}
			case 2: {
				rbMount.setChecked(true);
				break;
			}
			case 3: {
				rbTrack.setChecked(true);
				break;
			}
			}
		}
		
		rgModel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton)findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				mode = Integer.valueOf(rb.getTag().toString());
				PreferencesUtils.putInt(ctx, "ride_model", mode);
			}
		});
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
	}
		
	/**
	 * 确定按钮事件
	 * @param listener
	 */
	public void setConfirmListener(View.OnClickListener listener) {
		btnConfirm.setOnClickListener(listener);
	}
	
}
