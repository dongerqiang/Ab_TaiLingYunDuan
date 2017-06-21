package com.ananda.tailing.bike.bluetooth;

/**
 * @package com.ananda.tailing.bike.util
 * @description: 蓝牙消息类型
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-14 上午9:53:06
 */
public class BlueToothMsg {

	// 类型的消息发送从BluetoothChatservice处理程序
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final String TOAST = "toast";
	public static final String DEVICE_NAME = "device_name";
	
	public static final int ROMTOR = 0;  // 遥控
	public static final int METER = 1;   // 仪表
	public static final int CITY_MODE = 1;   // 城市模式
	public static final int MOUNT_MODE = 2;  // 山地模式
	public static final int TRACK_MODE = 3;  // 赛道模式
	
}
