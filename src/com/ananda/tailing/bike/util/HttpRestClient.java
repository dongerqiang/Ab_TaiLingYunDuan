package com.ananda.tailing.bike.util;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @package com.ananda.tailing.bike.util
 * @description: 网络请求连接管理类
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-6 下午5:14:10
 */
public class HttpRestClient {
	
	private static final String BASE_URL = "http://61.177.137.82:3888/";
//	private static final String BASE_URL = "http://tl.adinnet.cn/";
	
	private static AsyncHttpClient httpClient = new AsyncHttpClient();
	
	static {  
		httpClient.setTimeout(10000);  
    }
				
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		httpClient.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		httpClient.get(getAbsoluteUrl(url), responseHandler);
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		httpClient.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(Context context, String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
		httpClient.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
