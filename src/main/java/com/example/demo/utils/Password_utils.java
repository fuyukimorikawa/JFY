package com.example.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password_utils {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // パスワードをハッシュ化
    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // パスワードとハッシュを照合
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
