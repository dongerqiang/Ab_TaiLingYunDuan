/**
 * 
 */
package com.ananda.tailing.bike.entity;

import java.io.Serializable;

/**
 * @package com.ananda.tailing.bike.entity
 * @description:
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午7:16:00
 */
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private String Name;
	private String Sex;
	private String Birthday;
	private String Job;
	private String Use;
	private String Idintity;
	private String Mobile;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getJob() {
		return Job;
	}

	public void setJob(String job) {
		Job = job;
	}

	public String getUse() {
		return Use;
	}

	public void setUse(String use) {
		Use = use;
	}

	public String getIdintity() {
		return Idintity;
	}

	public void setIdintity(String idintity) {
		Idintity = idintity;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

}
