package com.process.clash.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "realtime.socketio")
public class SocketIoProperties {

    private String host = "0.0.0.0";
    private int port = 9092;
    private String path = "/socket.io";
    private String allowedOrigins = "*";
    private int pingIntervalMs = 25000;
    private int pingTimeoutMs = 60000;
    private long tokenTtlSeconds = 300;

}
