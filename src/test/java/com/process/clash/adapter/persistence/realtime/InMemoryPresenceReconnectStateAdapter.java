package com.process.clash.adapter.persistence.realtime;

import com.process.clash.application.realtime.port.out.PresenceReconnectStatePort;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Component
@Profile("test")
public class InMemoryPresenceReconnectStateAdapter implements PresenceReconnectStatePort {

    private final ConcurrentHashMap<Long, Instant> reconnectDeadlineByUserId = new ConcurrentHashMap<>();

    @Override
    public void markReconnectPending(Long userId, Instant reconnectDeadline) {
        if (userId == null || reconnectDeadline == null) {
            return;
        }
        reconnectDeadlineByUserId.put(userId, reconnectDeadline);
    }

    @Override
    public void clearReconnectPending(Long userId) {
        if (userId == null) {
            return;
        }
        reconnectDeadlineByUserId.remove(userId);
    }

    @Override
    public boolean isReconnectPending(Long userId) {
        if (userId == null) {
            return false;
        }
        return reconnectDeadlineByUserId.containsKey(userId);
    }

    @Override
    public Set<Long> findReconnectPendingUserIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Set.of();
        }

        return userIds.stream()
            .filter(reconnectDeadlineByUserId::containsKey)
            .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public List<Long> findExpiredReconnectUserIds(Instant deadlineInclusive) {
        if (deadlineInclusive == null) {
            return List.of();
        }

        return reconnectDeadlineByUserId.entrySet().stream()
            .filter(entry -> !entry.getValue().isAfter(deadlineInclusive))
            .map(java.util.Map.Entry::getKey)
            .toList();
    }
}
