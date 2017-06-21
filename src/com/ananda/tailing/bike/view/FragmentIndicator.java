package com.ananda.tailing.bike.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ananda.tailing.bike.R;

/**
 * @package com.ananda.tailing.bike.view
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-7 上午11:19:36
 */
public class FragmentIndicator extends LinearLayout implements OnClickListener {

	private static final int TAG_0 = 0;
	private static final int TAG_1 = 1;
	private static final int TAG_2 = 2;
	private static final int TAG_3 = 3;
	
	private Context mContext;
	private List<View> itemList;
	private List<View> textList;

	/**
	 * @param context
	 */
	public FragmentIndicator(Context context) {
		super(context);		
		// TODO Auto-generated constructor stub
		mContext = context;
		initView();
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public FragmentIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    View layout = inflater.inflate(R.layout.tab_frame, null);
	    layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
	    ImageView ivRomtor = (ImageView) layout.findViewById(R.id.imageview_romtor);
	    TextView tvRomtor = (TextView) layout.findViewById(R.id.textview_romtor);
	    
	    ImageView ivMeter = (ImageView) layout.findViewById(R.id.imageview_meter);
	    TextView tvMeter = (TextView) layout.findViewById(R.id.textview_meter);
	    
	    ImageView ivArs = (ImageView) layout.findViewById(R.id.imageview_ars);
	    TextView tvArs = (TextView) layout.findViewById(R.id.textview_ars);
	    
	    ImageView ivMore = (ImageView) layout.findViewById(R.id.imageview_more);
	    TextView tvMore = (TextView) layout.findViewById(R.id.textview_more);
	    
	    ivRomtor.setOnClickListener(this);
	    ivMeter.setOnClickListener(this);
	    ivArs.setOnClickListener(this);
	    ivMore.setOnClickListener(this);
	    ivRomtor.setTag(TAG_0);
	    ivMeter.setTag(TAG_1);
	    ivArs.setTag(TAG_2);
	    ivMore.setTag(TAG_3);
	    tvRomtor.setTag(TAG_0);
	    tvMeter.setTag(TAG_1);
	    tvArs.setTag(TAG_2);
	    tvMore.setTag(TAG_3);
	    
	    itemList = new ArrayList<View>();
	    itemList.add(ivRomtor);
	    itemList.add(ivMeter);
	    itemList.add(ivArs);
	    itemList.add(ivMore);
	    
	    textList = new ArrayList<View>();
	    textList.add(tvRomtor);
	    textList.add(tvMeter);
	    textList.add(tvArs);
	    textList.add(tvMore);
	    this.addView(layout);
	    
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		switch(tag) {
		case TAG_0: {
			setNormalState(lastButton);
			setSelectedState(tag);
			break;
		}
		case TAG_1: {
			setNormalState(lastButton);
			setSelectedState(tag);		
			break;
		}
		case TAG_2: {
			setNormalState(lastButton);
			setSelectedState(tag);
			break;
		}
		case TAG_3: {
			setNormalState(lastButton);
			setSelectedState(tag);
			break;
		}
		}
	}
	
	private int lastButton = -1;
	
	public void setSelectedState(int index) {
		if(index != -1 && onItemChangedListener != null)
		{
			if(index > itemList.size())
			{
				throw new RuntimeException("the value of default bar item can not bigger than string array's length");
			}
			itemList.get(index).setSelected(true);
			textList.get(index).setSelected(true);
			onItemChangedListener.onItemChanged(index);
			lastButton = index;
		}
	}
	
	private void setNormalState(int index) {
		if(index != -1)
		{
			if(index > itemList.size())
			{
				throw new RuntimeException("the value of default bar item can not bigger than string array's length");
			}
			itemList.get(index).setSelected(false);
			textList.get(index).setSelected(false);
		}
	}
	
	public interface OnItemChangedListener
	{
		public void onItemChanged(int index);
	}
	
	private OnItemChangedListener onItemChangedListener;
	
	public void setOnItemChangedListener(OnItemChangedListener onItemChangedListener)
	{
	    this.onItemChangedListener = onItemChangedListener;
	}

}
