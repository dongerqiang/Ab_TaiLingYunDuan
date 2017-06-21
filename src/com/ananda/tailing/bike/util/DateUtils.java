package com.ananda.tailing.bike.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
    //把long型时间转换成指定格式时间
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }
    //把long型时间转换成默认格式时间
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }
    //获取系统当前时间戳
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }
    //获取（默认时间格式的）系统当前时间戳
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }
    //获取（指定时间格式的）系统当前时间戳
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
	public static String currentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(date);
		return currentDate;
	}
	//转换时间为unixtime
	public static String getLongTime(String time){
		String alltime=DateUtils.currentDate()+" "+time;
		String result="";
		try {
			long longtime=new SimpleDateFormat("yyyy-MM-dd").parse(alltime).getTime();
			result=longtime/1000+"";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		};
		return result;
	}
	public static String getdate(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(time*1000));		
	}
	public static String getDotDate(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(new Date(time*1000));		
	}
	

}
