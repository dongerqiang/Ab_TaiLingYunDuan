package com.ananda.tailing.bike.activity;

import java.util.Locale;

import org.apache.http.Header;

import com.ananda.tailing.bike.activity.more.AlarmReceiver;
import com.ananda.tailing.bike.util.CommonUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * @package com.ananda.tailing.bike.activity
 * @description:
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-5 下午6:22:01
 */
public abstract class BaseActivity extends Activity {

	protected ProgressDialog progressDialog;
	protected MyApplication myApplication;
	
	protected int pwd1 = 0;
	protected int pwd2 = 0;
	protected int pwd3 = 0;

	private AlarmReceiver alarmReceiver;
	protected abstract void initWidget();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
		initWidget();
		registReceiver();
		myApplication = (MyApplication) getApplication();
	}
	
	protected void registReceiver(){
		IntentFilter alarmFilter = new IntentFilter(RomtorActivity.ALARM_TAG);
		alarmReceiver = new AlarmReceiver(this);
		this.registerReceiver(alarmReceiver, alarmFilter);
	}

	public void onDestroy(){
		if(alarmReceiver!=null){
			this.unregisterReceiver(alarmReceiver);
		}
		super.onDestroy();
	}
	
	protected void showProgressDialog(Context context, String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
		}
		progressDialog.setCancelable(true);
		progressDialog.setMessage(msg);
		progressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	protected final String debugStatusCode(String TAG, int statusCode) {
		String msg = String.format(Locale.US, "Return Status Code: %d",
				statusCode);
		Log.d(TAG, msg);
		return msg;
	}

	protected final String debugResponse(String TAG, String response) {
		if (response != null) {
			Log.d(TAG, "Response data:");
			Log.d(TAG, response);
			return response;
		}
		return null;
	}
	
	 protected final String debugHeaders(String TAG, Header[] headers) {
	        if (headers != null) {
	            Log.d(TAG, "Return Headers:");
	            StringBuilder builder = new StringBuilder();
	            for (Header h : headers) {
	                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
	                Log.d(TAG, _h);
	                builder.append(_h);
	                builder.append("\n");
	            }
	            return builder.toString();
	        }
	        return null;
	    }

}
