package com.ananda.tailing.bike.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ananda.tailing.bike.entity.RideReportInfo;

/**  
 * @package com.ananda.tailing.bike.db 
 * @description: 数据库语句操作
 * @version v1.0  
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-8 下午2:28:46 
 */
public class SQLUtil {
	
	/**
	 * 保存骑行报告数据
	 * @param context
	 * @param rideReportInfo
	 */
	public static void saveRideReport(Context context, RideReportInfo rideReportInfo) {
		DBService dbService = new DBService(context);
		dbService.open();
		ContentValues contentValues = new ContentValues();
		contentValues.put("longer", rideReportInfo.getLonger());
		contentValues.put("mileage", rideReportInfo.getMileage());
		contentValues.put("average_speed", rideReportInfo.getAverageSpeed());
		contentValues.put("surplus_batery", rideReportInfo.getSurplusBatery());
		contentValues.put("energy_rated", rideReportInfo.getEnergyRated());
		contentValues.put("magnitude_of_current", rideReportInfo.getMagnitudeOfCurrent());
		contentValues.put("control_temper", rideReportInfo.getControlTemper());
		contentValues.put("save_time", rideReportInfo.getSaveTime());
		dbService.insertStepTb(DBState.TABLE_RIDEREPORT, contentValues);
		dbService.close();
	}
	
	/**
	 * 查询骑行报告一周内的数据
	 * @param dbService
	 * @return
	 */
	public static List<RideReportInfo> queryRideReport(DBService dbService) {
		String sql = "select * from " + DBState.TABLE_RIDEREPORT + " where DATEDIFF(CURDATE(), save_time) < 7";
		Cursor resCursor = dbService.rawQuery(sql, null);
		if (resCursor != null) {
			List<RideReportInfo> listRide = new ArrayList<RideReportInfo>();
			while(resCursor.moveToNext()){
				RideReportInfo rideReportInfo = new RideReportInfo(); 
				rideReportInfo.setLonger(resCursor.getString(1));
				rideReportInfo.setMileage(resCursor.getString(2));
				rideReportInfo.setAverageSpeed(resCursor.getString(3));
				rideReportInfo.setSurplusBatery(resCursor.getString(4));
				rideReportInfo.setEnergyRated(resCursor.getInt(5));
				rideReportInfo.setMagnitudeOfCurrent(resCursor.getString(6));
				rideReportInfo.setControlTemper(resCursor.getString(7));
				rideReportInfo.setSaveTime(resCursor.getString(8)); 
			}
			return listRide;
		}
		return new ArrayList<RideReportInfo>();
	}
	
}
