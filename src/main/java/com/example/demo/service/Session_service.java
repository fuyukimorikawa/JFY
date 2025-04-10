package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Session_request;

@Service
public class Session_service {

    private final Session_manager_service sessionManagerService;
    private final Session_auth_service sessionAuthService;
    private final Session_cleanup_service sessionCleanupService;

    public Session_service(Session_auth_service authService,
                           Session_manager_service managerService,
                           Session_cleanup_service cleanupService) {
        this.sessionAuthService = authService;
        this.sessionManagerService = managerService;
        this.sessionCleanupService = cleanupService;
    }

    // セッション作成：UUID生成・ハッシュ化・保存
    public boolean createSession(Session_request request) {
        // ここで sessionManagerService と sessionAuthService を使用
        return sessionManagerService.createSession(request, sessionAuthService);
    }

    // セッション参加：UUIDチェック・ハッシュ照合・有効期限確認
    public boolean joinSession(Session_request request) {
        // ここで sessionManagerService, sessionAuthService, sessionCleanupService を使用
        return sessionManagerService.joinSession(request, sessionAuthService);
    }

    // 通話終了：明示的なセッションクリーンアップ
    public boolean endSession(String uuid) {
        // ここで sessionCleanupService を使用
        return sessionCleanupService.cleanup(uuid);
    }
}
