package com.ananda.tailing.bike.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.view.FragmentIndicator;
import com.ananda.tailing.bike.view.FragmentIndicator.OnItemChangedListener;
import com.ananda.tailing.bike.view.TitleBarView;

/**
 * 首页(已放弃)
 * @package com.ananda.tailing.bike.activity
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-7 下午12:26:03
 */
public class MainActivity extends FragmentActivity {
	
	private FragmentIndicator mFragmentAction;
	private TitleBarView mTitleBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		CommonUtils.subActivityStack.add(this);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mFragmentAction = (FragmentIndicator) findViewById(R.id.bottom_bar_layout);
		mFragmentAction.setOnItemChangedListener(new OnItemChangedListener() {
			
			@Override
			public void onItemChanged(int index) {
				// TODO Auto-generated method stub
				showContent(index);
				mTitleBarView.showTitle(index);
			}
		});
		mFragmentAction.setSelectedState(0);
		mTitleBarView.showTitle(0);
				
	}
	
	/**
	 * 显示不同的View
	 * @param index
	 */
	private void showContent(int index) {
		Fragment details = (Fragment)
	            getSupportFragmentManager().findFragmentById(R.id.content);
		switch(index) {
		case 0: {
			//details = new RomtorActivity();  // 遥控
			break;
		}
		case 1: {
			//details = new MeterActivity();   // 仪表
			break;
		}
		case 2: {
			//details = new ARSActivity();     // ARS
			break;
		}
		case 3: {
			//details = new MoreActivity();    // 更多
			break;
		}
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
	}
			
	public void onBackPressed() {
		CommonUtils.ExitActivity(this);
	}
	
}
