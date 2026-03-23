package com.process.clash.application.realtime.port.out;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PresenceReconnectStatePort {

    void markReconnectPending(Long userId, Instant reconnectDeadline);

    void clearReconnectPending(Long userId);

    boolean isReconnectPending(Long userId);

    Set<Long> findReconnectPendingUserIds(Collection<Long> userIds);

    List<Long> findExpiredReconnectUserIds(Instant deadlineInclusive);
}
