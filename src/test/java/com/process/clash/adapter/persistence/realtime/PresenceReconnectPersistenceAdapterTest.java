package com.process.clash.adapter.persistence.realtime;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresenceReconnectPersistenceAdapterTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    private PresenceReconnectPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PresenceReconnectPersistenceAdapter(stringRedisTemplate);
    }

    @Test
    @DisplayName("reconnect pending 기록은 Redis transaction으로 원자적으로 저장한다")
    @SuppressWarnings("unchecked")
    void markReconnectPending_executesInTransaction() {
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        ZSetOperations<String, String> zSetOperations = mock(ZSetOperations.class);
        RedisOperations<String, String> redisOperations = mock(RedisOperations.class);
        Instant reconnectDeadline = Instant.parse("2026-03-19T00:01:00Z");

        when(redisOperations.opsForValue()).thenReturn(valueOperations);
        when(redisOperations.opsForZSet()).thenReturn(zSetOperations);
        when(redisOperations.exec()).thenReturn(List.of(true, true));
        when(stringRedisTemplate.execute(any(SessionCallback.class))).thenAnswer(invocation -> {
            SessionCallback<List<Object>> callback = invocation.getArgument(0);
            return callback.execute(redisOperations);
        });

        adapter.markReconnectPending(1L, reconnectDeadline);

        verify(redisOperations).multi();
        verify(valueOperations).set("presence:reconnect:user:1", String.valueOf(reconnectDeadline.toEpochMilli()));
        verify(zSetOperations).add("presence:reconnect:deadline", "1", reconnectDeadline.toEpochMilli());
        verify(redisOperations).exec();
    }

    @Test
    @DisplayName("Redis transaction 결과가 없으면 reconnect 기록을 실패로 본다")
    void markReconnectPending_throwsWhenTransactionFails() {
        when(stringRedisTemplate.execute(any(SessionCallback.class))).thenReturn(null);

        assertThatThrownBy(() -> adapter.markReconnectPending(1L, Instant.parse("2026-03-19T00:01:00Z")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Presence reconnect redis transaction failed");
    }
}
