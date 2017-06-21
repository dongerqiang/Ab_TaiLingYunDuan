package com.ananda.tailing.bike.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ananda.tailing.bike.R;

/**
 * @package com.google.zxing.client.android.view
 * @description: 自定义对话框
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-5 下午8:03:48
 */
public class MyDialog extends Dialog {
	
	private TextView tvMes;
	private Handler handler;
	
	/**
	 * @param context
	 */
	public MyDialog(Context context, Handler handler) { 
		super(context, R.style.MyDialogStyle);
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.my_dialog);
		tvMes = (TextView) findViewById(R.id.message_textview);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
	}
	
	/**
	 * 设备文本信息
	 * 
	 * @param message
	 */
	public void setTextInfo(String resId) {
		this.tvMes.setText("" + resId);

	}

}
