package com.ananda.tailing.bike.view;

import com.ananda.tailing.bike.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**  
 * @package com.ananda.tailing.bike.view 
 * @description: 状态检测
 * @version v1.0  
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-9 上午10:39:52 
 */
public class StatusMonitorDialog extends Dialog {
	
	private TextView tvBatery;  // 当前电量
	private Button btnConfirm;  // 确定
	private Button btnSlip;     // 跳过

	/**
	 * @param context
	 */
	public StatusMonitorDialog(Context context) {
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.status_monitor_layout);
		tvBatery = (TextView) findViewById(R.id.text_batery);
		btnConfirm = (Button) findViewById(R.id.confrim_button);
		btnSlip = (Button) findViewById(R.id.slip_button);
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
	}
	
	/**
	 * 确定按钮点击事件
	 * @param listener
	 */
	public void setConfirmListener(View.OnClickListener listener) {
		btnConfirm.setOnClickListener(listener);
	}
	
	/**
	 * 跳过按钮点击事件
	 * @param listener
	 */
	public void setSlipListener(View.OnClickListener listener) {
		btnSlip.setOnClickListener(listener);
	}
	
	/**
	 * 设置电量
	 * @param batery
	 */
	public void setBatery(String batery) {
		this.tvBatery.setText(batery);
	}
		
}
