package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Session_request;
import com.example.demo.dto.Session_response;
import com.example.demo.service.Session_service;

@RestController
@RequestMapping("/session")
public class Session_controller {

    private final Session_service sessionService;

    @Autowired
    public Session_controller(Session_service sessionService) {
        this.sessionService = sessionService;
    }

    // セッション作成エンドポイント
    @PostMapping("/create")
    public Session_response createSession(@RequestBody Session_request request) {
        boolean isSuccess = sessionService.createSession(request);
        if (isSuccess) {
            return new Session_response(true, "セッション作成成功");
        } else {
            return new Session_response(false, "セッション作成失敗");
        }
    }

    // セッション参加エンドポイント
    @PostMapping("/join")
    public Session_response joinSession(@RequestBody Session_request request) {
        boolean isSuccess = sessionService.joinSession(request);
        if (isSuccess) {
            return new Session_response(true, "セッション参加成功");
        } else {
            return new Session_response(false, "合言葉が一致しません");
        }
    }
}