package com.ananda.tailing.bike.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ananda.tailing.bike.entity.ConsulationInfo;
import com.ananda.tailing.bike.entity.HelpListInfo;
import com.ananda.tailing.bike.entity.UserInfo;

/**
 * @package com.ananda.tailing.bike.util
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-5 下午3:22:59
 */
public class JsonPaserUtils {
	
	/**
	 * 解析获取ARS数据对象
	 * @param jsonArrayStr
	 * @return
	 */
	public static List<HelpListInfo> parseJsonToArsInfo(String jsonArrayStr){
		return JSONObject.parseArray(jsonArrayStr, HelpListInfo.class);
	}
	
	/**
	 * 解析获取用户信息
	 * @param jsonArrayStr
	 * @return
	 */
	public static UserInfo parseJsonToUserInfo(String jsonArrayStr) {
		return JSONObject.parseObject(jsonArrayStr, UserInfo.class);
	}
	
	/**
	 * 解析获取咨询信息
	 * @param jsonArrayStr
	 * @return
	 */
	public static List<ConsulationInfo> parseJsonToConsultationInfo(String jsonArrayStr) {
		return JSONObject.parseArray(jsonArrayStr, ConsulationInfo.class);
	}

}
