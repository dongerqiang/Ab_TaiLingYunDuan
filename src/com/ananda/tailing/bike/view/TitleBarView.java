package com.ananda.tailing.bike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ananda.tailing.bike.R;

/**
 * @package com.ananda.tailing.bike.view
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-17 下午2:37:17
 */
public class TitleBarView extends LinearLayout {

	private TextView tvTitle;  // 标题
	
	/**
	 * @param context
	 */
	public TitleBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
		
	}
	private void init(Context context){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_bar, this, true);
		tvTitle = (TextView) findViewById(R.id.title_textview);
	}
	
	/**
	 * 设置标题
	 * @param index
	 */
	public void showTitle(int index) {
		switch(index) {
		case 0: {
			this.setTitle("遥控功能");
			break;
		}
		case 1: {
			this.setTitle("仪表功能");
			break;
		}
		case 2: {
			this.setTitle("");
			break;
		}
		case 3: {
			this.setTitle("更多功能");
			break;
		}
		}
	}
	
	/**
	 * 设置标题文本
	 * @param text 字符串
	 */
	public void setTitle(String text) {
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(text);
	}
	
	/**
	 * 设置标题文本
	 * @param stringID R.string.xxx
	 */
	public void setTitle(int stringID) {
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(stringID);
	}

}
