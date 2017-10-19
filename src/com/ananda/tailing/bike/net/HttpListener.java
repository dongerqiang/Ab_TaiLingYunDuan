package com.ananda.tailing.bike.net;

public interface HttpListener<T> {
	public void onStart();
	public void onFinish();
	public void onResult(T result);
	public void onFail(int code);
}
