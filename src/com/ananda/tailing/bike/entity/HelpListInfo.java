package com.ananda.tailing.bike.entity;

import java.io.Serializable;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * @package com.ananda.tailing.bike.entity
 * @description:  ARS
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-5 下午3:56:50
 */
public class HelpListInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gzid;
	private String gzmc;
	private List<HelpInfoEntity> data;

	public String getGzid() {
		return gzid;
	}

	public void setGzid(String gzid) {
		this.gzid = gzid;
	}

	public String getGzmc() {
		return gzmc;
	}

	public void setGzmc(String gzmc) {
		this.gzmc = gzmc;
	}

	public List<HelpInfoEntity> getData() {
		return data;
	}

	public void setData(List<HelpInfoEntity> data) {
		this.data = data;
	}

}
