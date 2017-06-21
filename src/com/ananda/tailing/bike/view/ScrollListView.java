package com.ananda.tailing.bike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/*
 * 自定义listView，解决scrollView嵌套显示不全
 */
public class ScrollListView extends ListView {

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public ScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		Log.d("", "onMeasure:" + expandSpec);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
