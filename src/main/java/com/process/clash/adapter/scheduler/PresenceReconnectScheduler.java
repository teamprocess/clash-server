package com.process.clash.adapter.scheduler;

import com.process.clash.application.realtime.service.PresenceReconnectRecoveryService;
import com.process.clash.application.realtime.service.PresenceReconnectTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PresenceReconnectScheduler {

    private final PresenceReconnectTimeoutService presenceReconnectTimeoutService;
    private final PresenceReconnectRecoveryService presenceReconnectRecoveryService;

    @Scheduled(
        fixedDelayString = "${realtime.socketio.reconnect-sweep-fixed-delay-ms:10000}",
        initialDelayString = "${realtime.socketio.reconnect-sweep-fixed-delay-ms:10000}"
    )
    public void expireReconnectTimeouts() {
        safeExpireReconnectTimeouts("scheduled");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void recoverActiveSessionsOnStartup() {
        safeExpireReconnectTimeouts("startup");
        safeRecoverActiveSessions("startup");
    }

    private void safeExpireReconnectTimeouts(String trigger) {
        try {
            int expiredCount = presenceReconnectTimeoutService.expireReconnectTimeouts();
            if (expiredCount > 0) {
                log.info("Presence reconnect timeout applied. trigger={}, expiredCount={}", trigger, expiredCount);
            }
        } catch (Exception exception) {
            log.error("Presence reconnect timeout failed. trigger={}", trigger, exception);
        }
    }

    private void safeRecoverActiveSessions(String trigger) {
        try {
            int recoveredCount = presenceReconnectRecoveryService.recoverActiveSessionUsers();
            if (recoveredCount > 0) {
                log.info("Presence reconnect recovery applied. trigger={}, recoveredCount={}", trigger, recoveredCount);
            }
        } catch (Exception exception) {
            log.error("Presence reconnect recovery failed. trigger={}", trigger, exception);
        }
    }
}
