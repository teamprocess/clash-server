package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.process.clash.infrastructure.config.SocketIoProperties;
import com.process.clash.infrastructure.config.socket.SocketIoExceptionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SocketIoProperties.class)
@RequiredArgsConstructor
public class SocketIoConfig {

    private final SocketIoProperties socketIoProperties;

    @Bean
    public SocketIOServer socketIOServer(SocketIoAuthService authService) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(socketIoProperties.getHost());
        config.setPort(socketIoProperties.getPort());
        config.setContext(socketIoProperties.getPath());
        config.setOrigin(null);
        config.setPingInterval(socketIoProperties.getPingIntervalMs());
        config.setPingTimeout(socketIoProperties.getPingTimeoutMs());
        config.setTransports(Transport.WEBSOCKET);
        config.setAuthorizationListener(authService::authorize);

        config.setExceptionListener(new SocketIoExceptionListener());

        return new SocketIOServer(config);
    }
}
