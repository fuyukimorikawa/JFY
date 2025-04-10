package com.example.demo.exception;
/*
 * 無効なセッションIDや合言葉に対する例外処理
 */
public class Invalid_session_exception extends RuntimeException{
	public Invalid_session_exception(String message) {
		super(message);
	}
}
