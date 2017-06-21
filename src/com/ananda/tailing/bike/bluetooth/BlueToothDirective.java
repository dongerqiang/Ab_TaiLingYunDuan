package com.ananda.tailing.bike.bluetooth;

/**
 * @package com.ananda.tailing.bike.bluetooth
 * @description: 蓝牙命令协议
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-24 下午4:13:31
 * 手机发送数据格式: 0x3A + pw1 + pw2 + pw3 + 遥控代码 + 校验  + 0x0D
 */
public class BlueToothDirective {
	
	/**
	 * 设置密码, 前面3位是旧密码, 后面3位是新密码
	 * @param opwd1
	 * @param opwd2
	 * @param opwd3
	 * @param npwd1
	 * @param npwd2
	 * @param npwd3
	 * @return
	 */
	public static String sentSettingPwd(int opwd1, int opwd2, int opwd3,
			int npwd1, int npwd2, int npwd3) {
		byte[] a = new byte[10];
		a[0] = 0x3A;        // 起始符
		a[1] = 0x43;
		a[2] = (byte) opwd1;
		a[3] = (byte) opwd2;        // 初始密码1
		a[4] = (byte) opwd3;        // 初始密码2
		a[5] = (byte) npwd1; // 设定密码1
		a[6] = (byte) npwd2; // 设定密码2
		a[7] = (byte) npwd3; // 设定密码3
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6] + a[7]) & 0xff); // 校验
		a[9] = 0x0D;   // 终止符
		return transmitEncoding(a, a.length);
	}
		
	/**
	 * 解锁开电命令: 0x01解锁
	 * @return
	 */
	public static String sentLocking(int pwd1, int pwd2, int pwd3, int mode, int mswitch) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x54;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x01;
		a[6] = (byte) mode;
		a[7] = (byte) mswitch;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);
	}
	
	/**
	 * 上锁断电命令：0x02上锁
	 * @return
	 */
	public static String sentUnLocking(int pwd1, int pwd2, int pwd3, int mode, int mswitch) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x54;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x02;
		a[6] = (byte) mode;
		a[7] = (byte) mswitch;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);
	}
	
	/**
	 * 寻车警报 0x05
	 * @param pwd1
	 * @param pwd2
	 * @param pwd3
	 * @param mode 三档模式
	 * @param mswitch 0:遥控界面; 1:仪表界面
	 * @return
	 */
	public static String sentAlarm(int pwd1, int pwd2, int pwd3, int mode, int mswitch) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x54;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x05;
		a[6] = (byte) mode;
		a[7] = (byte) mswitch;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);
	}
	
	/**
	 * 打开座桶 0x03
	 * @param pwd1
	 * @param pwd2
	 * @param pwd3
	 * @return
	 */
	public static String sentBarrelRide(int pwd1, int pwd2, int pwd3, int mode, int mswitch) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x54;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x03;
		a[6] = (byte) mode;
		a[7] = (byte) mswitch;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);
	}
	
	
	
	/**
	 * 获取电量和转把速度
	 * @return
	 */
	public static String getRideAndBattery(int pwd1, int pwd2, int pwd3, int mode, int mswitch) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x54;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x00;
		a[6] = (byte) mode;
		a[7] = (byte) mswitch;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);
	}
	
	/**
	 * ARS修复
	 * @param pwd1
	 * @param pwd2
	 * @param pwd3
	 * @param mode
	 * @return
	 */
	public static String sentRepairArs(int pwd1, int pwd2, int pwd3, int mode) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x65;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x08;
		a[6] = (byte) mode;
		a[7] = 0x01;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);		
	}
	
	/**
	 * 修复电量
	 * @param pwd1
	 * @param pwd2
	 * @param pwd3
	 * @param mode
	 * @return
	 */
	public static String sentRepairBatery(int pwd1, int pwd2, int pwd3, int mode) {
		byte[] a = new byte[10];
		a[0] = 0x3A;
		a[1] = 0x65;
		a[2] = (byte) pwd1;
		a[3] = (byte) pwd2;
		a[4] = (byte) pwd3;
		a[5] = 0x09;
		a[6] = (byte) mode;
		a[7] = 0x01;
		a[8] = (byte) ((a[1] + a[2] + a[3] + a[4] + a[5] + a[6]+ a[7]) & 0xff);
		a[9] = 0x0D;
		return transmitEncoding(a, a.length);		
	}
	
	/**
	 * 发送的报文
	 * 
	 * @param sendByte
	 * @param byteLength
	 * @return
	 */
	public static String transmitEncoding(byte[] sendByte, int byteLength) {
		byte[] transByte = new byte[byteLength * 2 + 2];
		byte tmp;
		for (int i = 0; i < byteLength; i++) {
			if (sendByte[i] < 0) {
				tmp = 0;
				// 无符号
				tmp = (byte) ((byte) sendByte[i] & 0x7f);

				tmp >>= 4;
				tmp |= 0x08;
				if ((tmp >= 0x00) && (tmp <= 0x09)) {
					transByte[i * 2 + 1] = (byte) (tmp + '0');
				} else if ((tmp >= 0x0a) && (tmp <= 0x0f)) {
					transByte[i * 2 + 1] = (byte) (tmp - 0x0a + 'a');
				}

				tmp = 0;
				tmp = (byte) (sendByte[i] & 0x0f);
				if ((tmp >= 0) && (tmp <= 9)) {
					transByte[i * 2 + 2] = (byte) (tmp + '0');
				} else if ((tmp >= 0x0a) && (tmp <= 0x0f)) {
					transByte[i * 2 + 2] = (byte) (tmp - 0x0a + 'a');
				}
			} else {
				tmp = 0;
				tmp = (byte) (sendByte[i] >> 4);

				if ((tmp >= 0x00) && (tmp <= 0x09)) {
					transByte[i * 2 + 1] = (byte) (tmp + '0');
				} else if ((tmp >= 0x0a) && (tmp <= 0x0f)) {
					transByte[i * 2 + 1] = (byte) (tmp - 0x0a + 'a');
				}

				tmp = (byte) (sendByte[i] & 0x0f);
				if ((tmp >= 0) && (tmp <= 9)) {
					transByte[i * 2 + 2] = (byte) (tmp + '0');
				} else if ((tmp >= 0x0a) && (tmp <= 0x0f)) {
					transByte[i * 2 + 2] = (byte) (tmp - 0x0a + 'a');
				}
			}
		}
		String str = new String(transByte);
		return str.substring(1, str.length() - 1); 
	}

}
