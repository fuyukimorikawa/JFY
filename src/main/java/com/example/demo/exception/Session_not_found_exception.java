package com.example.demo.exception;
/*
 * 存在しないセッションUUIDにアクセスしたときの例外処理
 */
public class Session_not_found_exception extends RuntimeException{
	public Session_not_found_exception(String message) {
		super(message);
	}
}
