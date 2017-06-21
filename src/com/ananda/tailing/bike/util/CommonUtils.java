package com.ananda.tailing.bike.util;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ananda.tailing.bike.smartbike.SmartBikeInstance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.text.TextUtils;

/**
 * @package com.ananda.tailing.bike.util
 * @description: 工具类
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-14 上午9:53:32
 */
public class CommonUtils {
	
	public static Stack<Activity> subActivityStack = new Stack<Activity>();
	public static Vibrator vibrator;

	/**
	 * 公共方法 退出应用时，关闭所有的activity
	 */
	public static void finishActivity() {
		Activity activity = null;
		while (!subActivityStack.isEmpty()) {
			activity = subActivityStack.pop();
			if (activity != null) {
				activity.finish();
			}
		}
	}

	/**
	 * 退出应用
	 * 
	 * @param atx
	 */
	public static void ExitActivity(final Activity atx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(atx);
		builder.setTitle("提示").setMessage("确定要退出程序吗!")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						atx.finish();
						finishActivity();// 关闭所有其他activity
						SmartBikeInstance.closeDevice();
						System.exit(0);
					}
				}).setPositiveButton("取消", null).create().show();
	}

	/**
	 * 截取设备返回过来的蓝牙命令
	 * 
	 * @param params
	 * @return
	 */
	public static String getSubString(String params) {
		String result = null;
 		if (!TextUtils.isEmpty(params) && params.startsWith("3")) {
 			result = params.substring(0, params.lastIndexOf("D") + 1);
 		} else {
 			return null; 
 		}
 		return result;
	}
	
	/**
	 * 截取设备返回过来的蓝牙命令
	 * 
	 * @param params
	 * @return
	 */
	public static String getSubMeterString(String params) {
		String result = null;
 		if (!TextUtils.isEmpty(params) && params.startsWith("4")) {
 			result = params.substring(0, params.lastIndexOf("D") + 1);
 		} else {
 			return null; 
 		}
 		return result;
	}

	/**
	 * byte数组转String
	 * @param b
	 * @return
	 */
	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	/**
	 * String转byte数组
	 * @param paramString
	 * @return
	 */
	public static byte[] hexStr2Bytes(String paramString) {
		int i = paramString.length() / 2;
		System.out.println(i);
		byte[] arrayOfByte = new byte[i];
		int j = 0;
		while (true) {
			if (j >= i)
				return arrayOfByte;
			int k = 1 + j * 2;
			int l = k + 1;
			arrayOfByte[j] = (byte) (0xFF & Integer.decode(
					"0x" + paramString.substring(j * 2, k)
							+ paramString.substring(k, l)).intValue());
			++j;
		}
	}

	/**
	 * String乱码转化
	 * @param paramString
	 * @return
	 */
	public static String hexStr2Str(String paramString) {
		char[] arrayOfChar = paramString.toCharArray();
		byte[] arrayOfByte = new byte[paramString.length() / 2];
		int i = 0;
		while (true) {
			if (i >= arrayOfByte.length)
				return new String(arrayOfByte);
			arrayOfByte[i] = (byte) (0xFF & 16
					* "0123456789ABCDEF".indexOf(arrayOfChar[(i * 2)])
					+ "0123456789ABCDEF".indexOf(arrayOfChar[(1 + i * 2)]));
			++i;
		}
	}
	
	/**
	 * 截取密码的前两位
	 * @param context
	 * @return
	 */
	public static int getSubPwd1(Context context, String pwd) {
		System.out.println("保存的密码: -->" + pwd);
		if(!TextUtils.isEmpty(pwd)) {
			return Integer.valueOf(pwd.substring(0, 2));
		} else {
			return 0;
		}
	}
	
	/**
	 * 截取密码的中间两位
	 * @param context
	 * @return
	 */
	public static int getSubPwd2(Context context, String pwd) {
		if(!TextUtils.isEmpty(pwd)) {
			return Integer.valueOf(pwd.substring(2, 4));
		} else {
			return 0;
		}
	}
	
	/**
	 * 截取密码的最后两位
	 * @param context
	 * @return
	 */
	public static int getSubPwd3(Context context, String pwd) {
		if(!TextUtils.isEmpty(pwd)) {
			return Integer.valueOf(pwd.substring(4, 6));
		} else {
			return 0;
		}
	}
	
	/**
	 * 校验返回的蓝牙命令
	 * @param result
	 * @return
	 */
	public static String checkBluetoothReuslt(String result) {
		Pattern p = Pattern.compile("3A\\w*?0D");
		Matcher m = p.matcher(result);
		if(m.find()) {
			return m.group(0);
		}
		return null;
	}
	
	/**
	 * 校验返回的蓝牙命令是否正确
	 * @param result
	 * @return
	 */
	public static String getXorCode(String result) {
		Pattern p = Pattern.compile("\\w*?0D");
		Matcher m = p.matcher(result);
		if(m.find()) {
			return m.group(0);
		}
		return null;
	}
	
	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeMD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * @Description: 将Unicode字符转为中文
	 * @param theString
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
	
	/**
	 * ASCII码转字符串
	 * @param value
	 * @return
	 */
	public static String asciiToString(String value) {
		StringBuffer sbu = new StringBuffer();
		String[] chars = value.split(",");
		for (int i = 0; i < chars.length; i++) {
			sbu.append((char) Integer.parseInt(chars[i]));
		}
		return sbu.toString();
	}

	/**
	 * 返回时间戳
	 * @return
	 */
	public static String getTimestamp() {
		return String.valueOf(new Date().getTime());
	}
	
	/**
	 * 返回当前的时间
	 * @return
	 */
	public static String getDateTime() {
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(date);
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(date);
	}
	
	/**
	 * 计算相差的天数
	 * @param smdate 过去的时间
	 * @param bdate 当前的时间
	 * @return 天数
	 * @throws ParseException
	 */
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	
	/**
	 * 计算时间差
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static float getMistining(String beginTime, String endTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		float f = 0; 
		try {
			java.util.Date now = df.parse(beginTime);
			java.util.Date date = df.parse(endTime);
			long l = now.getTime() - date.getTime();
			f = l / 1000 / 3600f; 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
		
	/**
	 * 计算行驶的平均速度
	 * @param mi
	 * @param time
	 * @return
	 */
	public static String getAverageSpeed(int mi, float time) {
		DecimalFormat df = new DecimalFormat("#.0");
		return df.format(mi / time);
	}
	
	/**
	 * 计算行驶时间
	 * @param time
	 * @return
	 */
	public static String getTravelTime(float time) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(0);
		return numberFormat.format(time * 60);
	}
	
	/**
	 * 检查是否有可用的网络
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkNormal(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

}
