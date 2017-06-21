package com.ananda.tailing.bike.view;

import android.widget.ListView;

/**
 * @package com.ananda.tailing.bike.view
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-19 下午4:55:26
 */
public class MyListView extends ListView {
	
	public MyListView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	/*** Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
