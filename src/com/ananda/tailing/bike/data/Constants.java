package com.ananda.tailing.bike.data;

public class Constants {
	private static final String Server="http://60.190.223.145:13080/";
//	private static final String Server="http://192.168.2.145:13080/";
	
	//获得验证码
	public static final String PIN_CODE_URL = Server+"v1/user/getVerificationCode";
	//登录
	public static final String LOGIN_URL=Server+"v1/user/login";
	//上电
	public static final String START_URL = Server+"v1/ops/startBike/%s";
	//断电
	public static final String STOP_URL = Server+"v1/ops/closeBike/%s";
	//上锁
	public static final String LOCK_URL = Server+"v1/ops/lockBike/%s";
	//解锁
	public static final String UNLOCK_URL = Server+"v1/ops/unlockBike/%s";
	//开始寻车
	public static final String FIND_CAR_START_URL = Server+"v1/ops/seekStart/%s";
	//结束寻车
	public static final String FIND_CAR_STOP_URL = Server+"v1/ops/seekEnd/%s";
	//设置灵敏度
	public static final String SET_SENSITIVE_URL=Server+"v1/ops/setSensitivity";
	//获取车辆详细信息
	public static final String BIKE_DETAIL_URL =Server+"v1/bike/getBikeDetail/%s";
	//故障检测
	public static final String ERROR_CHECK_URL = Server+"v1/ops/faultDetect";
	//查询轨迹
	public static final String RUN_PATH_URL = Server+"v1/bike/getTrackByTime/%s";
	//设置车辆别名
	public static final String ALIAS_SET_URL = Server+"v1/bike/alias";
	//绑定手机号和imei
	public static final String BIND_PHONE_AND_IEMI_URL = Server+"v1/user/bindUserImei";
	
}
