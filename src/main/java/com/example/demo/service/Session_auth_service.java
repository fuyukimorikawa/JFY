package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.utils.Password_utils;

@Service
public class Session_auth_service {

    // 合言葉をBCryptでハッシュ化
    public String hashPassword(String rawPassword) {
        return Password_utils.encodePassword(rawPassword);
    }

    // 合言葉がハッシュと一致するかを検証
    public boolean matches(String rawPassword, String encodedPassword) {
        return Password_utils.matches(rawPassword, encodedPassword);
    }
}
