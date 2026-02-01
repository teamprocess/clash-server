package com.process.clash.application.realtime.service;

import com.process.clash.application.common.util.TokenGenerator;
import com.process.clash.application.realtime.data.IssueSocketTokenData;
import com.process.clash.application.realtime.port.in.IssueSocketTokenUseCase;
import com.process.clash.application.realtime.port.out.SocketTokenPort;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueSocketTokenService implements IssueSocketTokenUseCase {

    private final TokenGenerator tokenGenerator;
    private final SocketTokenPort socketTokenPort;

    @Value("${realtime.socketio.token-ttl-seconds:300}")
    private long tokenTtlSeconds;

    @Override
    public IssueSocketTokenData.Result issueToken(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        String token = tokenGenerator.generateCleanToken();
        socketTokenPort.storeToken(token, userId, Duration.ofSeconds(tokenTtlSeconds));
        return new IssueSocketTokenData.Result(token, tokenTtlSeconds);
    }
}
