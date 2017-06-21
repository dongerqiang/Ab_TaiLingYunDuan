package com.ananda.tailing.bike.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @package com.ananda.tailing.bike.entity
 * @description:  注册用户
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午4:11:46
 */
public class RegisterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Nmae;
	private String Idintity;
	private String Mobile;
	private String Password;
	private String Sex;
	private String Birthday;
	private String Job;
	private String Use;
	private String Timestamp;

	@JSONField(name = "Name")
	public String getNmae() {
		return Nmae;
	}

	public void setNmae(String nmae) {
		Nmae = nmae;
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

	@JSONField(name = "Password")
	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
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

	@JSONField(name = "Timestamp")
	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

}
