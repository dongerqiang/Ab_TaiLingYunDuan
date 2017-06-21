package com.ananda.tailing.bike.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @package com.ananda.tailing.bike.entity
 * @description: 修改用户信息
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午4:56:01
 */
public class EditUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private String UserId;
	private String Sex;
	private String Birthday;
	private String Job;
	private String Use;
	private String Idintity;
	private String Mobile;
	private String Timestamp;

	@JSONField(name = "UserId")
	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	@JSONField(name = "Sex")
	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	@JSONField(name = "Birthday")
	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	@JSONField(name = "Job")
	public String getJob() {
		return Job;
	}

	public void setJob(String job) {
		Job = job;
	}

	@JSONField(name = "Use")
	public String getUse() {
		return Use;
	}

	public void setUse(String use) {
		Use = use;
	}

	@JSONField(name = "Idintity")
	public String getIdintity() {
		return Idintity;
	}

	public void setIdintity(String idintity) {
		Idintity = idintity;
	}

	@JSONField(name = "Mobile")
	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	@JSONField(name = "Timestamp")
	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

}
