package com.ananda.tailing.bike.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @package com.ananda.tailing.bike.entity
 * @description: 找回密码
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午4:53:49
 */
public class LookForPwdInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Idintity;
	private String Mobile;
	private String Name;
	private String Timestamp;

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

	@JSONField(name = "Name")
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}

	@JSONField(name = "Timestamp")
	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

}
