package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.Session_request;
import com.example.demo.entity.Session_metadata;
import com.example.demo.exception.Invalid_session_exception;

@ExtendWith(MockitoExtension.class)  // Mockitoの拡張機能を使う場合
public class Session_service_test {

    @Mock
    private Session_manager_service sessionManagerService;

    @Mock
    private Session_auth_service sessionAuthService;

    @Mock
    private Session_cleanup_service sessionCleanupService;

    @InjectMocks
    private Session_service sessionService;  // テスト対象クラス

    @Test
    void testJoinSession() {
        // モックの設定
        Session_request request = new Session_request("test-uuid", "password");

        // モックがjoinSessionを正しく呼ぶことを検証
        doReturn(true).when(sessionManagerService).joinSession(request, sessionAuthService);

        // 実際にメソッドを呼び出す
        sessionService.joinSession(request);

        // メソッドが1回呼ばれたことを検証
        verify(sessionManagerService).joinSession(request, sessionAuthService);
    }

    @Test
    void testCreateSession() {
        Session_request request = new Session_request("test-uuid", "password");

        // モックがcreateSessionを正しく呼ぶことを検証
        doReturn(true).when(sessionManagerService).createSession(request, sessionAuthService);

        // 実際にメソッドを呼び出す
        boolean result = sessionService.createSession(request);

        // メソッドが1回呼ばれたことを検証
        verify(sessionManagerService).createSession(request, sessionAuthService);

        // 結果がtrueであることを確認
        assertTrue(result);
    }

    @Test
    void testEndSession() {
        String uuid = "test-uuid";

        // モックがcleanupを正しく呼ぶことを検証
        doReturn(true).when(sessionCleanupService).cleanup(uuid);

        // 実際にメソッドを呼び出す
        boolean result = sessionService.endSession(uuid);

        // メソッドが1回呼ばれたことを検証
        verify(sessionCleanupService).cleanup(uuid);

        // 結果がtrueであることを確認
        assertTrue(result);
    }

    @Test
    void testSessionExpiration() {
        // 時刻を固定するために Clock を使用
        Clock fixedClock = Clock.fixed(Instant.now().minusSeconds(600), ZoneId.systemDefault());
        Instant fixedInstant = Instant.now(fixedClock);

        // セッションのリクエストデータ
        Session_request request = new Session_request("expired-uuid", "password");

        // モックされた sessionMap を作成
        Map<String, Session_metadata> mockedSessionMap = mock(Map.class);

        // セッションが期限切れの状態を作成（現在時刻を固定）
        Session_metadata expiredSession = new Session_metadata("hashed-password", fixedInstant);

        // sessionManagerService の getSessionMap() をモックして mockedSessionMap を返すようにする
        when(sessionManagerService.getSessionMap()).thenReturn(mockedSessionMap);
        
        // mockedSessionMap の get() メソッドをモックして expiredSession を返すようにする
        when(mockedSessionMap.get(request.getUuid())).thenReturn(expiredSession);

        // cleanup メソッドが boolean を返すようにモック
        doReturn(true).when(sessionCleanupService).cleanup(request.getUuid());  // boolean を返すように設定

        // 実際にメソッドを呼び出し、セッションの有効期限切れを検出
        Invalid_session_exception thrown = assertThrows(Invalid_session_exception.class, () -> sessionService.joinSession(request));

        // 例外メッセージを確認
        assertEquals("セッションの有効期限が切れています。", thrown.getMessage());

        // cleanup が呼ばれたことを確認
        verify(sessionCleanupService).cleanup(request.getUuid());
    }

}
