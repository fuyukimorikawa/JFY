package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.dto.Session_response;

@ControllerAdvice
public class Global_exception_handler {
	@ExceptionHandler(Invalid_session_exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Session_response handle_invalid_session_exception(Invalid_session_exception ex) {
		return new Session_response(false, ex.getMessage());	
	}
	@ExceptionHandler(Session_not_found_exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Session_response handle_session_not_found_exception(Session_not_found_exception ex) {
        return new Session_response(false, ex.getMessage());
    }

    // 予期しないエラーのための汎用的なハンドラー
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Session_response handle_generic_exception(Exception ex) {
        return new Session_response(false, "予期しないエラーが発生しました");
    }
}
