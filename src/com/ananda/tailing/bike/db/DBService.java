package com.ananda.tailing.bike.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @package com.ananda.tailing.bike.db
 * @description: 数据库管理
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-2-11 下午5:29:12
 */
public class DBService {
	
	private final int DATABASE_VERSION = 1;
	
	// 执行open()打开数据库时，保存返回的数据库对象
	private SQLiteDatabase sqliteDatabase = null;

	// 继承SQLiteOpenHelper
	private DatabaseHelper databaseHelper = null;

	// 本地context对象
	private Context localContext = null;
	
	// 内部类
	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			/**
			 * 当调用getWriteableDatabase() 或者getReadableDatabase()方法的时候创建一个数据库
			 */
			super(context, DBState.DB_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * 创建表
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO 当数据库中没有表的时候，创建一个表
			db.beginTransaction(); // 打开事务
			try{
				// 创建骑行报告表
				String ride_report_tab_sql = "CREATE TABLE "
						+ DBState.TABLE_RIDEREPORT + " ("
						+ "id INTEGER PRIMARY KEY autoincrement," 
						+ "period_of_time TEXT,"         // 时段
						+ "longer TEXT,"                 // 时长
						+ "mileage TEXT,"                // 里程
				 		+ "average_speed TEXT,"          // 平均速度
						+ "surplus_batery TEXT,"         // 剩余电量
						+ "energy_rated INTEGER,"        // 能耗评级
						+ "magnitude_of_current TEXT,"   // 电流量
						+ "control_temper TEXT"          // 控制器温度
						+ "save_time TEXT);";            // 保存时间
				db.execSQL(ride_report_tab_sql); 
				db.setTransactionSuccessful(); // 设置提交事务，如果不设置的话，会自动回滚
			} finally {
				db.endTransaction(); // 由事务的标志决定是提交事务，还是回滚事务
			}
			System.out.println("onCreate----数据库创建成功");			
			
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
	}

	// 构造方法，取得一个context
	public DBService(Context context) {
		localContext = context;
	}
	
	// 打开数据库，返回一个数据库实例对象
	public void open() {
		try {
			databaseHelper = new DatabaseHelper(localContext);
			sqliteDatabase = databaseHelper.getWritableDatabase();
		} catch (SQLiteException sqliteEx) {
			System.out.println("创建数据库失败!");
		}
	}

	// 关闭数据库
	public void close() {
		sqliteDatabase.close();
	}

	/**
	 * 执行数据查询操作
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public Cursor rawQuery(String sql, String[] args) {
		if (sqliteDatabase == null) {
			sqliteDatabase = databaseHelper.getReadableDatabase();
		}
		return sqliteDatabase.rawQuery(sql, args);
	}
	
	/**
	 * 执行数据插入操作
	 * @param contentValues
	 * @return
	 */
	public int insertContentTb(String tableName, ContentValues contentValues) {
		long res = -1;
		sqliteDatabase.beginTransaction();
		res = sqliteDatabase.insert(tableName, null, contentValues);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return (int) res;
	}
	
	/**
	 * 执行数据插入操作
	 * @param contentValues
	 */
	public void insertStepTb(String tableName, ContentValues contentValues) {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.insert(tableName, null, contentValues);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	
	/**
	 * 执行更新数据操作
	 * @param values
	 * @param contentId
	 * @return
	 */
	public int updateContentTb(String tableName, ContentValues values, String contentId) {
		if (sqliteDatabase == null) {
			sqliteDatabase = databaseHelper.getReadableDatabase();
		}
		return sqliteDatabase.update(tableName, values, "_id=?",
				new String[] { contentId });
	}

	/**
	 * 执行更新数据操作
	 * @param values
	 * @param contentId
	 * @return
	 */
	public int updateStepTb(String tableName, ContentValues values, String contentId) {
		if (sqliteDatabase == null) {
			sqliteDatabase = databaseHelper.getReadableDatabase();
		}
		return sqliteDatabase.update(tableName, values,
				"content_id=?", new String[] { contentId });
	}

	/**
	 * 执行删除数据操作
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int deleteItemById(String table, String whereClause,
			String[] whereArgs) {
		if (sqliteDatabase == null) {
			sqliteDatabase = databaseHelper.getReadableDatabase();
		}
		return sqliteDatabase.delete(table, whereClause, whereArgs);
	}
	
	/**
	 * 清除表
	 * 
	 * @param tableName
	 */
	public void clearTable(String tableName) {
		if (sqliteDatabase == null) {
			sqliteDatabase = databaseHelper.getReadableDatabase();
		}
		sqliteDatabase.execSQL("delete from " + tableName);
	}
	
}
