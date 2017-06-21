package com.ananda.tailing.bike.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * @package com.android.activity.shake
 * @description:
 * @version v1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-29 下午2:11:53
 */
public class VibratorUtil {

	public static Vibrator vib;

	/**
	 * final Activity activity ：调用该方法的Activity实例 long milliseconds ：震动的时长，单位是毫秒
	 * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */

	public static void Vibrate(final Activity activity, long milliseconds) {
		vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public static void Vibrate(final Activity activity, long[] pattern,
			boolean isRepeat) {
		vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}

	/**
	 * 取消震动
	 */
	public static void CancelVibrate() {
		if(vib.hasVibrator()) {
			vib.cancel();
		}
	}
	
	/**
	 * 设置震动
	 * 另外一种实现方法
	 * @param context
	 */
	public static void playVibrator(Context context) {
		/*
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 */
		vib = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		// 重复两次上面的pattern 如果只想震动一次，index设为-1
		if (vib != null) {
			vib.vibrate(new long[] {100,10,100,2000}, -1);
		}
	}

	
	// 调用方法
	//VibratorUtil.Vibrate(ShakeActivity.this, new long[] {100,10,100,2000}, false);   
	//playVibrator(ShakeActivity.this);
}
