package com.ananda.tailing.bike.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ananda.tailing.bike.R;

/**  
 * @package com.ananda.tailing.bike.view 
 * @description: 电量监测
 * @version v1.0  
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-7 下午2:20:21 
 */
public class BateryLowDialog extends Dialog {

	private Button btnExecute;  // 按此速度
	private Button btnRestore;  // 恢复速度
	
	/**
	 * @param context
	 */
	public BateryLowDialog(Context context) { 
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.batery_low_layout);
		btnExecute = (Button) findViewById(R.id.button_execute);
		btnRestore = (Button) findViewById(R.id.button_restore);
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
	}
	
	/**
	 * 按此速度点击事件处理
	 * @param listener
	 */
	public void setExecuteListener(View.OnClickListener listener) {
		btnExecute.setOnClickListener(listener);
	}
	
	/**
	 * 恢复速度点击事件处理
	 * @param listener
	 */
	public void setRestoreListener(View.OnClickListener listener) {
		btnRestore.setOnClickListener(listener);
	}
	
}
