package com.ananda.tailing.bike.view;

import com.ananda.tailing.bike.R;

import android.app.Dialog;
import android.content.Context;

/**  
 * @package com.ananda.tailing.bike.view 
 * @description: 通用对话框
 * @version v1.0  
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-9 上午11:20:44 
 */
public class CommonDialog extends Dialog {

	/**
	 * @param context
	 */
	public CommonDialog(Context context) {
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setContentView(R.layout.common_dialog);
		
		// 设置点击其他地方不能关闭
		this.setCanceledOnTouchOutside(true);
		
	}

}
