package com.example.demo.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class Session_cleanup_service {

    // セッションメタ情報を保持しているMapを注入または共有する必要があります
    private final ConcurrentHashMap<String, Object> sessionMap;

    // コンストラクタでセッションマップを受け取る
    public Session_cleanup_service(ConcurrentHashMap<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    // セッションをUUIDで明示的に削除する
    public boolean cleanup(String uuid) {
        if (sessionMap.remove(uuid) != null) {
            System.out.println("セッション [" + uuid + "] を削除しました。");
            return true;
        } else {
            System.out.println("セッション [" + uuid + "] は存在しません。");
            return false;
        }
    }

    // 期限切れや不正アクセスなどにより呼ばれる場合の共通処理
    public void cleanupIfPresent(String uuid) {
        if (sessionMap.containsKey(uuid)) {
            sessionMap.remove(uuid);
            System.out.println("期限切れまたは異常検知によりセッション [" + uuid + "] を削除しました。");
        }
    }
}
