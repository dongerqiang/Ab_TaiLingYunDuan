package com.ananda.tailing.bike.view;

import com.ananda.tailing.bike.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**  
 * @package com.ananda.tailing.bike.view 
 * @description: ARS故障对话框
 * @version v1.0  
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-19 下午3:12:42 
 */
public class ArsDialog extends Dialog {
	
	private TextView tvMsg;
	private Button btnRepair;
	
	/**
	 * @param context
	 */
	public ArsDialog(Context context) { 
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.ars_dialog);
		tvMsg = (TextView) findViewById(R.id.textview_content);
		btnRepair = (Button) findViewById(R.id.button_repair);
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
	}
	
	/**
	 * 设置文本
	 * @param message
	 */
	public void setTextContent(String message) {
		tvMsg.setText(message);
	}
		
	/**
	 * 修复按钮事件
	 * @param listener
	 */
	public void setButtonRepairListener(View.OnClickListener listener) {
		btnRepair.setOnClickListener(listener);
	}
		
}
