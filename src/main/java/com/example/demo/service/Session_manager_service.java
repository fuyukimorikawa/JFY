package com.example.demo.service;


import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Session_request;
import com.example.demo.entity.Session_metadata;
import com.example.demo.exception.Invalid_session_exception;
import com.example.demo.exception.Session_not_found_exception;

@Service
public class Session_manager_service {

    // UUID → セッション情報（ハッシュ済みパスワード、有効期限など）
    private final Map<String, Session_metadata> sessionMap = new ConcurrentHashMap<>();

    // セッション有効期限（秒単位）：ここでは10分
    private static final long SESSION_TTL_SECONDS = 600;

    private final Session_cleanup_service sessionCleanupService;

    public Session_manager_service(Session_cleanup_service sessionCleanupService) {
        this.sessionCleanupService = sessionCleanupService;
    }
    
    public Map<String, Session_metadata> getSessionMap() {
        return sessionMap;
    }
    

    // セッション作成処理
    public boolean createSession(Session_request request, Session_auth_service authService) {
        String uuid = (request.getUuid() != null) ? request.getUuid() : UUID.randomUUID().toString();

        if (sessionMap.containsKey(uuid)) {
            throw new Invalid_session_exception("セッションは既に存在します。");
        }

        String hashedPassword = authService.hashPassword(request.getPassword());
        Instant expiresAt = Instant.now().plusSeconds(SESSION_TTL_SECONDS);

        sessionMap.put(uuid, new Session_metadata(hashedPassword, expiresAt));
        return true;
    }

    // セッション参加処理
    public boolean joinSession(Session_request request, Session_auth_service authService) {
        Session_metadata metadata = sessionMap.get(request.getUuid());

        if (metadata == null) {
            throw new Session_not_found_exception("セッションが見つかりません。");
        }

        // 有効期限チェック
        if (Instant.now().isAfter(metadata.getExpiresAt())) {
            sessionCleanupService.cleanup(request.getUuid()); // 有効期限切れの場合、クリーンアップ
            throw new Invalid_session_exception("セッションの有効期限が切れています。");
        }

        // 合言葉の照合
        if (!authService.matches(request.getPassword(), metadata.getPasswordHash())) {
            throw new Invalid_session_exception("合言葉が一致しません。");
        }

        // セッション延長処理（通話中に有効期限を延長する）
        extendSession(request.getUuid());

        return true;
    }

    // セッション有効期限を延長
    public void extendSession(String uuid) {
        Session_metadata metadata = sessionMap.get(uuid);
        if (metadata != null) {
            Instant extendedExpiry = Instant.now().plusSeconds(SESSION_TTL_SECONDS);
            metadata.setExpiresAt(extendedExpiry);
        }
    }

    // セッションを内部的に削除（Session_cleanup_serviceから呼び出される想定）
    public boolean removeSession(String uuid) {
        return sessionMap.remove(uuid) != null;
    }

    // セッション存在チェック（任意のユースケース向け）
    public boolean exists(String uuid) {
        return sessionMap.containsKey(uuid);
    }
}
