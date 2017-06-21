/**
 * 
 */
package com.ananda.tailing.bike.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @package com.ananda.tailing.bike.util
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午2:07:29
 */
public class MyToast {

	public static void showShortToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
}
