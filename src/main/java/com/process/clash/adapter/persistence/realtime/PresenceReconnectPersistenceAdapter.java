package com.process.clash.adapter.persistence.realtime;

import com.process.clash.application.realtime.port.out.PresenceReconnectStatePort;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresenceReconnectPersistenceAdapter implements PresenceReconnectStatePort {

    private static final String RECONNECT_KEY_PREFIX = "presence:reconnect:user:";
    private static final String RECONNECT_DEADLINE_ZSET_KEY = "presence:reconnect:deadline";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void markReconnectPending(Long userId, Instant reconnectDeadline) {
        if (userId == null || reconnectDeadline == null) {
            return;
        }

        String member = userId.toString();
        String value = String.valueOf(reconnectDeadline.toEpochMilli());
        stringRedisTemplate.opsForValue().set(reconnectKey(userId), value);
        stringRedisTemplate.opsForZSet().add(
            RECONNECT_DEADLINE_ZSET_KEY,
            member,
            reconnectDeadline.toEpochMilli()
        );
    }

    @Override
    public void clearReconnectPending(Long userId) {
        if (userId == null) {
            return;
        }

        String member = userId.toString();
        stringRedisTemplate.delete(reconnectKey(userId));
        stringRedisTemplate.opsForZSet().remove(RECONNECT_DEADLINE_ZSET_KEY, member);
    }

    @Override
    public boolean isReconnectPending(Long userId) {
        if (userId == null) {
            return false;
        }
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(reconnectKey(userId)));
    }

    @Override
    public Set<Long> findReconnectPendingUserIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Set.of();
        }

        List<Long> ids = userIds.stream()
            .filter(java.util.Objects::nonNull)
            .distinct()
            .toList();
        if (ids.isEmpty()) {
            return Set.of();
        }

        List<String> keys = ids.stream()
            .map(this::reconnectKey)
            .toList();
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        if (values == null || values.isEmpty()) {
            return Set.of();
        }

        Set<Long> reconnectingUserIds = new LinkedHashSet<>();
        for (int i = 0; i < ids.size() && i < values.size(); i++) {
            if (values.get(i) != null) {
                reconnectingUserIds.add(ids.get(i));
            }
        }
        return reconnectingUserIds;
    }

    @Override
    public List<Long> findExpiredReconnectUserIds(Instant deadlineInclusive) {
        if (deadlineInclusive == null) {
            return List.of();
        }

        Set<String> members = stringRedisTemplate.opsForZSet().rangeByScore(
            RECONNECT_DEADLINE_ZSET_KEY,
            Double.NEGATIVE_INFINITY,
            deadlineInclusive.toEpochMilli()
        );
        if (members == null || members.isEmpty()) {
            return List.of();
        }

        return members.stream()
            .map(this::parseUserId)
            .filter(java.util.Objects::nonNull)
            .toList();
    }

    private String reconnectKey(Long userId) {
        return RECONNECT_KEY_PREFIX + userId;
    }

    private Long parseUserId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
