package com.ananda.tailing.bike.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @package com.ananda.tailing.bike.entity
 * @description: 修改密码
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午4:50:50
 */
public class AmendPwdInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String UserId;
	private String Password;
	private String NewPassword;
	private String Timestamp;

	@JSONField(name = "UserId")
	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	@JSONField(name = "Password")
	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	@JSONField(name = "NewPassword")
	public String getNewPassword() {
		return NewPassword;
	}

	public void setNewPassword(String newPassword) {
		NewPassword = newPassword;
	}

	@JSONField(name = "Timestamp")
	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

}
