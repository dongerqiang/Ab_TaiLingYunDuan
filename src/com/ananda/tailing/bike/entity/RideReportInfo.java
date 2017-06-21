package com.ananda.tailing.bike.entity;

import java.io.Serializable;

/**
 * @package com.ananda.tailing.bike.entity
 * @description: 骑行报告
 * @version v1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-5-8 下午2:40:11
 */
public class RideReportInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3787721232430857610L;

	private String longer; // 时长
	private String mileage; // 里程
	private String averageSpeed; // 平均速度
	private String surplusBatery; // 剩余电量
	private int energyRated; // 能耗评级
	private String magnitudeOfCurrent; // 电流量
	private String controlTemper; // 控制器温度
	private String saveTime; // 保存时间
	private int chargingTimes; // 充电次数

	public String getLonger() {
		return longer;
	}

	public void setLonger(String longer) {
		this.longer = longer;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(String averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public String getSurplusBatery() {
		return surplusBatery;
	}

	public void setSurplusBatery(String surplusBatery) {
		this.surplusBatery = surplusBatery;
	}

	public int getEnergyRated() {
		return energyRated;
	}

	public void setEnergyRated(int energyRated) {
		this.energyRated = energyRated;
	}

	public String getMagnitudeOfCurrent() {
		return magnitudeOfCurrent;
	}

	public void setMagnitudeOfCurrent(String magnitudeOfCurrent) {
		this.magnitudeOfCurrent = magnitudeOfCurrent;
	}

	public String getControlTemper() {
		return controlTemper;
	}

	public void setControlTemper(String controlTemper) {
		this.controlTemper = controlTemper;
	}

	public String getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}

	public int getChargingTimes() {
		return chargingTimes;
	}

	public void setChargingTimes(int chargingTimes) {
		this.chargingTimes = chargingTimes;
	}

	@Override
	public String toString() {
		return "RideReportInfo [longer=" + longer + ", mileage=" + mileage
				+ ", averageSpeed=" + averageSpeed + ", surplusBatery="
				+ surplusBatery + ", energyRated=" + energyRated
				+ ", magnitudeOfCurrent=" + magnitudeOfCurrent
				+ ", controlTemper=" + controlTemper + ", saveTime=" + saveTime
				+ ", chargingTimes=" + chargingTimes + "]";
	}

}
