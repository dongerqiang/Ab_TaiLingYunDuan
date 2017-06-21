package com.ananda.tailing.bike.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @package com.ananda.tailing.bike.entity
 * @description: ARS
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-5 下午3:57:18
 */
public class HelpInfoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String content;
	@JSONField(name="imgpath")
	private String imgpath;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JSONField(name="imgpath")
	public String getImageurl() {
		return imgpath;
	}
	
	@JSONField(name="imgpath")
	public void setImageurl(String imgpath) {
		this.imgpath = imgpath;
	}

}
