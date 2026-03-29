package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.HandshakeData;
import com.process.clash.application.realtime.port.out.SocketTokenPort;
import com.process.clash.infrastructure.config.security.AllowedOriginService;
import com.process.clash.infrastructure.config.security.CorsProperties;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocketIoAuthServiceTest {

    @Mock
    private SocketTokenPort socketTokenPort;

    @Mock
    private HandshakeData handshakeData;

    @Test
    @DisplayName("허용된 origin과 유효한 토큰이면 Socket.IO 인증을 허용")
    void isAuthorized_AllowsKnownOriginWithValidToken() {
        // given
        SocketIoAuthService socketIoAuthService = new SocketIoAuthService(
                socketTokenPort,
                new AllowedOriginService(new CorsProperties(List.of(
                        "https://api.clash.kr", "https://clash.kr", "app://clash",
                        "https://local.clash.kr:5173", "https://local.clash.kr:5174")))
        );
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Origin", "app://clash");
        when(handshakeData.getHttpHeaders()).thenReturn(headers);
        when(handshakeData.getSingleUrlParam("token")).thenReturn("socket-token");
        when(socketTokenPort.resolveUserId("socket-token")).thenReturn(Optional.of(1L));

        // when
        boolean authorized = socketIoAuthService.isAuthorized(handshakeData);

        // then
        assertThat(authorized).isTrue();
        verify(socketTokenPort).resolveUserId("socket-token");
    }

    @Test
    @DisplayName("허용되지 않은 origin이면 토큰 검증 전에 Socket.IO 인증을 거부")
    void isAuthorized_RejectsUnknownOrigin() {
        // given
        SocketIoAuthService socketIoAuthService = new SocketIoAuthService(
                socketTokenPort,
                new AllowedOriginService(new CorsProperties(List.of(
                        "https://api.clash.kr", "https://clash.kr", "app://clash",
                        "https://local.clash.kr:5173", "https://local.clash.kr:5174")))
        );
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Origin", "https://evil.example");
        when(handshakeData.getHttpHeaders()).thenReturn(headers);

        // when
        boolean authorized = socketIoAuthService.isAuthorized(handshakeData);

        // then
        assertThat(authorized).isFalse();
        verify(socketTokenPort, never()).resolveUserId("socket-token");
    }

    @Test
    @DisplayName("origin은 허용되어도 토큰이 없으면 Socket.IO 인증을 거부")
    void isAuthorized_RejectsWhenTokenMissing() {
        // given
        SocketIoAuthService socketIoAuthService = new SocketIoAuthService(
                socketTokenPort,
                new AllowedOriginService(new CorsProperties(List.of(
                        "https://api.clash.kr", "https://clash.kr", "app://clash",
                        "https://local.clash.kr:5173", "https://local.clash.kr:5174")))
        );
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Origin", "app://clash");
        when(handshakeData.getHttpHeaders()).thenReturn(headers);
        when(handshakeData.getSingleUrlParam("token")).thenReturn(null);

        // when
        boolean authorized = socketIoAuthService.isAuthorized(handshakeData);

        // then
        assertThat(authorized).isFalse();
    }

}
