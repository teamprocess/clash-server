package com.process.clash.application.realtime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresenceReconnectTimeoutService {

    private final UserPresenceService userPresenceService;

    public int expireReconnectTimeouts() {
        return userPresenceService.expireReconnectingUsers();
    }
}
